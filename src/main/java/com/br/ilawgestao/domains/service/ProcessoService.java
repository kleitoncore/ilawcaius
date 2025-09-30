package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.models.*;
import com.br.ilawgestao.domains.repository.*;
import com.br.ilawgestao.domains.repository.filtros.FiltroHistoricoAtividadeFase;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeJaCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.repository.filtros.FiltroProcesso;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
@Slf4j
public class ProcessoService {

	private static final Logger log = LoggerFactory.getLogger(ProcessoService.class);
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private PartesRepository partesRepository;
	
	@Autowired
	private GrupoTrabalhoRepository grupoRepository;
	
	@Autowired
	private TipoAcaoRepository tipoAcaoRepository;
	
	@Autowired
	private StatusProcessualRepository statusRepository;
	
	@Autowired
	private AreaAtuacaoRepository areaAtuacaoRepository;
	
	@Autowired
	private TipoDecisaoRepository tipoDecisaoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private IndiceProcessoRepository indiceProcessoRepository;
	
	@Autowired
	private ProcessoImportanciaService processoImportanciaService;
	
	@Autowired
	private HistoricoAlteracaoProcessoRepository historicoReporitory;
	
	@Autowired
	private ProcessoArquivadoRepository processoArquivadoRepository;
	
	@Autowired
	private LancamentoRepostory lancamentoRepository;

	@Autowired
	private TransacaoFinanceiraRepoository transacaoFinanceiraRepoository;
	
	@Autowired
	private ProcessoExcluidoRepository processoExcluidoRepository;
	
	@Autowired
	private PagamentoProcessoRepository pagamentoProcessoRepository;
	
	@Autowired
	private CustasProcessoRepository custasProcessoRepository;
	
	@Autowired
	private GarantiaProcessoRepository garantiasProcessoRepository;
	
	@Autowired
	private PedidoProcessoRepository pedidoProcessoRepository;
	
	@Autowired
	private HistoricoProcessoRepository historicoProcessoRepository;
	
	@Autowired
	private AtividadeRepository atividadeRepository;
	
	@Autowired
	private AtividadeUsuarioRepository atividadeUsuarioRepository;
	
	@Autowired
	private ArquivoProcessoRepository arquivoProcessoRepository;
	
	@Autowired
	private AtividadeService atividadeService;
	
	@Autowired
	private ObjetoAcaoProcessoRepository objetoAcaoProcessoRepository;
	
	@Autowired
	private TipoAndamentoProcessualRepository tipoAndamentoProcessualRepository;
	
	@Autowired
	private AgravoApensoRepository agravoApensoRepository;

	@Autowired
	private FaseRepository faseRepository;

	@Autowired
	private RitoRepository ritoRepository;

	@Autowired
	private AtividadeShortRepository atividadeShortRepository;

	@Autowired
	private HistoricoAtividadeRepository historicoAtividadeRepository;

	@Autowired
	private AgendaRepository agendaRepository;

	@Autowired
	private CentralAtividadeRepository centralAtividadeRepository;

	@Autowired
	private HistoricoFaseProcesualRepository historicoFaseProcesualRepository;

	@Autowired
	private ProcessoImportanciaRepository processoImportanciaRepository;

	@Autowired
	private ArquivoAtividadeRepository arquivoAtividadeRepository;

	private String PROCESSO_EXCLUIDO = "E";
	private long PERFIL_ADMINISTRADOR = 1;
	
	public ProcessoDto cadastrarProcesso(ProcessoDto processoDto) {
		Optional<GrupoTrabalho> grupoTrabalho = grupoRepository.findById(processoDto.getGrupoTrabalho().getCodigo());
		if(!grupoTrabalho.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrado");
		}
		
		Optional<TipoAcao> tipoAcao = tipoAcaoRepository.findById(processoDto.getTipoAcao().getCodigo());
		if(!tipoAcao.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Ação não encontrada");
		}
		
		Optional<StatusProcessual> statusProcessual = statusRepository.findById(processoDto.getStatusProcessual().getCodigo());
		if(!statusProcessual.isPresent()) {
			throw new EntidadeNaoEncontradaException("Status Processual não encontrado");
		}
		
		Optional<AreaAtuacao> areaAtuacao = areaAtuacaoRepository.findById(processoDto.getAreaAtuacao().getCodigo());
		if(!areaAtuacao.isPresent()) {
			throw new EntidadeNaoEncontradaException("Área de Atuação não encontrada");
		}
		
		Optional<Usuario> responsavel = usuarioRepository.findById(processoDto.getResponsavel().getCodigo());
		if(!responsavel.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário Responsável não encontrado");
		}
		
		Optional<Usuario> usuarioCadastro = usuarioRepository.findById(processoDto.getUsuario().getCodigo());
		if(!usuarioCadastro.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		if(processoDto.getNrCnj() != "") {
			Optional<Processo> processoCnj = 
					processoRepository.findByNrCnjAndEmpresaCodigoAndStatus(this.retiraMascara(processoDto.getNrCnj()), processoDto.getEmpresa().getCodigo(),
							0l);
			if(processoCnj.isPresent()) {
				throw new EntidadeJaCadastradaException("Processo com este número de CNJ já cadastrado");
			}
		}
		
		if(processoDto.getNrProcesso() != "") {
			Optional<Processo> processoNumero = processoRepository.findByNrProcessoAndEmpresaCodigoAndStatus(processoDto.getNrProcesso(), processoDto.getEmpresa().getCodigo(),
					0l);
			if(processoNumero.isPresent()) {
				throw new EntidadeJaCadastradaException("Processo com este número já cadastrado");
			}
		}
		
		processoDto.setDataCadastro(DatasUtil.getDataAtual());
		processoDto.setDataUltimaMovimentacao(processoDto.getDataCadastro());
		
		if(processoDto.getDataDistribuicao().equals("") || processoDto.getDataDistribuicao().equals(null)) {
			processoDto.setDataDistribuicao(null);
		}

		if(processoDto.getFase() != null) {
			Optional<Fase> fase = faseRepository.findById(processoDto.getFase().getCodigo());
			if(!fase.isPresent()) {
				throw new EntidadeNaoEncontradaException("Fase Processual não foi encontrada");
			}
		}

		if(processoDto.getRito() != null) {
			Optional<Rito> rito = ritoRepository.findById(processoDto.getRito().getCodigo());
			if(!rito.isPresent()) {
				throw new EntidadeNaoEncontradaException("Rito de Processo não encontrado");
			}
		}
		
		processoDto.setDataSentenca(null);
		
		processoDto.setSnEmail("N");
		processoDto.setSnHistorico("N");
		processoDto.setSnPush("N");
		processoDto.setSnImportante("N");
		processoDto.setEstrategico("N");
		TipoDecisao tipoDecisao = new TipoDecisao();
		tipoDecisao.setCodigo(1);
		processoDto.setTipoDecisao(tipoDecisao);
		
		Processo processo = Processo.builder()
				.grupoTrabalho(processoDto.getGrupoTrabalho())
				.pasta(processoDto.getPasta())
				.nrProcesso(processoDto.getNrProcesso())
				.nrCnj(this.retiraMascara(processoDto.getNrCnj()))
				.nrInstancia(processoDto.getNrInstancia())
				.dsComarca(processoDto.getDsComarca())
				.tipoAcao(processoDto.getTipoAcao())
				.statusProcessual(processoDto.getStatusProcessual())
				.areaAtuacao(processoDto.getAreaAtuacao())
				.tipoDecisao(processoDto.getTipoDecisao())
				.dataDistribuicao(processoDto.getDataDistribuicao())
				.dataUltimaMovimentacao(processoDto.getDataUltimaMovimentacao())
				.dataUltimaDecisao(processoDto.getDataUltimaDecisao())
				.vlProvavel(processoDto.getVlProvavel())
				.vlPossivel(processoDto.getVlPossivel())
				.vlRemoto(processoDto.getVlRemoto())
				.vlCausa(processoDto.getVlCausa())
				.dsPedidos(processoDto.getDsPedidos())
				.observacao(processoDto.getObservacao())
				.usuario(processoDto.getUsuario())
				.responsavel(processoDto.getResponsavel())
				.statusInterno(processoDto.getStatusInterno())
				.dataCadastro(processoDto.getDataCadastro())
				.snPush(processoDto.getSnPush())
				.snEmail(processoDto.getSnEmail())
				.snHistorico(processoDto.getSnHistorico())
				.status(processoDto.getStatus())
				.empresa(processoDto.getEmpresa())
				.snImportante(processoDto.getSnImportante())
				.dataSentenca(processoDto.getDataSentenca())
				.tipoContingenciaContabil(processoDto.getTipoContingenciaContabil())
				.uf(processoDto.getUf())
				.estrategico(processoDto.getEstrategico())
				.orgaoColegiado(processoDto.getOrgaoColegiado())
				.relator(processoDto.getRelator())
				.bancada(processoDto.getBancada())
				.fase(processoDto.getFase())
				.rito(processoDto.getRito())
				.build();
		
		ProcessoDto dto = ProcessoDto.build(processoRepository.save(processo));
		dto.setPartes(processoDto.getPartes());
		dto.setPedidos(processoDto.getPedidos());
		dto.setObjetos(processoDto.getObjetos());
		dto.setNrCnj(processo.getNrCnj());
		if(dto != null) {
			//Incluir partes
			this.incluirPartes(dto);
			//Inclui Objetos
			if(processoDto.getObjetos() != null && processoDto.getObjetos().size() > 0) {
				this.incluirObjetos(dto);
			}
			//Incluir pedidos do processo
			if(processoDto.getPedidos() != null && processoDto.getPedidos().size() > 0) {
				this.incluirPedidos(dto);
			}
			
			this.cadastrarIndiceProcesso(dto);
		}
		dto.setAction("NEW");
		return dto;
	}
	
	private String retiraMascara(String cnj) {
		if(cnj != "" && cnj != null) {
			String retorno = cnj.replace("-", ".");
			       retorno = retorno.replace(".", "");
			return retorno;
		}
		
		return null;
	}
	
	private void incluirObjetos(ProcessoDto processoDto) {
		Processo processo = new Processo();
		processo.setCodigo(processoDto.getCodigo());
		for(ObjetoAcaoProcesso objeto : processoDto.getObjetos()) {
			ObjetoAcaoProcesso oap = new ObjetoAcaoProcesso();
			ObjetoAcao ob = new ObjetoAcao();
			ob.setCodigo(objeto.getCodigo());
			oap.setObjeto(ob);
			oap.setProcesso(processo);
			objetoAcaoProcessoRepository.save(oap);																																																																																																																																																																																																																																																																																																																																																																																																
		}
	}
	
	private void incluirPedidos(ProcessoDto processoDto) {
		Processo processo = new Processo();
		processo.setCodigo(processoDto.getCodigo());
		for(PedidoProcessoDto pedidoProcesso : processoDto.getPedidos()) {
			Pedido pedido = new Pedido();
			pedido.setCodigo(pedidoProcesso.getPedido().getCodigo());
			PedidoProcesso pp = PedidoProcesso.builder()
					.processo(processo)
					.pedido(pedido)
					.vlPossivel(Double.parseDouble(this.tratarValorPedido(pedidoProcesso.getVlPossivel())))
					.vlProvavel(Double.parseDouble(this.tratarValorPedido(pedidoProcesso.getVlPossivel())))
					.vlRemoto(Double.parseDouble(this.tratarValorPedido(pedidoProcesso.getVlRemoto())))
					.dtRegistro(DatasUtil.getDataAtual())
					.build();
			pedidoProcessoRepository.save(pp);
		}
	}
	
	private String tratarValorPedido(String valor) {
			   String strValor = valor.replace(".", "");
			   strValor = strValor.replace(",", ".");
		return strValor;
	}
	
	public List<Processo> consultarProcessosPorIndice(String indice, long empresa, long usuario) {
		String formato = indice.replace("-", ".");
		formato = formato.replace(".", "");
		return processoRepository.consultaProcessoPorIndice(formato,empresa,usuario);
	}
	
	public Partes cadastrarPartes(Partes parte) {
		Optional<Processo> processoConsulta = processoRepository.findById(parte.getProcesso().getCodigo());
		if(!processoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		Optional<Pessoa> pessoaConsulta = pessoaRepository.findById(parte.getPessoa().getCodigo());
		if(!pessoaConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Pessoa não encontrada");
		}
		
		return partesRepository.save(parte);
	}
	
	public List<ProcessoDto> consultarProcessos(FiltroProcesso filtro) {
		List<Processo> processos = processoRepository.consultarProcessos(filtro);
		List<ProcessoDto> dtos = new ArrayList<ProcessoDto>();
		if(processos != null && processos.size() > 0) {
			for(Processo processo : processos) {
				 List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo.getCodigo());
				 List<PartesDto> partesDtos = new ArrayList<PartesDto>(); 
				 for(Partes parte :partes) { 
					PartesDto parteDto = PartesDto.build(parte);
					partesDtos.add(parteDto); 
				 }
				ProcessoImportancia pi = processoImportanciaService.consultarProcessoImportancia(Long.valueOf(filtro.getUsuario()), processo.getCodigo());
				Optional<AgravoApenso> agravoApenso = agravoApensoRepository.findByProcessoCodigo(processo.getCodigo());
				dtos.add(ProcessoDto.buildShort(processo, partesDtos, agravoApenso.isPresent() ? "S" : "N", 
						pi != null ? "S" : "N"));
			}
		}
		
		return dtos;
		
	}
	
	public List<Partes> consultarPartes(long processo) {
		return partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo);
	}
	
	public ProcessoDto consultarProcesso(long codigo, long usuario) {
		Optional<Processo> processo = processoRepository.findById(codigo);
		if(!processo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		List<PagamentoProcesso> pagamentosLista = pagamentoProcessoRepository.findByProcessoCodigoOrderByDtPagamentoDesc(processo.get().getCodigo());
		List<PagamentoProcesso> pagamentos = new ArrayList<PagamentoProcesso>();
		if(pagamentosLista != null && pagamentosLista.size() > 0) {
			for(PagamentoProcesso pag : pagamentosLista) {
				PagamentoProcesso p = new PagamentoProcesso();
				p.setCodigo(pag.getCodigo());
				p.setPagamento(pag.getPagamento());
				p.setProcesso(pag.getProcesso());
				p.setDtPagamento(DatasUtil.formatarDataTela(pag.getDtPagamento()));
				p.setVlPagamento(pag.getVlPagamento());
				if(pag.getTipo().equals("C")) {
					p.setTipo("Crédito");
				} else {
					p.setTipo("Débito");
				}
				pagamentos.add(p);
			}
		}
		List<CustasProcesso> custasLista = custasProcessoRepository.findByProcessoCodigoOrderByCustasNomeAsc(processo.get().getCodigo());
		List<CustasProcesso> custas = new ArrayList<CustasProcesso>();
		if(custasLista != null && custasLista.size() > 0) {
			for(CustasProcesso cp : custasLista) {
				CustasProcesso custa = new CustasProcesso();
				custa.setCodigo(cp.getCodigo());
				custa.setProcesso(cp.getProcesso());
				custa.setDtPagamento(DatasUtil.formatarDataTela(cp.getDtPagamento()));
				if(cp.getTipo().equals("C")) {
					custa.setTipo("Crédito");
				} else {
					custa.setTipo("Débito");
				}
				custa.setCustas(cp.getCustas());
				custa.setVlCustas(cp.getVlCustas());
				custas.add(custa);
			}
		}
		List<GarantiaProcesso> garantias = garantiasProcessoRepository.findByProcessoCodigoOrderByGarantiaNomeAsc(processo.get().getCodigo());
		List<PedidoProcesso> pedidosEntitys = pedidoProcessoRepository.findByProcessoCodigo(processo.get().getCodigo());
		List<ObjetoAcaoProcesso> objetos = objetoAcaoProcessoRepository.findByProcessoCodigoOrderByObjetoNomeAsc(processo.get().getCodigo());
		List<PedidoProcessoDto> pedidos = new ArrayList<PedidoProcessoDto>();
		if(pedidosEntitys != null) {
			for(PedidoProcesso pp : pedidosEntitys) {
				pedidos.add(PedidoProcessoDto.build(pp)); 
			}
		}
		List<Partes> partesEtitys = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo.get().getCodigo());
		List<PartesDto> partes = new ArrayList<PartesDto>();
		if(partesEtitys != null && partesEtitys.size() > 0) {
			for(Partes parte : partesEtitys) {
				partes.add(PartesDto.build(parte));
			}
		}
		
		List<HistoricoProcesso> historicoEntitys = historicoProcessoRepository.findByProcessoCodigoOrderByCodigoDesc(processo.get().getCodigo());
		List<HistoricoProcessoDto> historico = new ArrayList<HistoricoProcessoDto>();
		if(historicoEntitys != null) {
			for(HistoricoProcesso hist : historicoEntitys) {
				historico.add(HistoricoProcessoDto.build(hist));
			}
		}

		List<Lancamento> lancamentosEntitys = lancamentoRepository.findByProcessoCodigoOrderByDtVencimento(processo.get().getCodigo());
		List<LancamentoDto> lancamentosDtos = new ArrayList<LancamentoDto>();
		if(lancamentosEntitys != null && lancamentosEntitys.size() > 0) {
			for(Lancamento lanc : lancamentosEntitys) {
				lancamentosDtos.add(LancamentoDto.buildConsulta(lanc, null,null));
			}
		}
		List<ArquivoProcesso> arquivos = arquivoProcessoRepository.findByProcessoCodigoOrderByNomeAsc(processo.get().getCodigo());
		
		List<AgravoApenso> agravosApensosEntity = agravoApensoRepository.findByProcessoPrincipalCodigo(processo.get().getCodigo());
		List<AgravoApensoDto> agravosApensos = new ArrayList<AgravoApensoDto>();
		if(agravosApensosEntity != null && agravosApensosEntity.size() > 0) {
			for(AgravoApenso agp : agravosApensosEntity) {
				agravosApensos.add(AgravoApensoDto.build(agp));
			}
		}
		
		//Se o processo for agravo ou apenso, manda o processo principal
		Optional<AgravoApenso> agravoApenso = agravoApensoRepository.findByProcessoCodigo(processo.get().getCodigo());
		ProcessoDto dtoProcessoPrincipal = null;
		if(agravoApenso.isPresent()) {
			Optional<Processo> processoPrincipal = processoRepository.findById(agravoApenso.get().getProcessoPrincipal().getCodigo());
			dtoProcessoPrincipal = ProcessoDto.buildConsultaApenso(processoPrincipal.get());
		}

		//Histórico das atividades por fase proessual
		FiltroHistoricoAtividadeFase filtroHistoricoAtividadeFase = new FiltroHistoricoAtividadeFase();
		filtroHistoricoAtividadeFase.setProcesso(processo.get().getCodigo());
		filtroHistoricoAtividadeFase.setFase("0");
		filtroHistoricoAtividadeFase.setStatus("0");
		filtroHistoricoAtividadeFase.setDataLimiteInicial("0");
		filtroHistoricoAtividadeFase.setDataLimiteFinal("0");
		filtroHistoricoAtividadeFase.setDataFatalInicial("0");
		filtroHistoricoAtividadeFase.setDataFatalFinal("0");
		filtroHistoricoAtividadeFase.setImportante("T");
		filtroHistoricoAtividadeFase.setUrgente("T");
		filtroHistoricoAtividadeFase.setClassificacao(2);
		filtroHistoricoAtividadeFase.setOrdem(2);
		List<HistoricoFaseProcessualViewDto> historicoFasePocessual = atividadeService.consultarHistoricoFaseProcssual(filtroHistoricoAtividadeFase,usuario);

		//Gráfico das atividades por status
		List<GraficoAtividadesStatusDto> atividadeStatus = new ArrayList<GraficoAtividadesStatusDto>();
		atividadeStatus = atividadeService.graficoStatus(processo.get().getCodigo());

		//Gráfico das Atividades por Fase
		List<GraficoAtividadeFaseDto> atividadesFases = new ArrayList<>();
		atividadesFases = atividadeService.graficoFase(processo.get().getCodigo());
		
		ProcessoDto dto = ProcessoDto.buildConsulta(processo.get(), partes, 
				objetos, pedidos, pagamentos, custas, garantias, 
				historico, null, arquivos, lancamentosDtos, agravosApensos, dtoProcessoPrincipal,atividadesFases,
				atividadeStatus,historicoFasePocessual);
		
		return dto;
	}
	
	private String listaResponsvaeisAtividade(long atividade) {
		String responsaveis = "";
		List<AtividadeUsuario> lista = atividadeUsuarioRepository.findByAtividadeCodigoAndTipoOrderByUsuarioNome(atividade, "R"); // Só os responsáveis
		for(AtividadeUsuario usuarios: lista) {
			responsaveis = responsaveis + usuarios.getUsuario().getNome() + ",";
		}
		
		return responsaveis;
	}
	
	public ProcessoDto alterarProcesso(long codigo, ProcessoDto processoDto) {
		Optional<GrupoTrabalho> grupoTrabalho = grupoRepository.findById(processoDto.getGrupoTrabalho().getCodigo());
		if(!grupoTrabalho.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrado");
		}
		
		Optional<TipoAcao> tipoAcao = tipoAcaoRepository.findById(processoDto.getTipoAcao().getCodigo());
		if(!tipoAcao.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Ação não encontrada");
		}
		
		Optional<StatusProcessual> statusProcessual = statusRepository.findById(processoDto.getStatusProcessual().getCodigo());
		if(!statusProcessual.isPresent()) {
			throw new EntidadeNaoEncontradaException("Status Processual não encontrado");
		}
		
		Optional<AreaAtuacao> areaAtuacao = areaAtuacaoRepository.findById(processoDto.getAreaAtuacao().getCodigo());
		if(!areaAtuacao.isPresent()) {
			throw new EntidadeNaoEncontradaException("Área de Atuação não encontrada");
		}
		
		if(processoDto.getTipoDecisao() != null) {
			Optional<TipoDecisao> tipoDecisao = tipoDecisaoRepository.findById(processoDto.getTipoDecisao().getCodigo());
			if(!tipoDecisao.isPresent()) {
				throw new EntidadeNaoEncontradaException("Tipo de Decisão não encontrada");
			}
		}
		
		Optional<Usuario> responsavel = usuarioRepository.findById(processoDto.getResponsavel().getCodigo());
		if(!responsavel.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário Responsável não encontrado");
		}
		
		Optional<Usuario> usuarioCadastro = usuarioRepository.findById(processoDto.getUsuario().getCodigo());
		if(!usuarioCadastro.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		ProcessoDto processoConsulta = this.consultarProcesso(codigo, processoDto.getUsuario().getCodigo());
		
		if(processoDto.getNrProcesso() != "" && processoDto.getNrProcesso() != null) {
			Optional<Processo> processoNumeroProcesso = processoRepository.findByNrProcessoAndEmpresaCodigoAndStatus(processoDto.getNrProcesso(), processoDto.getEmpresa().getCodigo(),
					0l);
			if(processoNumeroProcesso.isPresent() && processoNumeroProcesso.get().getCodigo() != processoConsulta.getCodigo()) {
				throw new EntidadeJaCadastradaException("Atenção! Já existe um número de processo cadastrado com este número");
			}
		}
		
		if(processoDto.getNrCnj() != "" && processoDto.getNrCnj() != null) {
			Optional<Processo> processoNumeroCnj = processoRepository.findByNrCnjAndEmpresaCodigoAndStatus(processoDto.getNrCnj(), processoDto.getEmpresa().getCodigo(),
					0l);
			if(processoNumeroCnj.isPresent() && processoNumeroCnj.get().getCodigo() != processoConsulta.getCodigo()) {
				throw new EntidadeJaCadastradaException("Atenção! Já existe um número de CNJ cadstrado com este número");
			}
		}
		
		//Verifica se houve mudança no status processual
		if(processoConsulta.getStatusProcessual().getCodigo() != processoDto.getStatusProcessual().getCodigo()) {
			//Houve mudança de status processual
			Optional<StatusProcessual> statusProcessualMudanca = statusRepository.findById(processoDto.getStatusProcessual().getCodigo());
			if(statusProcessualMudanca.isPresent()) {
				Optional<TipoAndamentoProcessual> tipoAndamento = tipoAndamentoProcessualRepository.findByNomeAndEmpresaCodigo("Mudanca de status processual", 
						processoConsulta.getEmpresa().getCodigo());
				Processo processoHistorico = new Processo();
				processoHistorico.setCodigo(processoConsulta.getCodigo());
				HistoricoProcesso hp = HistoricoProcesso.builder()
						.historico("Mudança de Status Processual de: " + processoConsulta.getStatusProcessual().getDescricao() + " para: " + 
				statusProcessualMudanca.get().getDescricao())
						.dataHistorico(DatasUtil.getDataAtual())
						.dataOcorrencia(DatasUtil.getDataAtual())
						.usuario(processoDto.getUsuario())
						.processo(processoHistorico)
						.tipoAndamento(tipoAndamento.get())
						.build();
				historicoProcessoRepository.save(hp);
			}
		}
		
		//Verifica se houve mudança de decisão
		if(processoDto.getTipoDecisao() != null) {
			String nomeTipoDecisaoAntigo = "";
			if(processoConsulta.getTipoDecisao() == null) {
				nomeTipoDecisaoAntigo = "";
			} else {
				nomeTipoDecisaoAntigo = processoConsulta.getTipoDecisao().getNome();
			}
			if(processoConsulta.getTipoDecisao() == null || processoConsulta.getTipoDecisao().getCodigo() != processoDto.getTipoDecisao().getCodigo()) {
				//Houve mudança de tipo de decisão
				Optional<TipoDecisao> tipoDecisaoMudanca = tipoDecisaoRepository.findById(processoDto.getTipoDecisao().getCodigo());
				if(!tipoDecisaoMudanca.isPresent()) {
					throw new EntidadeNaoEncontradaException("Tipo de Decisão não Encontrada");
				}
				Optional<TipoAndamentoProcessual> tipoAndamento = tipoAndamentoProcessualRepository.findByNomeAndEmpresaCodigo("Mudanca de Tipo de Decisao", 
						processoConsulta.getEmpresa().getCodigo());
				Processo processoHistorico = new Processo();
				processoHistorico.setCodigo(processoConsulta.getCodigo());
				HistoricoProcesso hp = HistoricoProcesso.builder()
						.historico("Mudança de Tipo de Decisão de: " + nomeTipoDecisaoAntigo + " para: " + 
								tipoDecisaoMudanca.get().getNome())
						.dataHistorico(DatasUtil.getDataAtual())
						.dataOcorrencia(DatasUtil.getDataAtual())
						.usuario(processoDto.getUsuario())
						.processo(processoHistorico)
						.tipoAndamento(tipoAndamento.get())
						.build();
				historicoProcessoRepository.save(hp);
			}
		}
		
		BeanUtils.copyProperties(processoDto, processoConsulta,"codigo", "dataCadastro", "snImportante");
		processoConsulta.setDataUltimaMovimentacao(DatasUtil.getDataAtual());
		MotivoResultado motivoResultado = new MotivoResultado();
		if(processoConsulta.getMotivoDto() != null) {
			motivoResultado.setCodigo(processoConsulta.getMotivoDto().getCodigo());
		} else {
			motivoResultado = null;
		}
		
		Fase fase = new Fase();
		if(processoConsulta.getFase() != null) {
			fase.setCodigo(processoConsulta.getFase().getCodigo());
		} else {
			fase = null;
		}
		
		Rito rito = new Rito();
		if(processoConsulta.getRito() != null) {
			rito.setCodigo(processoConsulta.getRito().getCodigo());
		} else {
			rito = null;
		}
		
		Processo processo = Processo.builder()	
				.codigo(processoConsulta.getCodigo())
				.grupoTrabalho(processoConsulta.getGrupoTrabalho())
				.pasta(processoConsulta.getPasta())
				.nrProcesso(processoConsulta.getNrProcesso())
				.nrCnj(this.retiraMascara(processoConsulta.getNrCnj()))
				.nrInstancia(processoConsulta.getNrInstancia())
				.dsComarca(processoConsulta.getDsComarca())
				.tipoAcao(processoConsulta.getTipoAcao())
				.statusProcessual(processoConsulta.getStatusProcessual())
				.areaAtuacao(processoConsulta.getAreaAtuacao())
				.tipoDecisao(processoConsulta.getTipoDecisao())
				.dataDistribuicao(processoConsulta.getDataDistribuicao())
				.dataUltimaMovimentacao(processoConsulta.getDataUltimaMovimentacao())
				.dataUltimaDecisao(processoConsulta.getDataUltimaDecisao())
				.vlProvavel(processoConsulta.getVlProvavel())
				.vlPossivel(processoConsulta.getVlPossivel())
				.vlRemoto(processoConsulta.getVlRemoto())
				.vlCausa(processoConsulta.getVlCausa())
				.dsPedidos(processoConsulta.getDsPedidos())
				.observacao(processoConsulta.getObservacao())
				.usuario(processoConsulta.getUsuario())
				.responsavel(processoConsulta.getResponsavel())
				.statusInterno(processoConsulta.getStatusInterno())
				.dataCadastro(processoConsulta.getDataCadastro())
				.snPush(processoConsulta.getSnPush())
				.snEmail(processoConsulta.getSnEmail())
				.snHistorico(processoConsulta.getSnHistorico())
				.status(processoConsulta.getStatus())
				.empresa(processoConsulta.getEmpresa())
				.snImportante(processoConsulta.getSnImportante())
				.dataSentenca(processoConsulta.getDataSentenca())
				.tipoContingenciaContabil(processoConsulta.getTipoContingenciaContabil())
				.motivoResultado(motivoResultado)
				.fase(fase)
				.rito(rito)
				.uf(processoConsulta.getUf())
				.orgaoColegiado(processoConsulta.getOrgaoColegiado())
				.relator(processoConsulta.getRelator())
				.bancada(processoConsulta.getBancada())
				.build();
		
		Processo processoAlterado = processoRepository.save(processo);
		
		if(processoAlterado != null) {
			//Manutenção em Atividade Short
			List<Atividade> atividades = atividadeRepository.findByProcessoCodigoOrderByDtLimiteDesc(processoAlterado.getCodigo());
			if(atividades != null) {
				List<Usuario> responsaveis = new ArrayList<Usuario>();
				List<Usuario> interessados = new ArrayList<Usuario>();
				for(Atividade ativ : atividades) {
					List<AtividadeUsuario> atividadeUsuarios = atividadeUsuarioRepository.findByAtividadeCodigo(ativ.getCodigo());
					for(AtividadeUsuario au : atividadeUsuarios) {
						if(au.getTipo().equals("R")) {
							responsaveis.add(au.getUsuario());
						} else {
							interessados.add(au.getUsuario());
						}
					}
					AtividadeCadastroDTO dtoAtividade = new AtividadeCadastroDTO();
					dtoAtividade.setAtividade(ativ);
					dtoAtividade.setResponsaveis(responsaveis);
					dtoAtividade.setInteressados(interessados);
					atividadeService.excluirAtividadesShort(ativ);
					List<Atividade> ativs = new ArrayList<Atividade>();
					ativs.add(ativ);
					atividadeService.incluirAtividadeShort(dtoAtividade,ativs);
				}
			}
			
			//Índice do do processo
			Optional<IndiceProcesso> indice = indiceProcessoRepository.findByProcessoCodigo(processoAlterado.getCodigo());
			if(indice.isPresent()) {
				indiceProcessoRepository.deleteById(indice.get().getCodigo());
				this.cadastrarIndiceProcesso(ProcessoDto.build(processoAlterado));
			}
			//Verifica se o status do processo é arquivado
			Optional<StatusProcessual> statusProcessualArquivado = statusRepository.findById(processo.getStatusProcessual().getCodigo());
			if(statusProcessualArquivado.get().getDescricao().equals("Arquivado")) {
				Optional<ProcessoArquivado> processoArquivado = processoArquivadoRepository.findByProcessoCodigo(codigo);
				if(!processoArquivado.isPresent()) {
					ProcessoArquivado pa = new ProcessoArquivado();
					pa.setProcesso(processo);
					pa.setUsuario(processo.getUsuario());
					pa.setDataArquivado(DatasUtil.getDataAtual());
					processoArquivadoRepository.save(pa);
				}
			} else {
				Optional<ProcessoArquivado> processoArquivado = processoArquivadoRepository.findByProcessoCodigo(codigo);
				if(processoArquivado.isPresent()) {
					processoArquivadoRepository.deleteById(processoArquivado.get().getCodigo());
				}
			}
			
		}
		
		ProcessoDto dto = ProcessoDto.build(processoAlterado);
		return dto;
	}
	
	private void incluirPartes(ProcessoDto processoDto) {
		if(processoDto.getPartes() == null) {
			throw new EntidadeNaoEncontradaException("Nenhuma parte foi informada");
		}
		for(PartesDto parteDto : processoDto.getPartes()) {
			Partes parte = new Partes();
			parte.setPessoa(parteDto.getPessoa());
			Processo processo = new Processo();
			processo.setCodigo(processoDto.getCodigo());
			parte.setProcesso(processo);
			parte.setTipoParte(parteDto.getTipo());
			partesRepository.save(parte);
		}
	}
	
	@SuppressWarnings("unused")
	private void mergePartes(ProcessoDto processoDto) {
		List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processoDto.getCodigo());
		for(Partes parte : partes) {
			partesRepository.deleteById(parte.getCodigo());
		}
		
		if(processoDto.getPartes() == null) {
			throw new EntidadeNaoEncontradaException("Parte do processo não eocntrado");
		}
		
		for(PartesDto parteDto : processoDto.getPartes()) {
			Partes parte = new Partes();
			parte.setPessoa(parteDto.getPessoa());
			parte.setProcesso(parteDto.getProcesso());
			parte.setTipoParte(parteDto.getTipo());
			partesRepository.save(parte);
		}
	}
	
	public void excluirParte(long codigo) {
		Optional<Partes> parte = partesRepository.findById(codigo);
		if(!parte.isPresent()) {
			throw new EntidadeNaoEncontradaException("Parte do processo não eocntrado");
		}
		
		partesRepository.deleteById(codigo);
	}
	
	public ProcessoDto alterarProcessoEstrategico(long codigo, ProcessoDto processoDto) {
		ProcessoDto processoConsulta = this.consultarProcesso(codigo,processoDto.getUsuario().getCodigo());
		BeanUtils.copyProperties(processoDto, processoConsulta,"codigo", "dataCadastro",
															   "grupoTrabalho","pasta","nrProcesso","nrCnj","nrInstancia","dsComarca",
															   "tipoAcao","statusProcessual","areaAtuacao","tipoDecisao","dataDistribuicao",
															   "dataUltimaDecisao","vlProvavel","vlPossivel","vlRemoto","vlCausa","dsPedidos",
															   "observacao","usuario","responsavel","statusInterno","snPush","snHistorico","snImportante",
															   "snEmail","status","empresa","dataUltimaMovimentacao","dataSentenca","processoApenso");
		Processo processo = Processo.builder()
				.codigo(processoConsulta.getCodigo())
				.grupoTrabalho(processoConsulta.getGrupoTrabalho())
				.pasta(processoConsulta.getPasta())
				.nrProcesso(processoConsulta.getNrProcesso())
				.nrCnj(processoConsulta.getNrCnj())
				.nrInstancia(processoConsulta.getNrInstancia())
				.dsComarca(processoConsulta.getDsComarca())
				.tipoAcao(processoConsulta.getTipoAcao())
				.statusProcessual(processoConsulta.getStatusProcessual())
				.areaAtuacao(processoConsulta.getAreaAtuacao())
				.tipoDecisao(processoConsulta.getTipoDecisao())
				.dataDistribuicao(processoConsulta.getDataDistribuicao())
				.dataUltimaMovimentacao(processoConsulta.getDataUltimaMovimentacao())
				.dataUltimaDecisao(processoConsulta.getDataUltimaDecisao())
				.vlProvavel(processoConsulta.getVlProvavel())
				.vlPossivel(processoConsulta.getVlPossivel())
				.vlRemoto(processoConsulta.getVlRemoto())
				.vlCausa(processoConsulta.getVlCausa())
				.dsPedidos(processoConsulta.getDsPedidos())
				.observacao(processoConsulta.getObservacao())
				.usuario(processoConsulta.getUsuario())
				.responsavel(processoConsulta.getResponsavel())
				.statusInterno(processoConsulta.getStatusInterno())
				.dataCadastro(processoConsulta.getDataCadastro())
				.snPush(processoConsulta.getSnPush())
				.snEmail(processoConsulta.getSnEmail())
				.snHistorico(processoConsulta.getSnHistorico())
				.status(processoConsulta.getStatus())
				.empresa(processoConsulta.getEmpresa())
				.snImportante(processoConsulta.getSnImportante())
				.estrategico(processoConsulta.getEstrategico())
				.dataSentenca(processoConsulta.getDataSentenca())
				.tipoContingenciaContabil(processoConsulta.getTipoContingenciaContabil())
				.build();
		
		ProcessoDto processoDtoAlterado = ProcessoDto.build(processoRepository.save(processo));
		if(processoDtoAlterado != null) {
			//Manutenção em Atividade Short
			List<Atividade> atividades = atividadeRepository.findByProcessoCodigoOrderByDtLimiteDesc(processoDtoAlterado.getCodigo());
			if(atividades != null) {
				List<Usuario> responsaveis = new ArrayList<Usuario>();
				List<Usuario> interessados = new ArrayList<Usuario>();
				for(Atividade ativ : atividades) {
					List<AtividadeUsuario> atividadeUsuarios = atividadeUsuarioRepository.findByAtividadeCodigo(ativ.getCodigo());
					for(AtividadeUsuario au : atividadeUsuarios) {
						if(au.getTipo().equals("R")) {
							responsaveis.add(au.getUsuario());
						} else {
							interessados.add(au.getUsuario());
						}
					}
					AtividadeCadastroDTO dtoAtividade = new AtividadeCadastroDTO();
					dtoAtividade.setAtividade(ativ);
					dtoAtividade.setResponsaveis(responsaveis);
					dtoAtividade.setInteressados(interessados);
					atividadeService.excluirAtividadesShort(ativ);
					List<Atividade> ativs = new ArrayList<Atividade>();
					ativs.add(ativ);
					atividadeService.incluirAtividadeShort(dtoAtividade,ativs);
				}
			}
		}
		
		return processoDtoAlterado;
	}
	
	public ProcessoDto alterarImportanteParaEmpresa(long codigo, ProcessoDto processoDto) {
		ProcessoDto processoConsulta = this.consultarProcesso(codigo,processoDto.getUsuario().getCodigo());
		BeanUtils.copyProperties(processoDto, processoConsulta,"codigo", "dataCadastro",
															   "grupoTrabalho","pasta","nrProcesso","nrCnj","nrInstancia","dsComarca",
															   "tipoAcao","statusProcessual","areaAtuacao","tipoDecisao","dataDistribuicao",
															   "dataUltimaDecisao","vlProvavel","vlPossivel","vlRemoto","vlCausa","dsPedidos",
															   "observacao","usuario","responsavel","statusInterno","snPush","snHistorico",
															   "snEmail","status","empresa","dataUltimaMovimentacao","dataSentenca","processoApenso");
		Processo processo = Processo.builder()
				.codigo(processoConsulta.getCodigo())
				.grupoTrabalho(processoConsulta.getGrupoTrabalho())
				.pasta(processoConsulta.getPasta())
				.nrProcesso(processoConsulta.getNrProcesso())
				.nrCnj(processoConsulta.getNrCnj())
				.nrInstancia(processoConsulta.getNrInstancia())
				.dsComarca(processoConsulta.getDsComarca())
				.tipoAcao(processoConsulta.getTipoAcao())
				.statusProcessual(processoConsulta.getStatusProcessual())
				.areaAtuacao(processoConsulta.getAreaAtuacao())
				.tipoDecisao(processoConsulta.getTipoDecisao())
				.dataDistribuicao(processoConsulta.getDataDistribuicao())
				.dataUltimaMovimentacao(processoConsulta.getDataUltimaMovimentacao())
				.dataUltimaDecisao(processoConsulta.getDataUltimaDecisao())
				.vlProvavel(processoConsulta.getVlProvavel())
				.vlPossivel(processoConsulta.getVlPossivel())
				.vlRemoto(processoConsulta.getVlRemoto())
				.vlCausa(processoConsulta.getVlCausa())
				.dsPedidos(processoConsulta.getDsPedidos())
				.observacao(processoConsulta.getObservacao())
				.usuario(processoConsulta.getUsuario())
				.responsavel(processoConsulta.getResponsavel())
				.statusInterno(processoConsulta.getStatusInterno())
				.dataCadastro(processoConsulta.getDataCadastro())
				.snPush(processoConsulta.getSnPush())
				.snEmail(processoConsulta.getSnEmail())
				.snHistorico(processoConsulta.getSnHistorico())
				.status(processoConsulta.getStatus())
				.empresa(processoConsulta.getEmpresa())
				.snImportante(processoConsulta.getSnImportante())
				.dataSentenca(processoConsulta.getDataSentenca())
				.tipoContingenciaContabil(processoConsulta.getTipoContingenciaContabil())
				.build();
		
		ProcessoDto processoDtoAlterado = ProcessoDto.build(processoRepository.save(processo));
		if(processoDtoAlterado != null) {
			//Manutenção em Atividade Short
			List<Atividade> atividades = atividadeRepository.findByProcessoCodigoOrderByDtLimiteDesc(processoDtoAlterado.getCodigo());
			if(atividades != null) {
				List<Usuario> responsaveis = new ArrayList<Usuario>();
				List<Usuario> interessados = new ArrayList<Usuario>();
				for(Atividade ativ : atividades) {
					List<AtividadeUsuario> atividadeUsuarios = atividadeUsuarioRepository.findByAtividadeCodigo(ativ.getCodigo());
					for(AtividadeUsuario au : atividadeUsuarios) {
						if(au.getTipo().equals("R")) {
							responsaveis.add(au.getUsuario());
						} else {
							interessados.add(au.getUsuario());
						}
					}
					AtividadeCadastroDTO dtoAtividade = new AtividadeCadastroDTO();
					dtoAtividade.setAtividade(ativ);
					dtoAtividade.setResponsaveis(responsaveis);
					dtoAtividade.setInteressados(interessados);
					atividadeService.excluirAtividadesShort(ativ);
					List<Atividade> ativs = new ArrayList<Atividade>();
					ativs.add(ativ);
					atividadeService.incluirAtividadeShort(dtoAtividade,ativs);
				}
			}
		}
		
		return processoDtoAlterado;
	}
	
	private ProcessoExcluido incluirProcessoExcluido(Processo processo) {
		ProcessoExcluido pe = new ProcessoExcluido();
		pe.setProcesso(processo);
		pe.setUsuario(processo.getUsuario());
		pe.setDataExcluido(DatasUtil.getDataAtual());
		pe.setStatus(this.PROCESSO_EXCLUIDO);
		ProcessoExcluido processoExcluidoSalvo = processoExcluidoRepository.save(pe);
		if(processoExcluidoSalvo != null) {
			log.info("O processo foi removido para lixeira, avisa aos administradores do sistema por meio de notificação");
			log.info("Selecionando os administradores do sistema");
			List<Usuario> administradores = usuarioRepository.findByPerfilCodigoAndEmpresaCodigo(PERFIL_ADMINISTRADOR,processo.getEmpresa().getCodigo());
			if(administradores != null && administradores.size() > 0) {
				for (Usuario usu : administradores) {
					CentralAtividade central = new CentralAtividade();
					central.setAtividade(null);
					central.setStatus(1); // Não lida
					central.setDescricao("Processo de número: " + processo.getNrCnj() + " foi removido para a lixeira por " + processo.getUsuario().getNome());
					central.setDataRegistro(DatasUtil.getDataAtual());
					central.setUsuario(usu);
					CentralAtividade centralSalvo = centralAtividadeRepository.save(central);
					if (centralSalvo != null) {
						log.info("Informação de processo removido para lixeira cadastrada na central");
					}
				}
			}
		}

		return processoExcluidoSalvo;
	}
	
	public ProcessoDto excluirProcesso(long codigo, ProcessoDto processoDto, long usuario) {
		ProcessoDto processoConsulta = this.consultarProcesso(codigo,processoDto.getUsuario().getCodigo());
		BeanUtils.copyProperties(processoDto, processoConsulta,"codigo", "dataCadastro", "snImportante",
														       "grupoTrabalho","pasta","nrProcesso","nrCnj","nrInstancia","dsComarca",
														       "tipoAcao","statusProcessual","areaAtuacao","tipoDecisao","dataDistribuicao",
														       "dataUltimaDecisao","vlProvavel","vlPossivel","vlRemoto","vlCausa","dsPedidos",
														       "observacao","usuario","responsavel","statusInterno","snPush","snHistorico",
														       "snEmail","empresa","dataUltimaMovimentacao","dataSentenca","processoApenso");
		
		Processo processoExcluido = Processo.builder()
				.codigo(processoConsulta.getCodigo())
				.grupoTrabalho(processoConsulta.getGrupoTrabalho())
				.pasta(processoConsulta.getPasta())
				.nrProcesso(processoConsulta.getNrProcesso())
				.nrCnj(processoConsulta.getNrCnj())
				.nrInstancia(processoConsulta.getNrInstancia())
				.dsComarca(processoConsulta.getDsComarca())
				.tipoAcao(processoConsulta.getTipoAcao())
				.statusProcessual(processoConsulta.getStatusProcessual())
				.areaAtuacao(processoConsulta.getAreaAtuacao())
				.tipoDecisao(processoConsulta.getTipoDecisao())
				.dataDistribuicao(processoConsulta.getDataDistribuicao())
				.dataUltimaMovimentacao(processoConsulta.getDataUltimaMovimentacao())
				.dataUltimaDecisao(processoConsulta.getDataUltimaDecisao())
				.vlProvavel(processoConsulta.getVlProvavel())
				.vlPossivel(processoConsulta.getVlPossivel())
				.vlRemoto(processoConsulta.getVlRemoto())
				.vlCausa(processoConsulta.getVlCausa())
				.dsPedidos(processoConsulta.getDsPedidos())
				.observacao(processoConsulta.getObservacao())
				.usuario(processoConsulta.getUsuario())
				.responsavel(processoConsulta.getResponsavel())
				.statusInterno(processoConsulta.getStatusInterno())
				.dataCadastro(processoConsulta.getDataCadastro())
				.snPush(processoConsulta.getSnPush())
				.snEmail(processoConsulta.getSnEmail())
				.snHistorico(processoConsulta.getSnHistorico())
				.status(processoConsulta.getStatus())
				.empresa(processoConsulta.getEmpresa())
				.snImportante(processoConsulta.getSnImportante())
				.dataSentenca(processoConsulta.getDataSentenca())
				.tipoContingenciaContabil(processoConsulta.getTipoContingenciaContabil())
				.build();
		
				
		Processo processoSalvo = processoRepository.save(processoExcluido);
		
		
		if(processoSalvo != null) {
			//Histórico
			HistoricoAlteracaoProcesso historico = new HistoricoAlteracaoProcesso();
			historico.setProcesso(processoSalvo);
			Usuario usu = new Usuario();
			usu.setCodigo(usuario);
			historico.setUsuarioAlterou(usu);
			historico.setDataAlteracao(DatasUtil.getDataAtual());
			historico.setDsAlteracao("Processo exclído");
			historico.setTipoAlteracao("E");
			historicoReporitory.save(historico);
			//Incluir na tabela de processos excluidos
			this.incluirProcessoExcluido(processoSalvo);
		}
		
		return ProcessoDto.build(processoSalvo);
	}
	
	public ProcessoDto ativarProcesso(long codigo, ProcessoDto processoDto, long usuario) {
		ProcessoDto processoConsulta = this.consultarProcesso(codigo,processoDto.getUsuario().getCodigo());
		BeanUtils.copyProperties(processoDto, processoConsulta,"codigo", "dataCadastro", "snImportante",
															   "grupoTrabalho","pasta","nrProcesso","nrCnj","nrInstancia","dsComarca",
															   "tipoAcao","statusProcessual","areaAtuacao","tipoDecisao","dataDistribuicao",
															   "dataUltimaDecisao","vlProvavel","vlPossivel","vlRemoto","vlCausa","dsPedidos",
															   "observacao","usuario","responsavel","statusInterno","snPush","snHistorico",
															   "snEmail","empresa","dataUltimaMovimentacao","dataSentenca","processoApenso");
		
		Processo processo = Processo.builder()
				.codigo(processoConsulta.getCodigo())
				.grupoTrabalho(processoConsulta.getGrupoTrabalho())
				.pasta(processoConsulta.getPasta())
				.nrProcesso(processoConsulta.getNrProcesso())
				.nrCnj(processoConsulta.getNrCnj())
				.nrInstancia(processoConsulta.getNrInstancia())
				.dsComarca(processoConsulta.getDsComarca())
				.tipoAcao(processoConsulta.getTipoAcao())
				.statusProcessual(processoConsulta.getStatusProcessual())
				.areaAtuacao(processoConsulta.getAreaAtuacao())
				.tipoDecisao(processoConsulta.getTipoDecisao())
				.dataDistribuicao(processoConsulta.getDataDistribuicao())
				.dataUltimaMovimentacao(processoConsulta.getDataUltimaMovimentacao())
				.dataUltimaDecisao(processoConsulta.getDataUltimaDecisao())
				.vlProvavel(processoConsulta.getVlProvavel())
				.vlPossivel(processoConsulta.getVlPossivel())
				.vlRemoto(processoConsulta.getVlRemoto())
				.vlCausa(processoConsulta.getVlCausa())
				.dsPedidos(processoConsulta.getDsPedidos())
				.observacao(processoConsulta.getObservacao())
				.usuario(processoConsulta.getUsuario())
				.responsavel(processoConsulta.getResponsavel())
				.statusInterno(processoConsulta.getStatusInterno())
				.dataCadastro(processoConsulta.getDataCadastro())
				.snPush(processoConsulta.getSnPush())
				.snEmail(processoConsulta.getSnEmail())
				.snHistorico(processoConsulta.getSnHistorico())
				.status(processoConsulta.getStatus())
				.empresa(processoConsulta.getEmpresa())
				.snImportante(processoConsulta.getSnImportante())
				.dataSentenca(processoConsulta.getDataSentenca())
				.tipoContingenciaContabil(processoConsulta.getTipoContingenciaContabil())
				.build();
		
		Processo processoSalvo = processoRepository.save(processo);
		
		if(processoSalvo != null) {
			//Histórico
			HistoricoAlteracaoProcesso historico = new HistoricoAlteracaoProcesso();
			historico.setProcesso(processoSalvo);
			Usuario usu = new Usuario();
			usu.setCodigo(usuario);
			historico.setUsuarioAlterou(usu);
			historico.setDataAlteracao(DatasUtil.getDataAtual());
			historico.setDsAlteracao("Processo reativado");
			historico.setTipoAlteracao("R");
			historicoReporitory.save(historico);
			
			//Retirar da tabela de processos excluidos
			Optional<ProcessoExcluido> processoExcluidoTabela = processoExcluidoRepository.findByProcessoCodigo(codigo);
			if(!processoExcluidoTabela.isPresent()) {
				throw new EntidadeNaoEncontradaException("Processo excluido não encontrado");
			}
			
			processoExcluidoRepository.deleteById(processoExcluidoTabela.get().getCodigo());
			
		}
		
		return ProcessoDto.build(processoSalvo);
	}
	
	public void cadastrarIndiceProcesso(ProcessoDto processo) {
		List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo.getCodigo());
		String partesTexto = "";
		int contador = 0;
		int tamanhoLista = partes.size();
		for(Partes a : partes) {
			Optional<Pessoa> pessoa = pessoaRepository.findById(a.getPessoa().getCodigo());
			if(contador == 0) {
				partesTexto = pessoa.get().getNome();
			} else {
				if(contador == tamanhoLista) {
					partesTexto = partesTexto + pessoa.get().getNome();;
				} else {
					partesTexto = partesTexto + pessoa.get().getNome() + " ";
				}
			}
			
			contador++;
		}
		
		IndiceProcesso indice = new IndiceProcesso();
		Processo processoConsulta = processoRepository.findById(processo.getCodigo()).get();
		indice.setProcesso(processoConsulta);
		indice.setEmpresa(processoConsulta.getEmpresa());
		indice.setIndice(processoConsulta.getPasta() + ' ' + processoConsulta.getNrCnj() + ' ' + processoConsulta.getNrProcesso() + ' ' + partesTexto);		
		indiceProcessoRepository.save(indice);
	}
	
	public void excluirIndiceProcesso(long processo) {
		Optional<IndiceProcesso> indice = indiceProcessoRepository.findByProcessoCodigo(processo);
		if(indice.isPresent()) {
			indiceProcessoRepository.deleteById(indice.get().getCodigo());
		}
	}
	
	public String retornaAutor(long processo) {
		List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo);
		String autor = "";
		if(partes != null && partes.size() > 0) {
			int contador = 0;
			int tamanhoLista = partes.size();
			for(Partes autores: partes) {
				if(autores.getTipoParte().equals("A")) {
					if(contador == 0) {
						autor = autores.getPessoa().getNome();
					} else {
						if(contador == tamanhoLista) {
							autor = autor + autores.getPessoa().getNome();
						} else {
							autor = autor + autores.getPessoa().getNome() + ",";
						}
					}
				}
				
				contador++;
			}
		}
		
		return autor;
	}
	
	public String retornaReu(long processo) {
		List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo);
		String reu = "";
		if(partes != null && partes.size() > 0) {
			int contador = 0;
			int tamanhoLista = partes.size();
			for(Partes reus: partes) {
				if(reus.getTipoParte().equals("R")) {
					if(contador == 0) {
						reu = reus.getPessoa().getNome();
					} else {
						if(contador == tamanhoLista) {
							reu = reu + reus.getPessoa().getNome();
						} else {
							reu = reu + reus.getPessoa().getNome() + ",";
						}
					}
				}
				
				contador++;
			}
		}
		
		return reu;
	}
	
	public String retornaAutorVersusReu(long processo) {
		String partes = this.retornaAutor(processo) + " x " + this.retornaReu(processo);
		return partes;
	}
	
	public String retornaPartes(long processo) {
		List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo);
		String strPartes = "";
		if(partes != null && partes.size() > 0) {
			for(Partes parte: partes) {
				if(parte.getTipoParte().equals("A")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Autor ), ";
				} else if(parte.getTipoParte().equals("R")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Réu ), ";
				} else if(parte.getTipoParte().equals("AD")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Advogado ), ";
				} else if(parte.getTipoParte().equals("EM")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Embargante ), ";
				} else if(parte.getTipoParte().equals("RE")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Reclamado ), ";
				} else if(parte.getTipoParte().equals("TE")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Testemunha ), ";
				} else if(parte.getTipoParte().equals("EN")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Envolvido ), ";
				} else if(parte.getTipoParte().equals("IN")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Inventariante ), ";
				} else if(parte.getTipoParte().equals("RC")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( REclamante ), ";
				} else if(parte.getTipoParte().equals("NO")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Notificado ), ";
				} else if(parte.getTipoParte().equals("NC")) {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Notificante ), ";
				} else {
					strPartes = strPartes + parte.getPessoa().getNome() + "( Outros ), ";
				}
			}
		}
		
		return strPartes;
	}
	
	public List<ProcessoDto> consultarProcessoPorpessoa(long pessoa, long usuario) {
		List<Processo> processos = processoRepository.consultarProcessosPorPessoa(pessoa);
		List<ProcessoDto> dtos = new ArrayList<ProcessoDto>();
		for(Processo p : processos) {
			ProcessoDto pro = new ProcessoDto();
			pro.setCodigo(p.getCodigo());
			pro.setNrCnj(p.getNrCnj());
			pro.setNrProcesso(p.getNrProcesso());
			pro.setPasta(p.getPasta());
			pro.setGrupoTrabalho(p.getGrupoTrabalho());
			pro.setDataUltimaMovimentacao(DatasUtil.formatarDataTela(p.getDataUltimaMovimentacao()));
			pro.setStatusProcessual(p.getStatusProcessual());
			List<Partes> partesEtitys = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(pro.getCodigo());
			List<PartesDto> partes = new ArrayList<PartesDto>();
			if(partesEtitys != null && partesEtitys.size() > 0) {
				for (Partes parte : partesEtitys) {
					partes.add(PartesDto.build(parte));
				}
				pro.setMontaPartes(this.montaPartesConsultaPorPessoa(partes));
			}
			dtos.add(pro);
		}

		return dtos;
	}

	private String montaPartesConsultaPorPessoa(List<PartesDto> partes) {
		String nomePartes = "";
		if(partes != null && partes.size() > 0) {
			for(PartesDto parte : partes) {
				nomePartes = nomePartes + parte.getPessoa().getNome() + "; ";
			}
		}

		return nomePartes;
	}
		
	private String consultarImportancia(long usuario, long processo) {
		String retorno = "";
		ProcessoImportancia importancia = processoImportanciaService.consultarProcessoImportancia(usuario, processo);
		if(importancia != null) {
			retorno = "p-button-rounded p-button-warning";
		} else {
			retorno = "p-button-rounded p-button-secondary";
		}
		
		return retorno;
	}
	
	public List<Partes> listarPartesPorTipo(long processo, String tipo) {
		return partesRepository.findByProcessoCodigoAndTipoParteOrderByPessoaNomeAsc(processo, tipo);
	}
	
	public List<PartesDto> listarPartesPorProcesso(long processo) {
		List<Partes> partesEtitys = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo);
		List<PartesDto> partes = new ArrayList<PartesDto>();
		if(partesEtitys != null && partesEtitys.size() > 0) {
			for(Partes parte : partesEtitys) {
				partes.add(PartesDto.build(parte));
			}
		}
		
		return partes;
	}
	
	public List<Lancamento> consultarLancamentosPorProcesso(long processo) {
		List<Lancamento> lista = lancamentoRepository.findByProcessoCodigoOrderByDtVencimento(processo);
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
				l.setVlLancamento(lanc.getVlLancamento());
				l.setVlPago(lanc.getVlPago());
				if(lanc.getSituacao().equals("P")) {
					l.setSituacao("Pago");
				} else if(lanc.getSituacao().equals("A")) {
					l.setSituacao("Ativo");
				} else {
					l.setSituacao("Cancelado");
				}
				if(lanc.getTipo().getTipo().equals("D")) {
					l.setCategoria("Despesa");
				} else {
					l.setCategoria("Receita");
				}
				lancamentos.add(l);
			}
		}
		
		return lancamentos;
	}
	
	public List<ProcessoExcluidoDto> listarProcessosExcluidos(long empresa) {
		List<ProcessoExcluido> processos = processoExcluidoRepository.findByProcessoEmpresaCodigoOrderByDataExcluidoDesc(empresa);
		List<ProcessoExcluidoDto> dtos = new ArrayList<>();
		if(processos != null && processos.size() > 0) {
			for(ProcessoExcluido pro : processos) {
				ProcessoExcluidoDto dto = new ProcessoExcluidoDto();
				ProcessoDto proDto = new ProcessoDto();
				proDto.setCodigo(pro.getProcesso().getCodigo());
				proDto.setNrCnj(ProcessoDto.mascaraProcessoCnj(pro.getProcesso().getNrCnj()));
				proDto.setNrProcesso(pro.getProcesso().getNrProcesso());
				proDto.setGrupoTrabalho(pro.getProcesso().getGrupoTrabalho());
				proDto.setPasta(pro.getProcesso().getPasta());
				dto.setProcesso(proDto);
				dto.setDataExcluido(DatasUtil.formatarDataTela(pro.getDataExcluido()));
				dto.setUsuario(pro.getUsuario());
				dto.setStatus(pro.getStatus());
				dtos.add(dto);
			}
		}
		return dtos;
	}
	
	public ProcessoDto consultarProcessoAgravoApenso(long codigo) {
		Optional<Processo> processo = processoRepository.findById(codigo);
		if(!processo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		List<Partes> partesEntitys = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo.get().getCodigo());
		List<PartesDto> partes = new ArrayList<PartesDto>();
		if(partesEntitys == null) {
			throw new EntidadeNaoEncontradaException("Partes não encontradas");
		}
		
		for(Partes parte : partesEntitys) {
			partes.add(PartesDto.build(parte));
		}
		
		ProcessoDto dto = ProcessoDto.buildAgravoApenso(processo.get(), partes);
		
		return dto;
	}
	
	public List<ProcessoDto> consultarProcessoShort(String indice, long empresa, long usuario) {
		String formato = indice.replace("-", ".");
		formato = formato.replace(".", "");
		List<Processo> processos = processoRepository.consultaProcessoPorIndice(formato,empresa,usuario);
		List<ProcessoDto> dtos = new ArrayList<ProcessoDto>();
		if(processos != null && processos.size() > 0) {
			for(Processo processo : processos) {
				 List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo.getCodigo());
				 List<PartesDto> partesDtos = new ArrayList<PartesDto>(); 
				 for(Partes parte :partes) { 
					PartesDto parteDto = PartesDto.build(parte);
					partesDtos.add(parteDto); 
				 }
				ProcessoImportancia pi = processoImportanciaService.consultarProcessoImportancia(usuario, processo.getCodigo());
				Optional<AgravoApenso> agravoApenso = agravoApensoRepository.findByProcessoCodigo(processo.getCodigo());
				dtos.add(ProcessoDto.buildShort(processo, partesDtos, agravoApenso.isPresent() ? "S" : "N", 
						pi != null ? "S" : "N"));
			}
		}
		
		return dtos;
	}
	
	public List<Atividade> listarAtividadesPorProcesso(long processo) {
		List<Atividade> atividadesEntitys = atividadeRepository.findByProcessoCodigoOrderByDtLimiteDesc(processo);
		List<Atividade> atividades = new ArrayList<Atividade>();
		if(atividadesEntitys != null && atividadesEntitys.size() > 0) {
			for(Atividade atividade : atividadesEntitys) {
				Atividade ativ = new Atividade();
				ativ.setCodigo(atividade.getCodigo());
				ativ.setTitulo(atividade.getTitulo());
				if(atividade.getTipo().equals("T")) {
					ativ.setDtLimite(DatasUtil.formatarDataTela(atividade.getDtLimite()));
				} else {
					ativ.setDtLimite(DatasUtil.formatarDataHoraTela(atividade.getDtLimite()));
				}
				if(atividade.getDtFatal() != null) {
					ativ.setDtFatal(DatasUtil.formatarDataTela(atividade.getDtFatal()));
				}
				ativ.setSubGrupo(atividade.getSubGrupo());
				ativ.setStatus(atividade.getStatus());
				ativ.setResponsaveis(this.listaResponsvaeisAtividade(atividade.getCodigo()));
				atividades.add(ativ);
			}
		}
		
		return atividades;
	}

	public void excluirProcessoDefinitivamente(long processo) {
		//Primeiro exclui os andamentos de processo
		log.info("Excluindo andamentos processuais");
		List<HistoricoProcesso> historico = historicoProcessoRepository.findByProcessoCodigoOrderByCodigoDesc(processo);
		if(historico != null && historico.size() > 0) {
			for(HistoricoProcesso hist : historico) {
				historicoProcessoRepository.deleteById(hist.getCodigo());
			}
		}

		//Histórico de arquivamento do processo
		log.info("Excluíndo histórico de arquivamento");
		Optional<ProcessoArquivado> processoArquivado = processoArquivadoRepository.findByProcessoCodigo(processo);
		if(processoArquivado.isPresent()) {
			processoArquivadoRepository.deleteById(processoArquivado.get().getCodigo());
		}

		//Pagamentos dos processo
		log.info("Excluíndo pagamentos de processo");
		List<PagamentoProcesso> pagamentos = pagamentoProcessoRepository.findByProcessoCodigoOrderByDtPagamentoDesc(processo);
		if(pagamentos != null && pagamentos.size() > 0) {
			for(PagamentoProcesso pp : pagamentos) {
				pagamentoProcessoRepository.deleteById(pp.getCodigo());
			}
		}

		//Custas do processo
		log.info("Excluíndo custas do processo");
		List<CustasProcesso> custas = custasProcessoRepository.findByProcessoCodigoOrderByCustasNomeAsc(processo);
		if(custas != null && custas.size() > 0) {
			for(CustasProcesso cust : custas) {
				custasProcessoRepository.deleteById(cust.getCodigo());
			}
		}

		//Garantias do processo
		log.info("Excluíndo as garantias do processo");
		List<GarantiaProcesso> garantias = garantiasProcessoRepository.findByProcessoCodigoOrderByGarantiaNomeAsc(processo);
		if(garantias != null && garantias.size() > 0) {
			for(GarantiaProcesso gara : garantias) {
				garantiasProcessoRepository.deleteById(gara.getCodigo());
			}
		}

		//Apaga os fases processuais associadas a tarefas
		log.info("Excluíndo os históricos de fases processuais");
		List<HistoricoFaseProcessual> historicoFase = historicoFaseProcesualRepository.findByProcessoCodigo(processo);
		if(historicoFase != null && historicoFase.size() > 0) {
			for(HistoricoFaseProcessual hfp : historicoFase) {
				historicoFaseProcesualRepository.deleteById(hfp.getCodigo());
			}
		}

		//Apaga as partes do processo
		log.info("Excluíndo as partes do processo");
		List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo);
		for(Partes parte : partes) {
			partesRepository.deleteById(parte.getCodigo());
		}

		//Apaga os pedidos do processo
		log.info("Excluíndo os pedidos do processo");
		List<PedidoProcesso> pedidos = pedidoProcessoRepository.findByProcessoCodigo(processo);
		if(pedidos != null && pedidos.size() > 0) {
			for(PedidoProcesso pp : pedidos) {
				pedidoProcessoRepository.deleteById(pp.getCodigo());
			}
		}

		//Apaga processo de meus favoritos
		log.info("Excluíndo o processo dos meus favoritos");
		List<ProcessoImportancia> processoImportancia = processoImportanciaRepository.findByProcessoCodigo(processo);
		if(processoImportancia != null && processoImportancia.size() > 0) {
			for(ProcessoImportancia pi : processoImportancia) {
				processoImportanciaRepository.deleteById(pi.getCodigo());
			}
		}

		//Apaga os arquivos do processo
		log.info("Excluíndo os arquivos de processo");
		List<ArquivoProcesso> arquivosProcesso = arquivoProcessoRepository.findByProcessoCodigoOrderByNomeAsc(processo);
		if(arquivosProcesso != null && arquivosProcesso.size() > 0) {
			for(ArquivoProcesso arq : arquivosProcesso) {
				arquivoProcessoRepository.deleteById(arq.getCodigo());
			}
		}

		//Apaga os lançamentos financeiros
		log.info("Excluíndo os lançamentos e transações financeiras");
		List<Lancamento> lancamentos = lancamentoRepository.findByProcessoCodigoOrderByDtVencimento(processo);
		if(lancamentos != null && lancamentos.size() > 0) {
			for(Lancamento lanc : lancamentos) {
				//Buscando as transações financeiras
				List<TransacaoFinanceira> transacoes = transacaoFinanceiraRepoository.findByLancamentoCodigoOrderByCodigo(lanc.getCodigo());
				if(transacoes != null && transacoes.size() > 0) {
					for(TransacaoFinanceira trans : transacoes) {
						transacaoFinanceiraRepoository.deleteById(trans.getCodigo());
					}
				}
				//Apagamendo o lançamento
				lancamentoRepository.deleteById(lanc.getCodigo());
			}
		}

		//Apaga os Agravos e Apensos
		log.info("Excluído os agravos e apensos");
		List<AgravoApenso> agravosApensos = agravoApensoRepository.findByProcessoPrincipalCodigo(processo);
		if(agravosApensos != null && agravosApensos.size() > 0) {
			for(AgravoApenso agro : agravosApensos) {
				agravoApensoRepository.deleteById(agro.getCodigo());
			}
		}

		//Apaga Objetos de Ação
		log.info("Excluíndo os objetos de ação");
		List<ObjetoAcaoProcesso> objetos = objetoAcaoProcessoRepository.findByProcessoCodigoOrderByObjetoNomeAsc(processo);
		if(objetos != null && objetos.size() > 0) {
			for(ObjetoAcaoProcesso obj : objetos) {
				objetoAcaoProcessoRepository.deleteById(obj.getCodigo());
			}
		}

		//Se este processo estiver na lixeira, apaga
		Optional<ProcessoExcluido> processoExcluido = processoExcluidoRepository.findByProcessoCodigo(processo);
		if(processoExcluido.isPresent()) {
			log.info("Excluíndo processo excluído");
			processoExcluidoRepository.deleteById(processoExcluido.get().getCodigo());
		}

		//Atividades
		List<Atividade> atividades = atividadeRepository.findByProcessoCodigoOrderByDtLimiteDesc(processo);
		List<AtividadeShort> shorts = atividadeShortRepository.findByCodigoProcesso(processo);
		//Excluindo as atividades shorts
		log.info("Excluíndo atividades shorts");
		if(shorts != null && shorts.size() > 0) {
			for(AtividadeShort as : shorts) {
				atividadeShortRepository.deleteById(as.getCodigo());
			}
		}
		if(atividades != null && atividades.size() > 0) {
			for(Atividade ativ : atividades) {
				//Excluir o histórico de atividades
				log.info("Excluíndo o histórico das atividades");
				List<HistoricoAtividade> historicoAtividade = historicoAtividadeRepository.findByAtividadeCodigoOrderByCodigoDesc(ativ.getCodigo());
				if(historicoAtividade != null && historicoAtividade.size() > 0) {
					for(HistoricoAtividade ha : historicoAtividade) {
						historicoAtividadeRepository.deleteById(ha.getCodigo());
					}
				}
				//Excluir os reponsáveis e interessados da atividade
				log.info("Excluíndo os responsáveis e interessados pela atividade");
				List<AtividadeUsuario> atividadesUsuario = atividadeUsuarioRepository.findByAtividadeCodigo(ativ.getCodigo());
				for(AtividadeUsuario au : atividadesUsuario) {
					atividadeUsuarioRepository.deleteById(au.getCodigo());
				}
				//Excluindo a Agenda
				log.info("Excluíndo agenda");
				List<Agenda> agenda = agendaRepository.findByCodigoAtividade(ativ.getCodigo());
				for(Agenda age : agenda) {
					agendaRepository.deleteById(age.getCodigo());
				}
				//Excluindo a Central de Atividades
				log.info("Excluíndo central de atividades");
				List<CentralAtividade> central = centralAtividadeRepository.findByAtividadeCodigo(ativ.getCodigo());
				if(central != null && central.size() > 0) {
					for(CentralAtividade cent : central) {
						centralAtividadeRepository.deleteById(cent.getCodigo());
					}
				}
				//Excluindo os arquivos de atividade
				log.info("Excluíndos os arquivos das atividades");
				List<ArquivoAtividade> arquivosAtividade = arquivoAtividadeRepository.findByAtividadeCodigoOrderByArquivo(ativ.getCodigo());
				if(arquivosAtividade != null && arquivosAtividade.size() > 0) {
					for(ArquivoAtividade arq : arquivosAtividade) {
						arquivoAtividadeRepository.deleteById(arq.getCodigo());
					}
				}
				//Excluindo as atividades
				log.info("Excluíndo as atividades");
				atividadeRepository.deleteById(ativ.getCodigo());
			}
		}
		//Excluir os históricos de alteração de processo
		log.info("Excluíndo o histórico de alterações de processo");
		List<HistoricoAlteracaoProcesso> historicoAlteracaoProcessos = historicoReporitory.findByProcessoCodigo(processo);
		if(historicoAlteracaoProcessos != null && historicoAlteracaoProcessos.size() > 0) {
			for(HistoricoAlteracaoProcesso hap : historicoAlteracaoProcessos) {
				historicoReporitory.deleteById(hap.getCodigo());
			}
		}
		//Apaga os índices de processo
		log.info("Excluíndo os índices de processo");
		Optional<IndiceProcesso> indice = indiceProcessoRepository.findByProcessoCodigo(processo);
		if(indice.isPresent()) {
			indiceProcessoRepository.deleteById(indice.get().getCodigo());
		}
		//Agora, por último, exclui o processo definitivamente, depois que todos os registros foram eliminados
		log.info("Exclui o processo");
		processoRepository.deleteById(processo);
	}
	
	private boolean exibeAtividadePrivada(long atividade, long usuario) {
		List<AtividadeUsuario> usuariosAtividades = atividadeUsuarioRepository.findByAtividadeCodigo(atividade);
		if(usuariosAtividades == null) {
			throw new EntidadeNaoEncontradaException("Usuários da atividade não encontrados");
		}
		
		Optional<Usuario> usuarioEntity = usuarioRepository.findById(usuario);
		if(!usuarioEntity.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuários não encontrado");
		}
		
		Optional<Atividade> atividadeEntity = atividadeRepository.findById(atividade);
		if(!atividadeEntity.isPresent()) {
			throw new EntidadeNaoEncontradaException("Atividade não encontrada");
		}
		
		for(AtividadeUsuario au : usuariosAtividades) {
			if(atividadeEntity.get().getPrivado().equals("N") || au.getUsuario().getCodigo() == usuario || usuarioEntity.get().getPerfil().getCodigo() == 1) {
				return true;
			}
		}
		
		return false;
	}
}
