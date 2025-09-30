package com.br.ilawgestao.domains.service;

import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.SaldoInsuficienteException;
import com.br.ilawgestao.domains.models.*;
import com.br.ilawgestao.domains.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepostory lancamentoRepository;
	
	@Autowired
	private TipoDespesaReceitaRepository tipoDespesaReceitaRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private PartesRepository partesRepository;
	
	@Autowired
	private MesRepository mesRepository;
	
	@Autowired
	private ArquivoFinanceiroService arquivosService;
	
	@Autowired
	private ArquivoFinanceiroRepository arquivoFianceiroRepository;
	
	@Autowired
	private AtividadeService atividadeService;
	
	@Autowired
	private GrupoTrabalhoRepository grupoTrabalhoRepository;
	
	@Autowired
	private GrupoTrabalhoService grupoTrabalhoService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TransacaoFinanceiraService transacaoFinanceiraService;

	@Autowired
	private ContaFinanceiraRepository contaFinanceiraRepository;

	@Autowired
	private StatusAtividadeRepository statusAtividadeRepository;

	private final int PERFIL_FINANCEIRO = 6;
	private final int PERFIL_ADMINISTRADOR = 1;
		
	public List<Mes> listarMeses() {
		return mesRepository.findAll();
	}
	
	public LancamentoDto cadastrarLancamento(LancamentoDto dto) {
		Optional<TipoDespesaReceita> tipo = tipoDespesaReceitaRepository.findById(dto.getTipo().getCodigo());
		if(!tipo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Despesa ou Receita não encontrado");
		}
		
		Processo processoCadastro = null;
		ProcessoDto processoDto = new ProcessoDto();
		if(dto.getProcesso() != null) {
			Optional<Processo> processo = processoRepository.findById(dto.getProcesso().getCodigo());
			if(!processo.isPresent()) {
				throw new EntidadeNaoEncontradaException("Processo não encontrado");		
			}
			processoCadastro = processo.get();
			processoDto.setCodigo(processo.get().getCodigo());
		}
		
		Lancamento lancamento = Lancamento.builder()
				.dtLancamento(DatasUtil.getDataAtual())
				.dtVencimento(dto.getDtVencimento())
				.vlLancamento(dto.getVlLancamento())
				.vlPago(0)
				.dtPago(null)
				.situacao("A") //Ativo
				.tipo(dto.getTipo())
				.dsLancamento(dto.getDsLancamento())
				.observacao(dto.getObservacao())
				.processo(processoCadastro)
				.usuario(dto.getUsuario())
				.build();
		
		LancamentoDto lanc = LancamentoDto.buildConsulta(lancamentoRepository.save(lancamento), processoDto, dto.getConta());
		
		if(lanc != null) {
			//Verifica se a opção de atividade foi marcada
			if(dto.getCriaAtividade().equals("S")) {
				AtividadeCadastroDTO atividadeDto = new AtividadeCadastroDTO();
				Atividade atividade = new Atividade();
				if(lanc.getDsLancamento() == null || lanc.getDsLancamento().equals("")) {
					atividade.setTitulo(lanc.getTipo().getNome());
				} else {
					atividade.setTitulo(lanc.getDsLancamento());
				}
				atividade.setDescricao(lanc.getObservacao());
				atividade.setDtLimite(dto.getDtVencimento());
				atividade.setDtFatal("");
				atividade.setDtRegistro(DatasUtil.getDataAtual());
				atividade.setTipo("L");
				atividade.setImportante("N");
				atividade.setUrgente("N");
				atividade.setPrivado("N");
				atividade.setLancamentoFinanceiro(LancamentoDto.build(lanc));
				StatusAtividade statusAtividade = new StatusAtividade();
				Optional<StatusAtividade> status = statusAtividadeRepository.findByStatusAndEmpresaCodigo("Criada",dto.getUsuario().getEmpresa().getCodigo());
				statusAtividade.setCodigo(status.get().getCodigo()); // Criada
				atividade.setStatus(statusAtividade);
				atividade.setUsuario(lanc.getUsuario());
				if(lanc.getProcesso() != null) {
					Optional<Processo> processoConsulta = processoRepository.findById(lanc.getProcesso().getCodigo());
					Optional<GrupoTrabalho> grupoTrabalhoEntity = grupoTrabalhoRepository.findGrupoTrabalhoByNomeAndGrupoPai("Financeiro", 
							processoConsulta.get().getGrupoTrabalho().getCodigo());
					GrupoTrabalho grupoTrabalho = null;
					if(!grupoTrabalhoEntity.isPresent()) {
						//Não existe um sub-grupo de trabalho com esse nome, sendo assim, cadastra-se neste momentto
						GrupoTrabalho grupoCadastro = new GrupoTrabalho();
						grupoCadastro.setNome("Financeiro");
						grupoCadastro.setDescricao("Sub Grupo criado para lanlçamentos financeiros");
						Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuario().getCodigo());
						grupoCadastro.setEmpresa(usuario.get().getEmpresa());
						grupoCadastro.setGrupoPai(processoConsulta.get().getGrupoTrabalho().getCodigo());
						GrupoTrabalho subGrupoSalvo = grupoTrabalhoRepository.save(grupoCadastro);
						grupoTrabalho = subGrupoSalvo;
					} else {
						grupoTrabalho = grupoTrabalhoEntity.get();
					}
					atividade.setSubGrupo(grupoTrabalho);
					atividade.setProcesso(processoConsulta.get());
				} else {
					Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuario().getCodigo());
					Optional<GrupoTrabalho> grupoTrabalhoEntity = grupoTrabalhoRepository.consultarGrupoTrabalhoPoNome("Lançamentos Financeiros", 
							usuario.get().getEmpresa().getCodigo());
					GrupoTrabalho grupoTrabalhoSalvo = null;
					if(!grupoTrabalhoEntity.isPresent()) {
						//Cria grupo com seus sub-grupos
						GrupoTrabalho grupoTrabalho = new GrupoTrabalho();
						grupoTrabalho.setNome("Lançamentos Financeiros");
						grupoTrabalho.setDescricao("Grupo de Trabalho destinado aos registros de Lançamentos Financeiros");
						grupoTrabalho.setEmpresa(usuario.get().getEmpresa());
						grupoTrabalhoSalvo = grupoTrabalhoService.cadastrarGrupoTrabalho(grupoTrabalho);
					} else {
						grupoTrabalhoSalvo = grupoTrabalhoEntity.get();
					}
					Optional<GrupoTrabalho> grupoTrabalhoEntity2 = grupoTrabalhoRepository.findGrupoTrabalhoByNomeAndGrupoPai("Financeiro", 
							grupoTrabalhoSalvo.getCodigo());
					atividade.setSubGrupo(grupoTrabalhoEntity2.get());
				}
					
				atividadeDto.setAtividade(atividade);
				List<Usuario> responsaveis = new ArrayList<Usuario>();
				responsaveis.add(lanc.getUsuario());
				atividadeDto.setResponsaveis(responsaveis);
				atividadeDto.setInteressados(null);
				atividadeDto.setRecorrencia(0);
				atividadeService.incluirAtividade(atividadeDto);
			}
			//Arquivos annexados
			List<ArquivoFinanceiroTemp> arquivos = arquivosService.listarTemp(lanc.getUsuario().getCodigo());
			if(arquivos != null) {
				for(ArquivoFinanceiroTemp arq : arquivos) {
					ArquivoFinanceiro arquivo = new ArquivoFinanceiro();
					arquivo.setLancamento(LancamentoDto.build(lanc));
					arquivo.setUsuario(lanc.getUsuario());
					arquivo.setDescricao(arq.getDescricao());
					arquivo.setFile(arq.getFile());
					arquivo.setArquivo(arq.getArquivo());
					arquivo.setTipo(arq.getTipo());
					arquivo.setDtRegistro(DatasUtil.getDataAtual());
					arquivoFianceiroRepository.save(arquivo);
				}
			}
		}
		
		return lanc;
	}

	private TransacaoFinanceiraDto montaTransacaoFinanceira(LancamentoDto lancamento, String tipo) {
		TransacaoFinanceiraDto transacao = new TransacaoFinanceiraDto();
		transacao.setLancamento(LancamentoDto.build(lancamento));
		transacao.setContaFinanceira(ContaFinanceiraDto.build(lancamento.getConta()));
		if(tipo.equals("R")) {
			transacao.setObservacao("Lançamento reaberto");
		} else if(tipo.equals("P")) {
			transacao.setObservacao("Lançamento Pago");
		} else {
			transacao.setObservacao("Lançamento cancelado");
		}
		transacao.setUsuario(lancamento.getUsuario());
		return transacao;
	}
	
	public void cadastrarLancamentoParcelada(LancamentoDto dto, long parcelas, String recorrencia) {
		Optional<TipoDespesaReceita> tipo = tipoDespesaReceitaRepository.findById(dto.getTipo().getCodigo());
		if(!tipo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Despesa ou Receita não encontrado");
		}
		
		ProcessoDto processoDto = new ProcessoDto();
		Processo processoCadastro = null;
		if(dto.getProcesso() != null) {
			Optional<Processo> processo = processoRepository.findById(dto.getProcesso().getCodigo());
			if(!processo.isPresent()) {
				throw new EntidadeNaoEncontradaException("Processo não encontrado");
			}
			processoCadastro = processo.get();
			processoDto.setCodigo(processo.get().getCodigo());
		}
		
		Calendar c = Calendar.getInstance();
		
		int contador = 0;
		
		SimpleDateFormat formatoBanco = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dataLancamento = null; 
		try {
			dataLancamento = formatoBanco.parse(dto.getDtVencimento());
		} catch(Exception e ) {
			e.printStackTrace();
		}
		
		double valorParcela = 0;
		
		if(recorrencia.equals("P")) {
			valorParcela = dto.getVlLancamento() / parcelas;
		} else {
			valorParcela = dto.getVlLancamento();
		}
		
		for(int i = 0; i < parcelas; i++) {
			c.setTime(dataLancamento);
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) + contador);
			Lancamento lancamanto = Lancamento.builder()
					.dtLancamento(DatasUtil.getDataAtual())
					.dtVencimento(formatoBanco.format(c.getTime()))
					.dtPago(null)
					.vlLancamento(valorParcela)
					.vlPago(0)
					.situacao("A")
					.tipo(dto.getTipo())
					.dsLancamento(dto.getDsLancamento())
					.observacao(dto.getObservacao())
					.usuario(dto.getUsuario())
					.processo(processoCadastro)
					.build();
			Lancamento lancSalvo = lancamentoRepository.save(lancamanto);
			if(lancSalvo != null) {
				if(dto.getCriaAtividade().equals("S")) {
					AtividadeCadastroDTO atividadeDto = new AtividadeCadastroDTO();
					Atividade atividade = new Atividade();
					atividade.setTitulo(lancSalvo.getTipo().getNome());
					atividade.setDescricao(lancSalvo.getObservacao());
					atividade.setDtLimite(formatoBanco.format(c.getTime()));
					atividade.setDtFatal("");
					atividade.setDtRegistro(DatasUtil.getDataAtual());
					atividade.setTipo("L");
					atividade.setImportante("N");
					atividade.setUrgente("N");
					atividade.setPrivado("N");
					StatusAtividade statusAtividade = new StatusAtividade();
					statusAtividade.setCodigo(1); // Ativo
					atividade.setStatus(statusAtividade);
					atividade.setUsuario(lancSalvo.getUsuario());
					if(lancSalvo.getProcesso() != null) {
						Optional<Processo> processoConsulta = processoRepository.findById(lancSalvo.getProcesso().getCodigo());
						Optional<GrupoTrabalho> grupoTrabalhoEntity = grupoTrabalhoRepository.findGrupoTrabalhoByNomeAndGrupoPai("Financeiro", 
								processoConsulta.get().getGrupoTrabalho().getCodigo());
						GrupoTrabalho grupoTrabalho = null;
						if(!grupoTrabalhoEntity.isPresent()) {
							//Não existe um sub-grupo de trabalho com esse nome, sendo assim, cadastra-se neste momentto
							GrupoTrabalho grupoCadastro = new GrupoTrabalho();
							grupoCadastro.setNome("Financeiro");
							grupoCadastro.setDescricao("Sub Grupo criado para lanlçamentos financeiros");
							Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuario().getCodigo());
							grupoCadastro.setEmpresa(usuario.get().getEmpresa());
							grupoCadastro.setGrupoPai(processoConsulta.get().getGrupoTrabalho().getCodigo());
							GrupoTrabalho subGrupoSalvo = grupoTrabalhoRepository.save(grupoCadastro);
							grupoTrabalho = subGrupoSalvo;
						} else {
							grupoTrabalho = grupoTrabalhoEntity.get();
						}
						atividade.setSubGrupo(grupoTrabalho);
						atividade.setProcesso(processoConsulta.get());
					} else {
						Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuario().getCodigo());
						Optional<GrupoTrabalho> grupoTrabalhoEntity = grupoTrabalhoRepository.consultarGrupoTrabalhoPoNome("Lançamentos Financeiros", 
								usuario.get().getEmpresa().getCodigo());
						GrupoTrabalho grupoTrabalhoSalvo = null;
						if(!grupoTrabalhoEntity.isPresent()) {
							//Cria grupo com seus sub-grupos
							GrupoTrabalho grupoTrabalho = new GrupoTrabalho();
							grupoTrabalho.setNome("Lançamentos Financeiros");
							grupoTrabalho.setDescricao("Grupo de Trabalho destinado aos registros de Lançamentos Financeiros");
							grupoTrabalho.setEmpresa(usuario.get().getEmpresa());
							grupoTrabalhoSalvo = grupoTrabalhoService.cadastrarGrupoTrabalho(grupoTrabalho);
						} else {
							grupoTrabalhoSalvo = grupoTrabalhoEntity.get();
						}
						Optional<GrupoTrabalho> grupoTrabalhoEntity2 = grupoTrabalhoRepository.findGrupoTrabalhoByNomeAndGrupoPai("Financeiro", 
								grupoTrabalhoSalvo.getCodigo());
						atividade.setSubGrupo(grupoTrabalhoEntity2.get());
					}
						
					atividadeDto.setAtividade(atividade);
					List<Usuario> responsaveis = new ArrayList<Usuario>();
					List<Usuario> usuariosResponsaveis = usuarioRepository.findByPerfilCodigoAndEmpresaCodigo(PERFIL_FINANCEIRO,dto.getUsuario().getEmpresa().getCodigo());
					List<Usuario> ususariosEncontrados = null;
					if(usuariosResponsaveis != null && usuariosResponsaveis.size() > 0) {
						ususariosEncontrados = usuariosResponsaveis;
					} else {
						ususariosEncontrados = usuarioRepository.findByPerfilCodigoAndEmpresaCodigo(PERFIL_ADMINISTRADOR,dto.getUsuario().getEmpresa().getCodigo());
					}
					responsaveis.add(lancSalvo.getUsuario());
					atividadeDto.setResponsaveis(ususariosEncontrados);
					atividadeDto.setInteressados(null);
					atividadeDto.setRecorrencia(0);
					atividadeService.incluirAtividade(atividadeDto);
				}
			}
			contador++;
		}
	}
	
	public List<LancamentoPorTipoDto> lancamentosPoTipoDespesaData(long empresa) {
		String dataInicial = DatasUtil.getAnoAtual() + "-01-01";
		String dataFinal = DatasUtil.getAnoAtual() + "-12-31";
		List<LancamentoPorTipoDto> dtos = lancamentoRepository.lancamantoPorTipoDespesaData(dataInicial, dataFinal, empresa);
		return dtos;
	}
	
	public List<LancamentoPorTipoDto> lancamentosPoTipoReceitaData(long empresa) {
		String dataInicial = DatasUtil.getAnoAtual() + "-01-01";
		String dataFinal = DatasUtil.getAnoAtual() + "-12-31";
		return lancamentoRepository.lancamantoPorTipoReceitaData(dataInicial, dataFinal, empresa);
	}
	
	public List<Lancamento> lancamentosPorTipoPeriodo(long empresa, String dataInicial, String dataFinal, String tipo) {
		List<Lancamento> lista = lancamentoRepository.lancamentosPorTipoPeriodo(empresa, dataInicial, dataFinal, tipo);
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		if(lista != null) {
			for(Lancamento lanc : lista) {
				Lancamento l = new Lancamento();
				l.setCodigo(lanc.getCodigo());
				l.setTipo(lanc.getTipo());
				l.setDtVencimento(DatasUtil.formatarDataTela(lanc.getDtVencimento()));
				if(lanc.getDtPago() != null) {
					l.setDtPago(DatasUtil.formatarDataTela(lanc.getDtPago()));
				} else {
					l.setDtPago(null);
				}
				Locale ptBr = new Locale("pt", "BR");
				NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
				
				BigDecimal valorLancamento = new BigDecimal(lanc.getVlLancamento());
				String formatoValorLancamento = nf.format(valorLancamento);
				l.setValorLancamentoFormat(formatoValorLancamento);
				l.setVlLancamento(lanc.getVlLancamento());
				
				BigDecimal valorPago = new BigDecimal(lanc.getVlPago());
				String formatoValorPago = nf.format(valorPago);
				l.setValorPagamentoFormat(formatoValorPago);
				l.setVlPago(lanc.getVlPago());
				l.setSituacao(lanc.getSituacao());
				lancamentos.add(l);
			}
		}
		
		return lancamentos;
	}
	
	public List<Lancamento> lancamentosPorTipoDescricaoPeriodo(long empresa, String dataInicial, String dataFinal, long tipoCodigo, String tipo, String situacao) {
		List<Lancamento> lista = lancamentoRepository.lancamentosPorTipoDescricaoPeriodo(empresa, dataInicial, dataFinal, tipoCodigo, tipo, situacao);
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		if(lista != null) {
			for(Lancamento lanc : lista) {
				Lancamento l = new Lancamento();
				l.setCodigo(lanc.getCodigo());
				l.setTipo(lanc.getTipo());
				l.setDtVencimento(DatasUtil.formatarDataTela(lanc.getDtVencimento()));
				if(lanc.getDtPago() != null) {
					l.setDtPago(DatasUtil.formatarDataTela(lanc.getDtPago()));
				} else {
					l.setDtPago(null);
				}
				Locale ptBr = new Locale("pt", "BR");
				NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
				
				BigDecimal valorLancamento = new BigDecimal(lanc.getVlLancamento());
				String formatoValorLancamento = nf.format(valorLancamento);
				l.setValorLancamentoFormat(formatoValorLancamento);
				l.setVlLancamento(lanc.getVlLancamento());
				
				BigDecimal valorPago = new BigDecimal(lanc.getVlPago());
				String formatoValorPago = nf.format(valorPago);
				l.setValorPagamentoFormat(formatoValorPago);
				l.setVlPago(lanc.getVlPago());
				l.setSituacao(lanc.getSituacao());
				lancamentos.add(l);
			}
		}
		
		return lancamentos;
	}
	
	public List<LancamentoPorTipoGraficoDto> lancamentoGrafico(long empresa, String dataInicial, String dataFinal, String tipo) {
		return lancamentoRepository.lancamentoGrafico(empresa, dataInicial, dataFinal, tipo);
	}
	
	public LancamentoDto consultarLancamento(long codigo) {
		Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
		if(!lancamento.isPresent()) {
			throw new EntidadeNaoEncontradaException("Lançamento Financeiro não encontrado");
		}
		
		ProcessoDto processoDto = null;
		if(lancamento.get().getProcesso() != null) {
			Optional<Processo> processo = processoRepository.findById(lancamento.get().getProcesso().getCodigo());
			List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo.get().getCodigo());
			List<PartesDto> partesDto = new ArrayList<PartesDto>();
			for(Partes part : partes) {
				partesDto.add(PartesDto.build(part));
			}
			processoDto = ProcessoDto.buildSimples(processo.get(), partesDto);
		}

		//Verifica a conta Financeira para este lançamento
		TransacaoFinanceiraDto transacao = transacaoFinanceiraService.consultarPorLancamento(lancamento.get().getCodigo());
		ContaFinanceiraDto conta = null;
		if(transacao != null) {
			conta = ContaFinanceiraDto.buildConsulta(transacao.getContaFinanceira(),null);
		}
		LancamentoDto dto = LancamentoDto.buildConsulta(lancamento.get(), processoDto,conta);
		return dto;
	}
	
	public Lancamento reabrirLancamento(long codigo, LancamentoDto lancamento) {
		Lancamento lancamentoConsulta = lancamentoRepository.findById(codigo).get();
		BeanUtils.copyProperties(lancamento, lancamentoConsulta, "codigo", "tipo", "dtVencimento", "dtLancamento", "vlLancamento", "dsLancamento",
				"usuario", "observacao", "processo");
		lancamentoConsulta.setVlPago(0);
		lancamentoConsulta.setDtPago(null);

		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamentoConsulta);
		if (lancamentoSalvo != null) {
			//Retorna saldo anterior na conta em questão
			transacaoFinanceiraService.incluirTransacao(this.montaTransacaoFinanceira(LancamentoDto.buildConsulta(lancamentoConsulta, null,
					lancamento.getConta()),"R"));
			TransacaoFinanceiraDto transacao = transacaoFinanceiraService.consultarPorLancamento(lancamentoSalvo.getCodigo());
			transacaoFinanceiraService.incluirTransacao(this.montaTransacaoFinanceira(LancamentoDto.buildConsulta(lancamentoConsulta, null,
					lancamento.getConta()), "R"));
		}

		return lancamentoSalvo;
	}
	
	public Lancamento pagarOuCancelarLancamento(long codigo, LancamentoDto lancamento) {
		Lancamento lancamentoConsulta = lancamentoRepository.findById(codigo).get();
		BeanUtils.copyProperties(lancamento, lancamentoConsulta,"codigo","tipo","dtVencimento", "dtLancamento", "vlLancamento","vlPago","dtPago","dsLancamento",
															    "usuario","observacao","processo");
		Lancamento lancamentoPago = lancamentoRepository.save(lancamentoConsulta);

		if(lancamentoPago != null) {

			if (lancamento.getSituacao().equals("C")) {
				if (lancamento.getSituacao().equals("P")) {
					//A conta já estava paga. Neste caso, o laçamento é cancelado, criado uma transação de retorno do valor a conta do lançamento
					transacaoFinanceiraService.incluirTransacao(this.montaTransacaoFinanceira(LancamentoDto.buildConsulta(lancamentoConsulta, null,
							lancamento.getConta()), "C"));
				}
			}


			if (!lancamento.getSituacao().equals("C")) {
				transacaoFinanceiraService.incluirTransacao(this.montaTransacaoFinanceira(LancamentoDto.buildConsulta(lancamentoConsulta, null,
						lancamento.getConta()), "P"));
			}
		}

		return lancamentoPago;
	}
	
	public LancamentoDto pagarOuCancelarLancamentoIndividual(long codigo, LancamentoDto lancamento) {
		Lancamento lancamentoConsulta = lancamentoRepository.findById(codigo).get();
		if(lancamentoConsulta == null) {
			throw new EntidadeNaoEncontradaException("Lançamento não localizado");
		}

		Optional<ContaFinanceira> conta = contaFinanceiraRepository.findById(lancamento.getConta().getCodigo());
		if(!conta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Conta financeira não localizada");
		}

		//Se a operação for de pagamento, verifica se a conta financeira tem saldo para pagar
		if(lancamentoConsulta.getTipo().getTipo().equals("D") && conta.get().getSaldo() < lancamento.getVlPago()) {
			throw new SaldoInsuficienteException("A conta selecionada não tem saldo suficiente para o paamento");
		}

		lancamentoConsulta.setDtPago(DatasUtil.getDataAtual());
		BeanUtils.copyProperties(lancamento, lancamentoConsulta,"codigo","tipo","dtVencimento", "dtLancamento", "vlLancamento","dsLancamento",
															    "usuario","processo");
		lancamentoConsulta.setDtPago(DatasUtil.getDataAtual());
		LancamentoDto lancamentoPago = LancamentoDto.buildConsulta(lancamentoRepository.save(lancamentoConsulta),null,lancamento.getConta());
		if(lancamentoPago != null) {
			transacaoFinanceiraService.incluirTransacao(this.montaTransacaoFinanceira(lancamentoPago,"P"));
		}

		return lancamentoPago;
	}

	public LancamentoDto alterarLancamento(LancamentoDto dto) {
		Optional<Lancamento> lancamentoConsulta = lancamentoRepository.findById(dto.getCodigo());
		if(!lancamentoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Lançamento não encontrado");
		}

		if(lancamentoConsulta.get().getSituacao().equals("C") || lancamentoConsulta.get().getSituacao().equals("P")) {
			throw new EntidadeEmUsoException("Lançamentos cancelados ou pagos não podem ser mais editados");
		}

		Lancamento lancamentoSalvo = Lancamento.builder()
				.codigo(dto.getCodigo())
				.dsLancamento(dto.getDsLancamento())
				.dtVencimento(dto.getDtVencimento())
				.vlLancamento(dto.getVlLancamento())
				.dtPago(lancamentoConsulta.get().getDtPago())
				.vlPago(lancamentoConsulta.get().getVlPago())
				.dtLancamento(lancamentoConsulta.get().getDtLancamento())
				.situacao(lancamentoConsulta.get().getSituacao())
				.tipo(lancamentoConsulta.get().getTipo())
				.usuario(lancamentoConsulta.get().getUsuario())
				.observacao(dto.getObservacao())
				.build();

		LancamentoDto lancamentoDtoSalvo = LancamentoDto.buildConsulta(lancamentoRepository.save(lancamentoSalvo),null,null);
		return lancamentoDtoSalvo;
	}
	
	public List<LancamentoDto> relatorioDetalhado(long empresa, String dataInicial, String dataFinal, long tipo, String categoria, 
			String situacao, long codigo, long processo, String descricao,
			String classificacao, String orderm) {
		
		List<Lancamento> lancamentos = lancamentoRepository.relatorioDetalhado(empresa, dataInicial, dataFinal, tipo, categoria,
				situacao, codigo, processo, descricao, classificacao, orderm);
		List<LancamentoDto> dtos = new ArrayList<LancamentoDto>();
		
		if(lancamentos != null && lancamentos.size() > 0) {
			for(Lancamento lanc : lancamentos) {
				dtos.add(LancamentoDto.buildConsulta(lanc, null,null));
			}
		}
		
		return dtos;
	}
	
	public List<RelatorioFinanceiroResumidoDto> relatorioFinanceiroResumido(long empresa, String dataInicial, String dataFinal,  String situacao, String relatorio) {
		
		List<RelatorioFinanceiroResumidoDto> relatorioGeral = new ArrayList<RelatorioFinanceiroResumidoDto>();
		
		if(relatorio.equals("CATEGORIA")) {
			List<RelatorioFinanceiroResumidoDto> lista = lancamentoRepository.relatorioResumidoCategoria(empresa, dataInicial, dataFinal, situacao);
			if(lista != null) {
				for(RelatorioFinanceiroResumidoDto rel : lista) {
					RelatorioFinanceiroResumidoDto dto = new RelatorioFinanceiroResumidoDto();
					if(rel.getLabel().equals("D")) {
						dto.setLabel("Despesa");
					} else {
						dto.setLabel("Receita");
					}
					dto.setValor(rel.getValor());
					relatorioGeral.add(dto);
				}
			}
		} else if(relatorio.equals("TIPO")) {
			List<RelatorioFinanceiroResumidoDto> lista = lancamentoRepository.relatorioResumidoTipo(empresa, dataInicial, dataFinal, situacao);
			if(lista != null) {
				for(RelatorioFinanceiroResumidoDto rel : lista) {
					RelatorioFinanceiroResumidoDto dto = new RelatorioFinanceiroResumidoDto();
					dto.setLabel(rel.getLabel());
					dto.setValor(rel.getValor());
					relatorioGeral.add(dto);
				}
			}
		}
		
		return relatorioGeral;
	}
	
	public double vencendoAmanha(long empresa) {
		FinanceiroVencendoDto valor = lancamentoRepository.vencendoAmanha(empresa);
		return valor.getVencendo();
	}
	
	public double vencendoHoje(long empresa) {
		FinanceiroVencendoDto valor = lancamentoRepository.vencendoHoje(empresa);
		return valor.getVencendo();
	}
	
	public double pendentes(long empresa) {
		FinanceiroVencendoDto valor = lancamentoRepository.pendentes(empresa);
		return valor.getVencendo();
	}
	
	public List<Lancamento> detalharPendentes(long empresa, long tipoCodigo, String tipo) {
		List<Lancamento> lancamentos = lancamentoRepository.lancamentosPendente(empresa, tipoCodigo, tipo);
		List<Lancamento> listaLancamentos = new ArrayList<Lancamento>();
		
		if(lancamentos != null) {
			for(Lancamento lanc: lancamentos) {
				Lancamento lancamento = new Lancamento();
				lancamento.setCodigo(lanc.getCodigo());
				lancamento.setTipo(lanc.getTipo());
				if(lanc.getTipo().getTipo().equals("D")) {
					lancamento.setCategoria("Despesa");
				} else {
					lancamento.setCategoria("Receita");
				}
				lancamento.setVlLancamento(lanc.getVlLancamento());
				lancamento.setDtVencimento(DatasUtil.formatarDataTela(lanc.getDtVencimento()));
				listaLancamentos.add(lancamento);
			}
		}
		
		return listaLancamentos;
	}
	
	public List<Lancamento> detalharVencendoHoje(long empresa, long tipoCodigo, String tipo) {
		List<Lancamento> lancamentos = lancamentoRepository.lancamentosVencendoHoje(empresa, tipoCodigo, tipo);
		List<Lancamento> listaLancamentos = new ArrayList<Lancamento>();
		
		if(lancamentos != null) {
			for(Lancamento lanc: lancamentos) {
				Lancamento lancamento = new Lancamento();
				lancamento.setCodigo(lanc.getCodigo());
				lancamento.setTipo(lanc.getTipo());
				if(lanc.getTipo().getTipo().equals("D")) {
					lancamento.setCategoria("Despesa");
				} else {
					lancamento.setCategoria("Receita");
				}
				lancamento.setVlLancamento(lanc.getVlLancamento());
				lancamento.setDtVencimento(DatasUtil.formatarDataTela(lanc.getDtVencimento()));
				listaLancamentos.add(lancamento);
			}
		}
		
		return listaLancamentos;
	}

	public List<LancamentosPorContaFinanceiraDto> lancamentosPorConta(String dataInicial, String dataFinal, long empresa, String tipo) {
		List<LancamentosPorContaFinanceiraDto> lancamentos = lancamentoRepository.lancamentosPagosPorContaFinanceira(dataInicial,
				dataFinal, empresa, tipo);
		List<LancamentosPorContaFinanceiraDto> dtos = new ArrayList<>();
		if(lancamentos != null && lancamentos.size() > 0) {
			for(LancamentosPorContaFinanceiraDto lanc : lancamentos) {
				LancamentosPorContaFinanceiraDto dto = new LancamentosPorContaFinanceiraDto();
				dto.setConta(lanc.getConta());
				dto.setValor(lanc.getValor());
				dto.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				dtos.add(dto);
			}
		}

		return dtos;
	}

	private Color gerarCorAleatoriamente(){
		Random randColor = new Random();
		int r = randColor.nextInt(256);
		int g = randColor.nextInt(256);
		int b = randColor.nextInt(256);
		return new Color(r, g, b);
	}

	private String gerarCorHexadecimal(Color color){
		return '#'+
				this.tratarHexString(Integer.toHexString(color.getRed()))+
				this.tratarHexString(Integer.toHexString(color.getGreen()))+
				this.tratarHexString(Integer.toHexString(color.getBlue()));
	}

	private String tratarHexString(String hexString){
		String hex = null;
		if(hexString.length() == 1){
			hex = '0'+hexString;
		}else{
			hex = hexString;
		}
		return hex;
	}
}
