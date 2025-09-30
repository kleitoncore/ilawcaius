package com.br.ilawgestao.domains.service;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import com.br.ilawgestao.domains.RodizioUsuarioAtividade;
import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.exception.SaldoInsuficienteException;
import com.br.ilawgestao.domains.models.*;
import com.br.ilawgestao.domains.repository.*;
import com.br.ilawgestao.domains.repository.filtros.FiltroHistoricoAtividadeFase;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.exception.UsuarioSemPermissaoException;
import com.br.ilawgestao.domains.repository.filtros.FiltroAgenda;
import com.br.ilawgestao.domains.repository.filtros.FiltroKanban;
import com.br.ilawgestao.domains.repository.filtros.FiltroMiniAgenda;
import com.br.ilawgestao.domains.utils.DatasUtil;

import javax.swing.text.html.Option;

@Service
public class AtividadeService {
	
	@Autowired
	private AtividadeRepository atividadeRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private GrupoTrabalhoRepository grupoTrabalhoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ProcessoService processoService;
	
	@Autowired
	private AtividadeUsuarioRepository atividadeUsuarioRepository;
	
	@Autowired
	private HistoricoAtividadeRepository historicoAtividadeRepository;
		
	@Autowired
	private PontuacaoAtividadeRepository pontuacaoRepository;
	
	@Autowired
	private CentralAtividadeRepository centralRepository;
	
	@Autowired
	private ArquivoAtividadeService arquivoAtividadeService;
	
	@Autowired
	private ArquivoAtividadeRepository arquivoAtividadeRepository;
		
	@Autowired
	private ArquivoProcessoRepository arquivoProcessoRepository;
	
	@Autowired
	private PartesRepository partesRepository;
	
	@Autowired
	private AtividadeShortRepository atividadeShortRepository;
	
	@Autowired
	private StatusAtividadeRepository statusAtividadeRepository;
	
	@Autowired
	private AgendaRepository agendaRepository;
	
	@Autowired
	private ProcessoImportanciaRepository processoImportanciaRepository;
	
	@Autowired
	private HistoricoProcessoRepository historicoProcessoRepository;
	
	@Autowired
	private UsuarioTarefaRepository usuarioTarefaRepository;
		
	@Autowired
	private TipoAndamentoProcessualRepository andamentoRepository;
	
	@Autowired
	private ExecucaoCheckListRepository checkListRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private FaseRepository faseRepository;

	@Autowired
	private HistoricoFaseProcesualRepository historicoFaseProcesualRepository;

	@Autowired
	private TituloAtividadeRepository tituloAtividadeRepository;

	@Autowired
	private UsuarioGrupoTrabalhoRepository usuarioGrupoTrabalhoRepository;

	@Autowired
	private ContaFinanceiraRepository contaFinanceiraRepository;

	@Autowired
	private LancamentoRepostory lancamentoRepostory;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private RodizioUsuarioAtividadeRepository rodizioUsuarioAtividadeRepository;

	private int PERFIL_ADMINISTRADOR = 1;
	private int ATIVIDADE_CRIADA = 1;
	private String USUARIO_TIPO_RESPONSAVEL = "R";
	
	public Atividade incluirAtividade(AtividadeCadastroDTO dto) {
		if(dto.getAtividade().getProcesso() != null) {
			Optional<Processo> processoConsulta = processoRepository.findById(dto.getAtividade().getProcesso().getCodigo());
			if(!processoConsulta.isPresent()) {
				throw new EntidadeNaoEncontradaException("Processo não encontrado ou excluído");
			}
		}

		// Ajeitar isso aqui
		if(dto.getAtividade().getDtFatal().equals("")) {
			dto.getAtividade().setDtFatal(null);
		}

		if(dto.getAtividade().getPessoa() != null) {
			Optional<Pessoa> pessoa = pessoaRepository.findById(dto.getAtividade().getPessoa().getCodigo());
			if(!pessoa.isPresent()) {
				throw new EntidadeNaoEncontradaException("Pessoa informada não encontrada");
			}
		}
		
		Optional<GrupoTrabalho> grupoConsulta = grupoTrabalhoRepository.findById(dto.getAtividade().getSubGrupo().getCodigo());
		if(!grupoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Sub Grupo de Trabalho não encontrado ou excluído");
		}
		
		Optional<Usuario> usuarioConsulta = usuarioRepository.findById(dto.getAtividade().getUsuario().getCodigo());
		if(!usuarioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado ou excluído");
		}

		Optional<StatusAtividade> status =
				statusAtividadeRepository.findByStatusAndEmpresaCodigo("Criada", usuarioConsulta.get().getEmpresa().getCodigo());
		Atividade atividadeSalva = null;
		List<Atividade> atividades = new ArrayList<Atividade>();
		if(dto.getRecorrencia() != 0) {
			SimpleDateFormat formatoBanco = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
            Date dataLimite = null;																																													
            Date dataFatal = null;
            try {
            	dataLimite = formatoBanco.parse(dto.getAtividade().getDtLimite());
            	if(dto.getAtividade().getDtFatal() != null && dto.getAtividade().getDtFatal() != "") {
            		dataFatal = formatoBanco.parse(dto.getAtividade().getDtFatal());
            	}
            } catch(Exception e ) {
               e.printStackTrace();
            }
            //Repetição das atividades																																						
            for(int i = 0; i < dto.getRecorrencia(); i++) {
            	Atividade atividade = new Atividade();
            	atividade.setTitulo(dto.getAtividade().getTitulo());
            	atividade.setDescricao(dto.getAtividade().getDescricao());
            	atividade.setSubGrupo(dto.getAtividade().getSubGrupo());
            	atividade.setUsuario(dto.getAtividade().getUsuario());
            	atividade.setTipo(dto.getAtividade().getTipo());
            	atividade.setProcesso(dto.getAtividade().getProcesso());
				atividade.setTituloAtividade(dto.getAtividade().getTituloAtividade());
            	//c.setTime(dataLimite);
            	//c.set(Calendar.MONTH, c.get(Calendar.MONTH) + i);
            	//atividade.setDtLimite(formatoBanco.format(c.getTime()));
				atividade.setDtLimite(dto.getAtividade().getDtLimite());
            	if(dto.getAtividade().getDtFatal() != null && dto.getAtividade().getDtFatal() != "") {
            		//c.setTime(dataFatal);
                	//c.set(Calendar.MONTH, c.get(Calendar.MONTH) + i);
                	//atividade.setDtFatal(formatoBanco.format(c.getTime()));
					atividade.setDtFatal(dto.getAtividade().getDtFatal());
            	}
            	atividade.setStatus(status.get());
            	atividade.setDtRegistro(DatasUtil.getDataAtual());
            	atividade.setImportante(dto.getAtividade().getImportante());
            	atividade.setUrgente(dto.getAtividade().getUrgente());
            	atividade.setPrivado(dto.getAtividade().getPrivado());
				atividade.setPessoa(dto.getAtividade().getPessoa());
				atividade.setLancamentoFinanceiro(dto.getAtividade().getLancamentoFinanceiro());
            	atividadeSalva = atividadeRepository.save(atividade);
            	atividades.add(atividadeSalva);
            }
		} else {
			dto.getAtividade().setDtRegistro(DatasUtil.getDataAtual());
			dto.getAtividade().setStatus(status.get());
			atividadeSalva = atividadeRepository.save(dto.getAtividade());
			this.incluirHistoricoFaseProcessual(atividadeSalva);
			atividades.add(atividadeSalva);
			dto.getAtividade().setCodigo(atividadeSalva.getCodigo());
		}
		
		//Incluíndo Responsáveis e Interessados
		if(dto.getResponsaveis() != null && dto.getResponsaveis().size() > 0) {
			this.incluirUsuariosAtividade(dto, atividades, "R");
		}
		
		if(dto.getInteressados() != null && dto.getInteressados().size() > 0) {
			this.incluirUsuariosAtividade(dto, atividades, "I");
		}
		
		//Responsáveis na central de atividades, exceto ele mesmo
		for(Usuario usu: dto.getResponsaveis()) {
			if(!usu.getCodigo().equals(dto.getAtividade().getUsuario().getCodigo())) {
				this.incluirAtividadeCentral(atividades, "Você tem uma nova Atividade", usu);
			}
		}
		
		if(dto.getInteressados() != null) {
			for(Usuario usu : dto.getInteressados()) {
				if(!usu.getCodigo().equals(dto.getAtividade().getUsuario().getCodigo())) {
					this.incluirAtividadeCentral(atividades, "Você foi incluído em uma atividade como interessado", usu);
				}
			}
		}
		
		//Atividade Short
		dto.getAtividade().setCodigo(atividadeSalva.getCodigo());
		this.incluirAtividadeShort(dto, atividades);
		//Incluir na Agenda
		this.incluirAtividadeAgenda(dto,atividades);
		
		
		//Arquivos anexados
		List<ArquivoAtividadeTemp> arquivosTemp = arquivoAtividadeService.listarArquivosTemp(dto.getAtividade().getUsuario().getCodigo());
		if(arquivosTemp != null && arquivosTemp.size() > 0) {
			for(ArquivoAtividadeTemp arq : arquivosTemp) {
				ArquivoAtividade arquivo = new ArquivoAtividade();
				arquivo.setArquivo(arq.getArquivo());
				arquivo.setDsArquivo(arq.getDescricao());
				arquivo.setTipo(arq.getTipo());
				arquivo.setAtividade(atividadeSalva);
				arquivo.setFile(arq.getFile());
				arquivo.setDtArquivo(DatasUtil.getDataAtual());
				arquivo.setUsuario(dto.getAtividade().getUsuario());
				arquivoAtividadeRepository.save(arquivo);
			}
			
			//Verifica se a atividade está associada a um processo. Se estiver, insere os arquivos no processo
			if(dto.getAtividade().getProcesso() != null) {
				for(ArquivoAtividadeTemp arq : arquivosTemp) {
					ArquivoProcesso arquivo = new ArquivoProcesso();
					arquivo.setNome(arq.getArquivo());
					arquivo.setDsArquivo(arq.getDescricao());
					arquivo.setTipo(arq.getTipo());
					arquivo.setProcesso(dto.getAtividade().getProcesso());
					arquivo.setFile(arq.getFile());
					arquivo.setDataArquivo(DatasUtil.getDataAtual());
					arquivo.setUsuario(dto.getAtividade().getUsuario());
					arquivoProcessoRepository.save(arquivo);
				}
			}
		}
		
		for(Atividade ativ : atividades) {
			HistoricoAtividadeDTO historicoDTO = new HistoricoAtividadeDTO();
			historicoDTO.setAtividade(ativ);
			historicoDTO.setUsuario(dto.getAtividade().getUsuario());
			historicoDTO.setDsHistorico("Atividade incluída pelo usuário: " + dto.getAtividade().getUsuario().getNome());
			historicoDTO.setDtHistorico(DatasUtil.getDataAtual());
			historicoDTO.setTpHistorico("A"); // Automático
			historicoAtividadeRepository.save(historicoDTO.transformeParaObjeto());	
		}
		
		return atividadeSalva;
	}
	
	private void incluirUsuariosAtividade(AtividadeCadastroDTO dtos, List<Atividade> atividades, String tipo) {
		if(tipo.equals("R")) {
			for(Usuario usu : dtos.getResponsaveis()) {
				for(Atividade atividade : atividades) {
					AtividadeUsuario atividadeUsuario = new AtividadeUsuario();
					atividadeUsuario.setUsuario(usu);
					atividadeUsuario.setAtividade(atividade);
					atividadeUsuario.setTipo(tipo);
					atividadeUsuarioRepository.save(atividadeUsuario);
				}
			}
		} else {
			for(Usuario usu : dtos.getInteressados()) {
				for(Atividade atividade : atividades) {
					AtividadeUsuario atividadeUsuario = new AtividadeUsuario();
					atividadeUsuario.setUsuario(usu);
					atividadeUsuario.setAtividade(atividade);
					atividadeUsuario.setTipo(tipo);
					atividadeUsuarioRepository.save(atividadeUsuario);
				}
			}
		}
	}
	
	public void incluirAtividadeShort(AtividadeCadastroDTO dto, List<Atividade> atividades) {
		for(Atividade ativ: atividades) {
			for(Usuario usuResp : dto.getResponsaveis()) {
				AtividadeShort atividade = new AtividadeShort();
				atividade.setCodigoAtividade(ativ.getCodigo());
				atividade.setTitulo(dto.getAtividade().getTitulo());
				atividade.setTipoAtividade(dto.getAtividade().getTipo());
				atividade.setDtLimite(ativ.getDtLimite());
				atividade.setDtfatal(ativ.getDtFatal());
				atividade.setImportanteAtividade(dto.getAtividade().getImportante());
				atividade.setUrgencia(dto.getAtividade().getUrgente());
				atividade.setPrivado(dto.getAtividade().getPrivado());
				if(dto.getAtividade().getPessoa() != null) {
					atividade.setCodigoCliente(dto.getAtividade().getPessoa().getCodigo());
					atividade.setNomeCliente(dto.getAtividade().getPessoa().getNome());
				}
				if(dto.getAtividade().getProcesso() != null) {
					atividade.setCodigoProcesso(dto.getAtividade().getProcesso().getCodigo());
					Optional<Processo> processoConsulta = processoRepository.findById(dto.getAtividade().getProcesso().getCodigo());
					if(processoConsulta.isPresent()) {
						atividade.setCnj(processoConsulta.get().getNrCnj());
						atividade.setPasta(processoConsulta.get().getPasta());
						atividade.setNrprocesso(processoConsulta.get().getNrProcesso());
						//Partes
						String autor = "";
						String reu = "";
						List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(dto.getAtividade().getProcesso().getCodigo());
						for(Partes parte : partes) {
							if(parte.getTipoParte().equals("A")) {
								autor = autor + parte.getPessoa().getNome() + ",";
							} else { 
								reu = reu + parte.getPessoa().getNome() + ",";
							}
						}
						
						atividade.setPartes(autor + " x " + reu);
					}
				} else {
					atividade.setCodigoProcesso(0);
				}
				
				//Grupo de Trabalho
				Optional<GrupoTrabalho> grupoTrabalho = grupoTrabalhoRepository.findById(dto.getAtividade().getSubGrupo().getGrupoPai());
				atividade.setGrupo(grupoTrabalho.get().getNome());
				atividade.setCodigoGrupo(grupoTrabalho.get().getCodigo());
				//Sub-Grupo de Trabalho
				Optional<GrupoTrabalho> subGrupoTrabalho = grupoTrabalhoRepository.findById(dto.getAtividade().getSubGrupo().getCodigo());
				atividade.setSubGrupo(subGrupoTrabalho.get().getNome());
				//Responsáveis e Interessados
				List<AtividadeUsuario> atividadeUsuarios = atividadeUsuarioRepository.findByAtividadeCodigo(dto.getAtividade().getCodigo());
				String responsaveis = "";
				String interessados = "";
				for(AtividadeUsuario au: atividadeUsuarios) {
					if(au.getTipo().equals("R")) {
						responsaveis = responsaveis + au.getUsuario().getNome() + ",";
					} else {
						interessados = interessados + au.getUsuario().getNome() + ",";
					}
				}
				
				atividade.setResponsaveis(responsaveis);
				if(interessados != "") {
					atividade.setInteressados(interessados);
				}
				
				atividade.setCodigoResponsavel(usuResp.getCodigo());
				atividade.setStatus(ativ.getStatus().getCodigo());
				//Optional<StatusAtividade> statusConsulta = statusAtividadeRepository.findById(dto.getAtividade().getStatus().getCodigo());
				atividade.setDescricaoStatus(ativ.getStatus().getStatus());
				atividade.setCorStatus(ativ.getStatus().getCor());
				atividade.setTipoAtividade(dto.getAtividade().getTipo());
				atividade.setDtRegistro(DatasUtil.getDataAtual());
				if(dto.getAtividade().getProcesso() != null) {
					ProcessoImportancia processoImportancia = processoImportanciaRepository.findProcessoImportanciaByUsuarioCodigoAndProcessoCodigo(usuResp.getCodigo(), dto.getAtividade().getProcesso().getCodigo());
					if(processoImportancia != null) {
						atividade.setFavorito("S");
					} else {
						atividade.setFavorito("N");
					}
					atividade.setImportante(dto.getAtividade().getProcesso().getSnImportante());
				} else {
					atividade.setFavorito("N");
					atividade.setImportante("N");
				}
				atividadeShortRepository.save(atividade);
			}
		}
		
		if(dto.getInteressados() != null && dto.getInteressados().size() > 0) {
			for(Atividade ativ: atividades) {
				for(Usuario usuInt : dto.getInteressados()) {
					AtividadeShort atividade = new AtividadeShort();
					atividade.setCodigoAtividade(ativ.getCodigo());
					atividade.setTitulo(dto.getAtividade().getTitulo());
					atividade.setTipoAtividade(dto.getAtividade().getTitulo());
					atividade.setDtLimite(ativ.getDtLimite());
					atividade.setDtfatal(ativ.getDtFatal());
					atividade.setImportanteAtividade(dto.getAtividade().getImportante());
					atividade.setUrgencia(dto.getAtividade().getUrgente());
					atividade.setPrivado(dto.getAtividade().getPrivado());
					if(dto.getAtividade().getPessoa() != null) {
						atividade.setCodigoCliente(dto.getAtividade().getPessoa().getCodigo());
						atividade.setNomeCliente(dto.getAtividade().getPessoa().getNome());
					}
					if(dto.getAtividade().getProcesso() != null) {
						atividade.setCodigoProcesso(dto.getAtividade().getProcesso().getCodigo());
						Optional<Processo> processoConsulta = processoRepository.findById(dto.getAtividade().getProcesso().getCodigo());
						if(processoConsulta.isPresent()) {
							atividade.setCnj(processoConsulta.get().getNrCnj());
							atividade.setPasta(processoConsulta.get().getPasta());
							atividade.setNrprocesso(processoConsulta.get().getNrProcesso());
							//Partes
							String autor = "";
							String reu = "";
							List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(dto.getAtividade().getProcesso().getCodigo());
							for(Partes parte : partes) {
								if(parte.getTipoParte().equals("A")) {
									autor = autor + parte.getPessoa().getNome() + ",";
								} else { 
									reu = reu + parte.getPessoa().getNome() + ",";
								}
							}
							
							atividade.setPartes(autor + " x " + reu);
						}
					} else {
						atividade.setCodigoProcesso(0);
					}
					
					//Grupo de Trabalho
					Optional<GrupoTrabalho> grupoTrabalho = grupoTrabalhoRepository.findById(dto.getAtividade().getSubGrupo().getGrupoPai());
					atividade.setGrupo(grupoTrabalho.get().getNome());
					//Sub-Grupo de Trabalho
					Optional<GrupoTrabalho> subGrupoTrabalho = grupoTrabalhoRepository.findById(dto.getAtividade().getSubGrupo().getCodigo());
					atividade.setSubGrupo(subGrupoTrabalho.get().getNome());
					atividade.setCodigoGrupo(grupoTrabalho.get().getCodigo());
					//Responsáveis e Interessados
					List<AtividadeUsuario> atividadeUsuarios = atividadeUsuarioRepository.findByAtividadeCodigo(dto.getAtividade().getCodigo());
					String responsaveis = "";
					String interessados = "";
					for(AtividadeUsuario au: atividadeUsuarios) {
						if(au.getTipo().equals("R")) {
							responsaveis = responsaveis + au.getUsuario().getNome() + ",";
						} else {
							interessados = interessados + au.getUsuario().getNome() + ",";
						}
					}
					
					atividade.setResponsaveis(responsaveis);
					if(interessados != "") {
						atividade.setInteressados(interessados);
					}
					
					atividade.setCodigoInteressado(usuInt.getCodigo());
					atividade.setStatus(ativ.getStatus().getCodigo());
					atividade.setDescricaoStatus(ativ.getStatus().getStatus());
					atividade.setCorStatus(ativ.getStatus().getCor());
					atividade.setTipoAtividade(dto.getAtividade().getTipo());
					atividade.setDtRegistro(DatasUtil.getDataAtual());
					if(dto.getAtividade().getProcesso() != null) {
						ProcessoImportancia processoImportancia = processoImportanciaRepository.findProcessoImportanciaByUsuarioCodigoAndProcessoCodigo(usuInt.getCodigo(), dto.getAtividade().getProcesso().getCodigo());
						if(processoImportancia != null) {
							atividade.setFavorito("S");
						} else {
							atividade.setFavorito("N");
						}
						atividade.setImportante(dto.getAtividade().getProcesso().getSnImportante());
					} else {
						atividade.setFavorito("N");
						atividade.setImportante("N");
					}
					atividadeShortRepository.save(atividade);
				}
			}
		}
		
	}
	
	private void incluirAtividadeAgenda(AtividadeCadastroDTO dto, List<Atividade> atividades) {
		for(Atividade atividade : atividades) {
			for(Usuario resp : dto.getResponsaveis()) {
				Agenda agenda = new Agenda();
				agenda.setCodigoAtividade(atividade.getCodigo());
				agenda.setTitulo(dto.getAtividade().getTitulo());
				agenda.setCodigoResponsavel(resp.getCodigo());
				Optional<GrupoTrabalho> grupo = grupoTrabalhoRepository.findById(dto.getAtividade().getSubGrupo().getCodigo());
				agenda.setCodigoGrupo(grupo.get().getGrupoPai());
				agenda.setStatus(atividade.getStatus().getCodigo());
				agenda.setResponsavel(resp.getNome());
				if(dto.getAtividade().getProcesso() != null) {
					agenda.setProcesso(dto.getAtividade().getProcesso().getNrCnj());
				} else {
					agenda.setProcesso(null);
				}
				
				agenda.setTpAtividade(dto.getAtividade().getTipo());
				agenda.setDataCompromisso(atividade.getDtLimite());
				agenda.setTpAtividade(dto.getAtividade().getTipo());
				agendaRepository.save(agenda);
			}
			
			if(dto.getInteressados() != null && dto.getInteressados().size() > 0) {
				for(Usuario inter : dto.getInteressados()) {
					Agenda agenda = new Agenda();
					agenda.setCodigoAtividade(dto.getAtividade().getCodigo());
					agenda.setTitulo(dto.getAtividade().getTitulo());
					agenda.setCodigoResponsavel(inter.getCodigo());
					Optional<GrupoTrabalho> grupo = grupoTrabalhoRepository.findById(dto.getAtividade().getSubGrupo().getCodigo());
					agenda.setCodigoGrupo(grupo.get().getGrupoPai());
					agenda.setStatus(atividade.getStatus().getCodigo());
					agenda.setResponsavel(inter.getNome());
					if(dto.getAtividade().getProcesso() != null) {
						agenda.setProcesso(dto.getAtividade().getProcesso().getNrCnj());
					} else {
						agenda.setProcesso(null);
					}
					
					agenda.setTpAtividade(dto.getAtividade().getTipo());
					agenda.setDataCompromisso(atividade.getDtLimite());
					agenda.setTpAtividade(dto.getAtividade().getTipo());
					agendaRepository.save(agenda);
				}
			}
		}
	}
	
	private void excluirAgenda(Atividade atividade) {
		List<Agenda> agenda = agendaRepository.findByCodigoAtividade(atividade.getCodigo());
		if(agenda != null && agenda.size() > 0) {
			for(Agenda ag : agenda) {
				agendaRepository.deleteById(ag.getCodigo());
			}
		}
	}
	
	private void incluirAtividadeCentral(List<Atividade> atividades, String mensagem, Usuario usuario) {
		for(Atividade atividade : atividades) {
			CentralAtividade central = new CentralAtividade();
			central.setAtividade(atividade);
			central.setUsuario(usuario);
			central.setDataRegistro(DatasUtil.getDataAtual());
			central.setDescricao(mensagem);
			central.setStatus(this.ATIVIDADE_CRIADA);
			centralRepository.save(central);
		}
	}
	
	private String retornaGrupoTrabalhoPai(long grupoPai) {
		GrupoTrabalho grupo = grupoTrabalhoRepository.findById(grupoPai).get();
		return grupo.getNome();
	}
	
	public List<AtividadeShort> listarAtividadesAtrasadasResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaAtividadesAtrasadasResponsavel(usuario,pesquisa,importante,urgente, fatal);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			} else {
				atividade.setDtfatal(null);
			}
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				if(ativ.getCnj().length() == 20) {
					cnjMascarado = ativ.getCnj().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
				} else {
					cnjMascarado = ativ.getCnj();
				}
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setResponsaveis(ativ.getResponsaveis());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setCorStatus(ativ.getCorStatus());
			atividade.setImportanteAtividade(ativ.getImportanteAtividade());
			atividade.setUrgencia(ativ.getUrgencia());
			listaAtividades.add(atividade);
		}
		return listaAtividades;
	}
	
	public List<AtividadeShort> listarAtividadesHojeResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaAtividadesHojeResponsavel(usuario,pesquisa,importante,urgente,fatal);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				cnjMascarado = ativ.getCnj().substring(0, 7);
				cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setResponsaveis(ativ.getResponsaveis());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setImportanteAtividade(ativ.getImportanteAtividade());
			atividade.setUrgencia(ativ.getUrgencia());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}
	
		return listaAtividades;
	}
	
	public List<AtividadeShort> listarAtividadesSeteResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaAtividadesSeteResponsavel(usuario, pesquisa,importante,urgente,fatal);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				cnjMascarado = ativ.getCnj().substring(0, 7);
				cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setResponsaveis(ativ.getResponsaveis());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setImportanteAtividade(ativ.getImportanteAtividade());
			atividade.setUrgencia(ativ.getUrgencia());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}
		
		return listaAtividades;
	}
	
	public List<AtividadeShort> listarAtividadesTrintaResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaAtividadesTrintaResponsavel(usuario, pesquisa, importante, urgente,fatal);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				if(ativ.getCnj().length() == 20) {
					cnjMascarado = ativ.getCnj().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
				} else {
					cnjMascarado = ativ.getCnj();
				}
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setResponsaveis(ativ.getResponsaveis());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setImportanteAtividade(ativ.getImportanteAtividade());
			atividade.setUrgencia(ativ.getUrgencia());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}
		
		return listaAtividades;
	}
	
	public List<AtividadeShort> listarAtividadesAtrasadasInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaAtividadesAtrasadasInteressado(usuario, pesquisa,importante,urgente,fatal);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				if(ativ.getCnj().length() == 20) {
					cnjMascarado = ativ.getCnj().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
				} else {
					cnjMascarado = ativ.getCnj();
				}
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setInteressados(ativ.getInteressados());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setImportanteAtividade(ativ.getImportanteAtividade());
			atividade.setUrgencia(ativ.getUrgencia());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}
		
		return listaAtividades;
	}
	
	public List<AtividadeShort> listarAtividadesHojeInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaAtividadesHojeInteressado(usuario, pesquisa,importante,urgente,fatal);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				if(ativ.getCnj().length() == 20) {
					cnjMascarado = ativ.getCnj().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
				} else {
					cnjMascarado = ativ.getCnj();
				}
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setInteressados(ativ.getInteressados());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setImportanteAtividade(ativ.getImportanteAtividade());
			atividade.setUrgencia(ativ.getUrgencia());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}
		
		return listaAtividades;
	}
	
	public List<AtividadeShort> listarAtividadesSeteInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaAtividadesSeteInteressado(usuario, pesquisa,importante,urgente,fatal);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				if(ativ.getCnj().length() == 20) {
					cnjMascarado = ativ.getCnj().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
				} else {
					cnjMascarado = ativ.getCnj();
				}
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setInteressados(ativ.getInteressados());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setImportanteAtividade(ativ.getImportanteAtividade());
			atividade.setUrgencia(ativ.getUrgencia());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}
		
		return listaAtividades;
	}
	
	public List<AtividadeShort> listarAtividadesTrintaInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaAtividadesTrintaInteressado(usuario, pesquisa, importante, urgente,fatal);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				if(ativ.getCnj().length() == 20) {
					cnjMascarado = ativ.getCnj().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
				} else {
					cnjMascarado = ativ.getCnj();
				}
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setInteressados(ativ.getInteressados());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setImportanteAtividade(ativ.getImportanteAtividade());
			atividade.setUrgencia(ativ.getUrgencia());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}

		return listaAtividades;
	}
	
	public List<AtividadeShort> listarCompromissosAtrasadasResponsavel(long usuario, String pesquisa) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaCompromissosAtrasadosResponsavel(usuario, pesquisa);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				if(ativ.getCnj().length() == 20) {
					cnjMascarado = ativ.getCnj().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
				} else {
					cnjMascarado = ativ.getCnj();
				}
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setResponsaveis(ativ.getResponsaveis());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}

		return listaAtividades;
	}
	
	public List<AtividadeShort> listarCompromissosHojeResponsavel(long usuario, String pesquisa) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaCompromissosHojeResponsavel(usuario, pesquisa);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				if(ativ.getCnj().length() == 20) {
					cnjMascarado = ativ.getCnj().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
				} else {
					cnjMascarado = ativ.getCnj();
				}
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setResponsaveis(ativ.getResponsaveis());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}

		return listaAtividades;
	}
	
	public List<AtividadeShort> listarCompromissosSeteResponsavel(long usuario, String pesquisa) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaCompromissosSeteResponsavel(usuario, pesquisa);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				if(ativ.getCnj().length() == 20) {
					cnjMascarado = ativ.getCnj().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
				} else {
					cnjMascarado = ativ.getCnj();
				}
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setResponsaveis(ativ.getResponsaveis());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}

		return listaAtividades;		
	}
	
	public List<AtividadeShort> listarCompromissosTrintaResponsavel(long usuario, String pesquisa) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.listaCompromissosTrintaResponsavel(usuario, pesquisa);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setTipoAtividade(ativ.getTipoAtividade());
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("FINANCEIRO");
			} else {
				atividade.setSnFinanceiro("TAREFA");
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null) {
				if(ativ.getCnj().length() == 20) {
					cnjMascarado = ativ.getCnj().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
				} else {
					cnjMascarado = ativ.getCnj();
				}
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setResponsaveis(ativ.getResponsaveis());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			atividade.setCodigoCliente(ativ.getCodigoCliente());
			atividade.setNomeCliente(ativ.getNomeCliente());
			atividade.setCorStatus(ativ.getCorStatus());
			listaAtividades.add(atividade);
		}

		return listaAtividades;
	}

	private Atividade lerAtividade(Atividade ativ, long usuario) {
		Atividade atividadeLida = atividadeRepository.findById(ativ.getCodigo()).get();
		Optional<StatusAtividade> statusAtividadeLer =
				statusAtividadeRepository.findByStatusAndEmpresaCodigo("Ativo",atividadeLida.getUsuario().getEmpresa().getCodigo());
		atividadeLida.setStatus(statusAtividadeLer.get());
		atividadeLida.setDataLida(DatasUtil.getDataAtual());
		Atividade atividadeLidaSalva = atividadeRepository.save(atividadeLida);
		if(atividadeLidaSalva != null) {
			//Short
			List<AtividadeShort> atividadeShortLida = atividadeShortRepository.findByCodigoAtividade(ativ.getCodigo());
			if(atividadeShortLida != null && atividadeShortLida.size() > 0) {
				for(AtividadeShort ashort : atividadeShortLida) {
					ashort.setStatus(statusAtividadeLer.get().getCodigo());
					ashort.setDescricaoStatus(statusAtividadeLer.get().getStatus());
					ashort.setCorStatus(statusAtividadeLer.get().getCor());
					ashort.setDataLida(ativ.getDataLida());
					atividadeShortRepository.save(ashort);
				}
			}
			//Agenda
			List<Agenda> agendaAtividadeLida = agendaRepository.findByCodigoAtividade(ativ.getCodigo());
			if(agendaAtividadeLida != null && agendaAtividadeLida.size() > 0) {
				for(Agenda alida : agendaAtividadeLida) {
					alida.setStatus(statusAtividadeLer.get().getCodigo());
					alida.setDataLida(ativ.getDataLida());
					agendaRepository.save(alida);
				}
			}
			//Histórico
			HistoricoAtividade ha = new HistoricoAtividade();
			ha.setAtividade(ativ);
			Optional<Usuario> usuarioLeu = usuarioRepository.findById(usuario);
			ha.setUsuario(usuarioLeu.get());
			ha.setTpHistorico("A");
			ha.setDtHistorico(DatasUtil.getDataAtual());
			ha.setDsHistorico("Atividade lida e ativada por: " + usuarioLeu.get().getNome() + " em " + DatasUtil.formatarDataTela(ha.getDtHistorico()));
			this.incluirHistorico(ha);
		}

		return atividadeLidaSalva;
	}
	
	public Atividade consultarAtividade(long codigo, long usuario) {
		Atividade ativ = atividadeRepository.findById(codigo).get();
		//Consultar atividade na tabela de atividades para recuperar a pontuação
		Optional<Usuario> usuarioConsultado = usuarioRepository.findById(usuario);

		Optional<TituloAtividade> tituloAtividade = null;
		if(ativ.getTituloAtividade() != null) {
			tituloAtividade = tituloAtividadeRepository.findById(ativ.getTituloAtividade().getCodigo());
		}

		if(ativ.getStatus().getStatus().equals("Criada")) {
			//Alterar para ativo neste momento

			List<AtividadeUsuario> atividadeUsuario = atividadeUsuarioRepository.findByAtividadeCodigo(codigo);
			boolean usuarioIsResponsavel = false;
			for(AtividadeUsuario au : atividadeUsuario) {
				if(au.getUsuario().getCodigo() == usuario && au.getTipo().equals("R")) {
					usuarioIsResponsavel = true;
				}
			}

			if(usuarioIsResponsavel) {
				this.lerAtividade(ativ,usuario);
			}

		}
		Atividade atividade = new Atividade();
		atividade.setCodigo(ativ.getCodigo());
		atividade.setTitulo(ativ.getTitulo());
		atividade.setDescricao(ativ.getDescricao());
		atividade.setUsuario(ativ.getUsuario());
		atividade.setDtRegistro(DatasUtil.formatarDataTela(ativ.getDtRegistro()));
		atividade.setTipo(ativ.getTipo());
		atividade.setDtLimite(DatasUtil.formatarAgenda(ativ.getDtLimite()));
		if(tituloAtividade == null) {
			atividade.setPontuacao(0);
		} else {
			atividade.setPontuacao(tituloAtividade.get().getPontos());
		}
		if(ativ.getTipo().equals("A")) {
			atividade.setHora(DatasUtil.formatarHoraTela(ativ.getDtLimite()));
		}
		atividade.setSubGrupo(ativ.getSubGrupo());
		atividade.setStatus(ativ.getStatus());
		atividade.setGrupoPai(this.retornaGrupoTrabalhoPai(ativ.getSubGrupo().getGrupoPai()));
		if(ativ.getDtFatal() != null) {
			atividade.setDtFatal(ativ.getDtFatal());
		} else {
			atividade.setDtFatal(null);
		}
		atividade.setImportante(ativ.getImportante());
		atividade.setUrgente(ativ.getUrgente());
		atividade.setPrivado(ativ.getPrivado());
		if(ativ.getProcesso() != null) {
			Processo pro = new Processo();
			pro.setCodigo(ativ.getProcesso().getCodigo());
			pro.setNrCnj(ProcessoDto.mascaraProcessoCnj(ativ.getProcesso().getNrCnj()));
			pro.setNrProcesso(ativ.getProcesso().getNrProcesso());
			pro.setPasta(ativ.getProcesso().getPasta());
			pro.setGrupoTrabalho(ativ.getProcesso().getGrupoTrabalho());
			pro.setAutor(processoService.retornaAutorVersusReu(ativ.getProcesso().getCodigo()));
			pro.setSnImportante(ativ.getProcesso().getSnImportante());
			pro.setFase(ativ.getProcesso().getFase());
			atividade.setProcesso(pro);
		} else {
			atividade.setProcesso(null);
		}

		if(ativ.getPessoa() != null) {
			atividade.setPessoa(ativ.getPessoa());
		}
		
		return atividade;
	}
	
	public List<HistoricoAtividade> consultarHistorico(long atividade) {
		Optional<Atividade> atividadeConsulta = atividadeRepository.findById(atividade);
		if(!atividadeConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Atividade não encontrada");
		}
		
		List<HistoricoAtividade> lista = historicoAtividadeRepository.findByAtividadeCodigoOrderByCodigoDesc(atividade);
		List<HistoricoAtividade> historicoLista = new ArrayList<HistoricoAtividade>();
		for(HistoricoAtividade hist : lista) {
			HistoricoAtividade historico = new HistoricoAtividade();
			historico.setCodigo(hist.getCodigo());
			historico.setAtividade(hist.getAtividade());
			historico.setDsHistorico(hist.getDsHistorico());
			historico.setTpHistorico(hist.getTpHistorico());
			historico.setDtHistorico(DatasUtil.formatarDataTela(hist.getDtHistorico()));
			historico.setUsuario(hist.getUsuario());
			historicoLista.add(historico);
		}
		
		return historicoLista;
	}
	
	public HistoricoAtividade incluirHistorico(HistoricoAtividade historico) {
		Optional<Atividade> atividade = atividadeRepository.findById(historico.getAtividade().getCodigo());
		if(!atividade.isPresent()) {
			throw new EntidadeNaoEncontradaException("Atividade não encontrada");
		}
		
		HistoricoAtividade historicoSalvo = historicoAtividadeRepository.save(historico);
		if(historicoSalvo != null) {
			//Central de Atividade
			List<AtividadeUsuario> atividadeUsuarios = atividadeUsuarioRepository.findByAtividadeCodigo(historico.getAtividade().getCodigo());
			for(AtividadeUsuario usuario: atividadeUsuarios) {
				List<Atividade> atividades = new ArrayList<Atividade>();
				atividades.add(historico.getAtividade());
				this.incluirAtividadeCentral(atividades, "Novo histórico adicionado a Atividade",
						usuario.getUsuario());
			}
		}
		
		return historicoSalvo;
	}
	
	public void excluirHistorico(long historico, long usuarioCadastrou, long usuarioSolicitou) {
		if(usuarioCadastrou != usuarioSolicitou) {
			throw new UsuarioSemPermissaoException("Usuário não tem permissão para excluir este histórico, apenas o seu criador");
		}
		
		historicoAtividadeRepository.deleteById(historico);
	}
	
	public List<Usuario> consultarUsuariosAtividade(long atividade, String tipo) {
		List<AtividadeUsuario> lista = atividadeUsuarioRepository.findByAtividadeCodigoAndTipoOrderByUsuarioNome(atividade, tipo);
		List<Usuario> usuarios = new ArrayList<Usuario>();
		for(AtividadeUsuario atividadeUsuario: lista) {
			usuarios.add(atividadeUsuario.getUsuario());
		}
		
		return usuarios;
	}
	
	@SuppressWarnings("unused")
	private AtividadeUsuario existeUsuarioAtividade(long atividade, long usuario) {
		Optional<AtividadeUsuario> atividadeConsulta = atividadeUsuarioRepository.findByAtividadeCodigoAndUsuarioCodigoAndTipo(atividade, usuario, "R");
		if(!atividadeConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não tem permissão para alterar esta atividade");
		}
		
		return atividadeConsulta.get();
	}
	
	public void excluirAtividadesShort(Atividade atividade) {
		List<AtividadeShort> atividades = atividadeShortRepository.findByCodigoAtividade(atividade.getCodigo());
		if(atividades != null) {
			for(AtividadeShort ativ : atividades) {
				atividadeShortRepository.deleteById(ativ.getCodigo());
			}
		}
	}
	
	private void excluirUsuariosAtividades(Atividade atividade) {
		List<AtividadeUsuario> atividadeUsuario = atividadeUsuarioRepository.findByAtividadeCodigo(atividade.getCodigo());
		if(atividadeUsuario != null) {
			for(AtividadeUsuario usu : atividadeUsuario) {
				atividadeUsuarioRepository.deleteById(usu.getCodigo());
			}
		}
	}
	
	private void incluirUsuarioAtividade(AtividadeCadastroDTO dto) {
		//Responsáveis
		for(Usuario resp : dto.getResponsaveis()) {
			AtividadeUsuario atividadeUsuario = new AtividadeUsuario();
			atividadeUsuario.setAtividade(dto.getAtividade());
			atividadeUsuario.setUsuario(resp);
			atividadeUsuario.setTipo("R");
			atividadeUsuarioRepository.save(atividadeUsuario);
		}
		
		//Interessados
		if(dto.getInteressados() != null && dto.getInteressados().size() > 0) {
			for(Usuario inter : dto.getInteressados()) {
				AtividadeUsuario atividadeUsuario = new AtividadeUsuario();
				atividadeUsuario.setAtividade(dto.getAtividade());
				atividadeUsuario.setUsuario(inter);
				atividadeUsuario.setTipo("I");
				atividadeUsuarioRepository.save(atividadeUsuario);
			}
		}
	}
	
	private void mergeAtividadeUsuario(AtividadeCadastroDTO dto) {
		this.excluirUsuariosAtividades(dto.getAtividade());
		this.incluirUsuarioAtividade(dto);
	}


	private static void mostrarDiferencas(List<Usuario> lista1, List<Usuario> lista2) {
		int maxLength = Math.max(lista1.size(), lista2.size());

		for (int i = 0; i < maxLength; i++) {
			Usuario pessoa1 = (i < lista1.size()) ? lista1.get(i) : null;
			Usuario pessoa2 = (i < lista2.size()) ? lista2.get(i) : null;

			if (!Objects.equals(pessoa1, pessoa2)) {
				System.out.printf("Diferente na posição %d:%n", i);
				if (pessoa1 != null) {
					System.out.println("Lista1: " + pessoa1);
				} else {
					System.out.println("Lista1: null");
				}
				if (pessoa2 != null) {
					System.out.println("Lista2: " + pessoa2);
				} else {
					System.out.println("Lista2: null");
				}
			}
		}
	}
	
	public Atividade alterarAtividade(long codigo, String dataAntiga, long statusAntigo, long usuario, AtividadeCadastroDTO dto) {
		Optional<Usuario> usuarioConsulta = usuarioRepository.findById(usuario);
		Optional<GrupoTrabalho> grupoTrabalho = grupoTrabalhoRepository.findById(dto.getAtividade().getSubGrupo().getCodigo());

		if(!grupoTrabalho.isPresent()) {
			throw new EntidadeNaoEncontradaException("Sub-Grupo de Trrabalho não encontrado");
		}
		
		if(dto.getAtividade().getProcesso() != null) {
			Optional<Processo> processo = processoRepository.findById(dto.getAtividade().getProcesso().getCodigo());
			if(!processo.isPresent()) {
				throw new EntidadeNaoEncontradaException("Processo não encontrado");
			}
		}
		
		if(dto.getAtividade().getDtFatal() == "") {
			dto.getAtividade().setDtFatal(null);
		}
		
		Atividade atividadeConsulta = atividadeRepository.findById(codigo).get();

		//Verifica se esta atividade é do tipo Lamçamento financeiro
		Lancamento lancamento = null;
		if(atividadeConsulta.getTipo().equals("L")) {
			Optional<Lancamento> lancamentoConsulta = lancamentoRepostory.findById(atividadeConsulta.getLancamentoFinanceiro().getCodigo());
			lancamento = lancamentoConsulta.get();
		}

		ContaFinanceira contaFinanceira = null;
		Optional<StatusAtividade> statusVerificaFinanceiro = statusAtividadeRepository.findById(dto.getAtividade().getStatus().getCodigo());
		if(statusVerificaFinanceiro.get().getStatus().equals("Concluído")) {
			//Verifica se a conta padrão tem saldo
			Optional<ContaFinanceira> contaFinanceiraConsulta =
					contaFinanceiraRepository.findByEmpresaCodigoAndContaDefault(usuarioConsulta.get().getEmpresa().getCodigo(),"S");
			if(contaFinanceiraConsulta.isPresent()) {
				contaFinanceira = contaFinanceiraConsulta.get();
			}

			//Verifica se o saldo da conta default é suficiente para pagar esta conta, se caso o tipo for de despesa
			if(lancamento.getTipo().getTipo().equals("D")) {
				if(lancamento.getVlLancamento() > contaFinanceira.getSaldo()) {
					throw new SaldoInsuficienteException("A conta finaceira default não possui saldo para baixar esta conta");
				}
			}
		}
		//Fim de verificação de connta financeira

		//Valida se o usuário não for responsváel e tentar mudar o responsável pela atividade
		//Primeiro verifica se o usuário envado está dentro das listas de responsáveis
		boolean isResponsavel = false;
		List<AtividadeUsuario> usuariosResponsaveis = atividadeUsuarioRepository.findByAtividadeCodigoAndTipoOrderByUsuarioNome(codigo,"R");
		List<AtividadeUsuario> usuariosInteressados = atividadeUsuarioRepository.findByAtividadeCodigoAndTipoOrderByUsuarioNome(codigo,"I");
		for(AtividadeUsuario au : usuariosResponsaveis) {
			if(usuario == au.getUsuario().getCodigo()) {
				isResponsavel = true;
				break;
			}
		}

		if(usuarioConsulta.get().getEmpresa().getCodigo() == 60 && !isResponsavel  && usuarioConsulta.get().getPerfil().getCodigo() != 1) {
			//Usuário não é responsável
			//Não pode fazer alterações nos usuários envolvidos
			List<Usuario> usuarios = new ArrayList<>();
			for(AtividadeUsuario au : usuariosResponsaveis) {
				Optional<Usuario> usuarioConfere = usuarioRepository.findById(au.getUsuario().getCodigo());
				usuarios.add(UsuarioDto.buildConsulta(usuarioConfere.get()));
			}

			List<Usuario> interessados = new ArrayList<>();
			for(AtividadeUsuario au : usuariosInteressados) {
				Optional<Usuario> usuarioConfere = usuarioRepository.findById(au.getUsuario().getCodigo());
				interessados.add(UsuarioDto.buildConsulta(usuarioConfere.get()));
			}

			if(!dto.getResponsaveis().equals(usuarios)) {
				throw new UsuarioSemPermissaoException("Você não tem permissão para mudar usuários responsáveis");
			}

			if(!dto.getInteressados().equals(interessados)) {
				throw new UsuarioSemPermissaoException("Você não tem permissão para mudar usuários interessados");
			}

			if(!dataAntiga.equals(dto.getAtividade().getDtLimite())) {
				throw new UsuarioSemPermissaoException("Você não tem permissão para alterar a data limite");
			}

			if(statusAntigo != dto.getAtividade().getStatus().getCodigo()) {
				throw new UsuarioSemPermissaoException("Você não tem permissão para alterar o status da atividade");
			}
		}

		BeanUtils.copyProperties(dto.getAtividade(), atividadeConsulta,"codigo", "usuario", "dtRegistro", "tituloAtividade","lancamentoFinanceiro");
		Optional<StatusAtividade> statusConcluido = statusAtividadeRepository.findById(dto.getAtividade().getStatus().getCodigo());
		if(statusConcluido.get().getStatus().equals("Concluído")) {
			atividadeConsulta.setDtConcluido(DatasUtil.getDataAtual());
		}
		

		if(dto.getAtividade().getUsuario().getEmpresa().getCodigo() == 138) {
			if(dto.getAtividade().getTipo().equals("A")) {
				if(!dataAntiga.equals(dto.getAtividade().getDtLimite())
						&& usuarioConsulta.get().getPerfil().getCodigo() != this.PERFIL_ADMINISTRADOR) {
					
					Optional<HistoricoAtividade> historicoAtividade  = historicoAtividadeRepository.consultarUltimoHistoricoAlteracaoData(usuario);
					if(!historicoAtividade.isPresent()) {
						throw new EntidadeNaoEncontradaException("Você precisa justificar a alteração desta data. Inclua um comentário no histórico");
					}
				}
			}
		}
		
		if(dto.getAtividade().getUsuario().getEmpresa().getCodigo() == 138) {
			if(!dataAntiga.equals(dto.getAtividade().getDtLimite()) && usuarioConsulta.get().getPerfil().getCodigo() != this.PERFIL_ADMINISTRADOR) {
				Optional<HistoricoAtividade> historicoAtividade  = historicoAtividadeRepository.consultarUltimoHistoricoAlteracaoData(usuario);
				if(!historicoAtividade.isPresent()) {
					throw new EntidadeNaoEncontradaException("Você precisa justificar a alteração desta data. Inclua um comentário no histórico");
				}
			}
		}
		
		Atividade atividadeAlterada = atividadeRepository.save(atividadeConsulta);

		if(atividadeAlterada != null) {
			//Lista de Interessados e Responsaveis
			this.mergeAtividadeUsuario(dto);
			//Excluir atividadeShort
			this.excluirAtividadesShort(dto.getAtividade());
			//Incluir AtividadeShort
			List<Atividade> atividades = new ArrayList<Atividade>();
			atividades.add(dto.getAtividade());
			this.incluirAtividadeShort(dto,atividades);
			//Agenda
			this.excluirAgenda(dto.getAtividade());
			this.incluirAtividadeAgenda(dto,atividades);
			//Andamento procesusal se exister o processo e for mudado para concluído
			if(atividadeAlterada.getStatus().getStatus().equals("Concluído") && atividadeAlterada.getProcesso() != null) {
				HistoricoProcesso histProc = new HistoricoProcesso();
				histProc.setProcesso(atividadeAlterada.getProcesso());
				histProc.setDataHistorico(DatasUtil.getDataAtual());
				histProc.setDataOcorrencia(DatasUtil.getDataAtual());
				histProc.setHistorico("O prazo: " + atividadeAlterada.getTitulo() + " foi concluído");
				Optional<TipoAndamentoProcessual> tap = andamentoRepository.findByNomeAndEmpresaCodigo("Conclusao Prazo", 
						usuarioConsulta.get().getEmpresa().getCodigo());
				histProc.setTipoAndamento(tap.get());
				histProc.setUsuario(usuarioConsulta.get());
				historicoProcessoRepository.save(histProc);
				//CheckList
				List<ExecucaoCheckList> checkList = checkListRepository.findByAtividadeCodigoOrderByCheckListOrdem(atividadeAlterada.getCodigo());
				if(checkList != null && checkList.size() > 0) {
					for(ExecucaoCheckList cl : checkList) {
						if(cl.getStatus().equals("A")) {
							ExecucaoCheckList exec = ExecucaoCheckList.builder()
									.codigo(cl.getCodigo())
									.atividade(cl.getAtividade())
									.checkList(cl.getCheckList())
									.usuario(usuarioConsulta.get())
									.dataExecucao(cl.getDataExecucao())
									.status("C")
									.build();
							checkListRepository.save(exec);			
						} else if(cl.getStatus().equals("F")) {
							ExecucaoCheckList exec = ExecucaoCheckList.builder()
									.codigo(cl.getCodigo())
									.atividade(cl.getAtividade())
									.checkList(cl.getCheckList())
									.usuario(cl.getUsuario())
									.dataExecucao(cl.getDataExecucao())
									.status("C")
									.build();
							checkListRepository.save(exec);			
						}
					}
				}
			}
			
			if(dto.getAtividade().getTipo().equals("A")) {
				if(!dataAntiga.equals(dto.getAtividade().getDtLimite())) {
					HistoricoAtividade historico = new HistoricoAtividade();
					historico.setAtividade(dto.getAtividade());
					Usuario usu = usuarioRepository.findById(usuario).get();
					usu.setCodigo(usuario);
					historico.setUsuario(usu);
					if(dto.getAtividade().getHora() != null && dto.getAtividade().getHora() != "") {
						historico.setDsHistorico("A data limite da atividade foi alterada de " +  DatasUtil.formatarDataHoraTela(dataAntiga) + " para " + DatasUtil.formatarDataHoraTela(dto.getAtividade().getDtLimite() + 
								" por " + usu.getNome()));
					} else {
						historico.setDsHistorico("A data limite da atividade foi alterada de " + DatasUtil.formatarDataTela(dataAntiga) + " para " + DatasUtil.formatarDataTela(dto.getAtividade().getDtLimite() + 
								" por " + usu.getNome()));
					}
					historico.setDtHistorico(DatasUtil.getDataAtual());
					historico.setTpHistorico("A");
					historicoAtividadeRepository.save(historico);
					
					//Inclui na Central
					for(Usuario resp : dto.getResponsaveis()) {
						if(!resp.getCodigo().equals(dto.getAtividade().getUsuario().getCodigo())) {
							List<Atividade> atividadesCentral = new ArrayList<Atividade>();
							atividadesCentral.add(dto.getAtividade());
							this.incluirAtividadeCentral(atividadesCentral, "A data limite da atividade foi alterada", resp);
						}
					}
					
					if(dto.getInteressados() != null) {
						for(Usuario inter : dto.getInteressados()) {
							if(!inter.getCodigo().equals(dto.getAtividade().getUsuario().getCodigo())) {
								List<Atividade> atividadesCentral = new ArrayList<Atividade>();
								atividadesCentral.add(dto.getAtividade());
								this.incluirAtividadeCentral(atividadesCentral, "A data limite da atividade foi alterada", inter);
							}
						}
					}
				}
			} else {
				if(!dataAntiga.equals(dto.getAtividade().getDtLimite())) {
					HistoricoAtividade historico = new HistoricoAtividade();
					historico.setAtividade(dto.getAtividade());
					Usuario usu = usuarioRepository.findById(usuario).get();
					usu.setCodigo(usuario);
					historico.setUsuario(usu);
					historico.setDsHistorico("A data limite da atividade foi alterada de " +  DatasUtil.formatarDataTela(dataAntiga) + " para " + DatasUtil.formatarDataTela(dto.getAtividade().getDtLimite() + 
							" por " + usu.getNome()));
					historico.setDtHistorico(DatasUtil.getDataAtual());
					historico.setTpHistorico("A");
					historicoAtividadeRepository.save(historico);
					
					//Inclui na Central
					for(Usuario resp : dto.getResponsaveis()) {
						if(!resp.getCodigo().equals(dto.getAtividade().getUsuario().getCodigo())) {
							List<Atividade> atividadesCentral = new ArrayList<Atividade>();
							atividadesCentral.add(dto.getAtividade());
							this.incluirAtividadeCentral(atividadesCentral, "A data limite da atividade foi alterada", resp);
						}
					}
					
					if(dto.getInteressados() != null) {
						for(Usuario inter : dto.getInteressados()) {
							if(!inter.getCodigo().equals(dto.getAtividade().getUsuario().getCodigo())) {
								List<Atividade> atividadesCentral = new ArrayList<Atividade>();
								atividadesCentral.add(dto.getAtividade());
								this.incluirAtividadeCentral(atividadesCentral, "A data limite da atividade foi alterada", inter);
							}
						}
					}
				}
			}
			
			if(statusAntigo != dto.getAtividade().getStatus().getCodigo()) {
				HistoricoAtividade historico = new HistoricoAtividade();
				historico.setAtividade(dto.getAtividade());
				Usuario usu = usuarioRepository.findById(usuario).get();
				usu.setCodigo(usuario);
				historico.setUsuario(usu);
				historico.setDsHistorico("O status da atividade foi alterado para " + dto.getAtividade().getStatus().getStatus() + " por " + usu.getNome());
				historico.setDtHistorico(DatasUtil.getDataAtual());
				historico.setTpHistorico("A");
				historicoAtividadeRepository.save(historico);

				//Inclui na Central
				for (Usuario resp : dto.getResponsaveis()) {
					if (!resp.getCodigo().equals(dto.getAtividade().getUsuario().getCodigo())) {
						List<Atividade> atividadesCentral = new ArrayList<Atividade>();
						atividadesCentral.add(dto.getAtividade());
						this.incluirAtividadeCentral(atividadesCentral, "O status da atividade foi alterado", resp);
					}
				}

				if (dto.getInteressados() != null) {
					for (Usuario inter : dto.getInteressados()) {
						if (!inter.getCodigo().equals(dto.getAtividade().getUsuario().getCodigo())) {
							List<Atividade> atividadesCentral = new ArrayList<Atividade>();
							atividadesCentral.add(dto.getAtividade());
							this.incluirAtividadeCentral(atividadesCentral, "O status da atividade foi alterado", inter);
						}
					}
				}
			}

			//Verifica se a atividade é do tipo lançamento e foi concluída
			if(atividadeConsulta.getTipo().equals("L") && statusConcluido.get().getStatus().equals("Concluído")) {
				//Da baixa no lançamento financeiro
				LancamentoDto lancamentoDto = new LancamentoDto();
				lancamentoDto.setCodigo(lancamento.getCodigo());
				lancamentoDto.setConta(ContaFinanceiraDto.build(contaFinanceira));
				lancamentoDto.setSituacao("P");
				lancamentoDto.setVlPago(lancamento.getVlLancamento());
				LancamentoDto lancamentoBaixado = lancamentoService.pagarOuCancelarLancamentoIndividual(lancamento.getCodigo(),lancamentoDto);
				if(lancamentoBaixado != null) {
					System.out.println("Lançamento baixado com sucesso");
				}
			}

			//Verifica se a atividade é do tipo lançamento e a data limite foi alterada, para poder alterar também a data de vencimento
			if(atividadeConsulta.getTipo().equals("L") &&
					!statusConcluido.get().getStatus().equals("Concluído") &&
					!dataAntiga.equals(dto.getAtividade().getDtLimite())) {

				//Muda a data de vencimento do lançamento
				LancamentoDto lancamentoDto = LancamentoDto.build(lancamento);
				lancamentoDto.setDtVencimento(dto.getAtividade().getDtLimite());
				lancamentoService.alterarLancamento(lancamentoDto);
			}
		}
		
		return atividadeAlterada;
	}
	
	private String listaResponsvaeisAtividade(long atividade) {
		String responsaveis = "";
		List<AtividadeUsuario> lista = atividadeUsuarioRepository.findByAtividadeCodigoAndTipoOrderByUsuarioNome(atividade, "R"); // Só os responsáveis
		for(AtividadeUsuario usuarios: lista) {
			responsaveis = responsaveis + usuarios.getUsuario().getNome() + ",";
		}
		
		return responsaveis;
	}
	
	public List<Atividade> consultarAtividadesPorProcesso(long processo) {
		List<Atividade> lista = atividadeRepository.findByProcessoCodigoOrderByDtLimiteDesc(processo);
		List<Atividade> listaAtividades = new ArrayList<Atividade>();
		for(Atividade ativ : lista) {
			Atividade atividade = new Atividade();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setTitulo(ativ.getTitulo());
			if(ativ.getTipo().equals("A")) {
				atividade.setDtLimite(DatasUtil.formatarDataHoraTela(ativ.getDtLimite()));
			} else {
				atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			}
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setGrupoPai(this.retornaGrupoTrabalhoPai(ativ.getSubGrupo().getGrupoPai()));
			atividade.setStatus(ativ.getStatus());
			if(ativ.getDtFatal() != null) {
				atividade.setDtFatal(DatasUtil.formatarDataTela(ativ.getDtFatal()));
			} else {
				atividade.setDtFatal(null);
			}
			atividade.setResponsaveis(this.listaResponsvaeisAtividade(ativ.getCodigo()));
			listaAtividades.add(atividade);
		}
		
		return listaAtividades;
	}
	
	public int atividadesCriadasUsuario(long usuario) {
		Optional<Usuario> consultaUsuario = usuarioRepository.findById(usuario);
		if(!consultaUsuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		return atividadeRepository.atividadesTotalUsuario(usuario);
	}
	
	public int atividadesUltimosTrintaDiasUsuario(long usuario) {
		Optional<Usuario> consultaUsuario = usuarioRepository.findById(usuario);
		if(!consultaUsuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		return atividadeRepository.atividadeUltimosTrintaDias(usuario);
	}
	
	public int atividadesConcluidasUltimosTrintaDiasUsuario(long usuario) {
		Optional<Usuario> consultaUsuario = usuarioRepository.findById(usuario);
		if(!consultaUsuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		return atividadeRepository.atividadesConcluidasUltimasTrintaDias(usuario);
	}
	
	public int atividadesConcluidasAtrasdasUltimosTrintaDiasUsuario(long usuario) {
		Optional<Usuario> consultaUsuario = usuarioRepository.findById(usuario);
		if(!consultaUsuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		return atividadeRepository.atividadesConcluidasComAtrasoUltimosTrintaDias(usuario);
	}
	
	public int atividadesConcluidasNoPrazoUltimosTrintaDiasUsuario(long usuario) {
		Optional<Usuario> consultaUsuario = usuarioRepository.findById(usuario);
		if(!consultaUsuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		return atividadeRepository.atividadesConsluidasNoPrazoUltimosTrintaDias(usuario);
	}
	
	private PontuacaoAtividade consultarPontuacao(long codigo) {
		return pontuacaoRepository.findById(codigo).get();
	}
	
	public double retornaPontuacao(long responsavel, long interesse, long agenda) {
		double quantidade = 0;
		PontuacaoAtividade pontuacao = this.consultarPontuacao(2);
		if(pontuacao != null) {
			quantidade = (((responsavel * pontuacao.getResponsavel()) / 100) +
					((interesse * pontuacao.getInteresse()) / 100) + ((agenda * pontuacao.getAgenda()) / 100));
		}
		return quantidade;
	}
	
	//Kanban
	public List<AtividadeShort> painelKanban(FiltroKanban filtro) {
		List<AtividadeShort> listaAtividades = new ArrayList<AtividadeShort>();
		List<AtividadeShort> atividades = atividadeRepository.painelKanban(filtro);
		for(AtividadeShort ativ : atividades) {
			AtividadeShort atividade = new AtividadeShort();
			atividade.setCodigo(ativ.getCodigo());
			atividade.setCodigoAtividade(ativ.getCodigoAtividade());
			atividade.setTitulo(ativ.getTitulo());
			atividade.setDtLimite(DatasUtil.formatarDataTela(ativ.getDtLimite()));
			atividade.setSubGrupo(ativ.getSubGrupo());
			atividade.setStatus(ativ.getStatus());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setGrupo(ativ.getGrupo());
			atividade.setStatus(ativ.getStatus());
			if(ativ.getTipoAtividade().equals("L")) {
				atividade.setSnFinanceiro("block");
			} else {
				atividade.setSnFinanceiro("none");
			}
			if(ativ.getDtfatal() != null) {
				atividade.setDtfatal(DatasUtil.formatarDataTela(ativ.getDtfatal()));
			} else {
				atividade.setDtfatal(null);
			}
			atividade.setCodigoProcesso(ativ.getCodigoProcesso());
			String cnjMascarado = "";
			if(ativ.getCnj() != null && ativ.getCnj() != "" && ativ.getCnj() != " ") {
				cnjMascarado = ativ.getCnj().substring(0, 7);
				cnjMascarado = cnjMascarado + "-" + ativ.getCnj().substring(7, 9);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(9, 13);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(13, 14);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(14, 16);
				cnjMascarado = cnjMascarado + "." + ativ.getCnj().substring(16, 20);
			}
			atividade.setCnj(cnjMascarado);
			atividade.setPasta(ativ.getPasta());
			atividade.setNrprocesso(ativ.getNrprocesso());
			atividade.setPartes(ativ.getPartes());
			atividade.setInteressados(ativ.getInteressados());
			atividade.setResponsaveis(ativ.getResponsaveis());
			atividade.setDescricaoStatus(ativ.getDescricaoStatus());
			atividade.setFavorito(ativ.getFavorito());
			atividade.setImportante(ativ.getImportante());
			atividade.setFase(ativ.getFase());
			listaAtividades.add(atividade);
		}
		
		return listaAtividades;
	}
	
	public long alterarMassa(AlterarAtividadeMassaDto alterar) {
		long alterou = 0;
		if(alterar.getAtividades() != null && alterar.getAtividades().size() > 0) {
			for(Atividade ativ : alterar.getAtividades()) {
				Optional<Atividade> atividadeEntity = atividadeRepository.findById(ativ.getCodigo());
				Atividade atividade = atividadeEntity.get();
				StatusAtividade statusAntigo = atividadeEntity.get().getStatus();
				String hora = "";
				String dataConsulta = "";
				boolean mudancaData = false;
				if(alterar.getDataLimite() != null) {
					dataConsulta = DatasUtil.formatarDataBancoOriginalHora(atividadeEntity.get().getDtLimite());
					if(atividade.getTipo().equals("A")) {
						hora = DatasUtil.formatarHoraTela(atividade.getDtLimite());
						if(!alterar.getDataLimite().equals(dataConsulta)) {
							mudancaData = true;
							atividade.setDtLimite(alterar.getDataLimite() + " " + hora + ":00");
						}
					} else {
						if(!dataConsulta.equals(alterar.getDataLimite())) {
							mudancaData = true;
							atividade.setDtLimite(alterar.getDataLimite());
						}
					}
				}
				
				if(alterar.getStatus() != null) {
					atividade.setStatus(alterar.getStatus());
					//Se a mudança for para concluído, muda a data para conclusão
					if(alterar.getStatus().getCodigo() == 5) {
						atividade.setDtConcluido(DatasUtil.getDataAtual());
					} else {
						atividade.setDtConcluido(null);
					}
				}
				
				Atividade atividadeAlterada = atividadeRepository.save(atividade);
				
				if(atividadeAlterada != null) {
					alterou++;
					//O histórico imputado pelo usuário
					if(alterar.getHistorico() != null && !alterar.getHistorico().equals("")) {
						HistoricoAtividade historico = new HistoricoAtividade();
						historico.setAtividade(atividadeAlterada);
						historico.setDsHistorico(alterar.getHistorico());
						historico.setDtHistorico(DatasUtil.getDataAtual());
						historico.setTpHistorico("A");
						historico.setUsuario(alterar.getUsuario());
						historicoAtividadeRepository.save(historico);
					}
					
					//Mudaça de Status
					Usuario usu = usuarioRepository.findById(alterar.getUsuario().getCodigo()).get();
					if(statusAntigo.getCodigo() != atividade.getStatus().getCodigo()) {
						HistoricoAtividade historico = new HistoricoAtividade();
						historico.setAtividade(atividade);
						historico.setUsuario(alterar.getUsuario());
						Optional<StatusAtividade> statusConsulta = statusAtividadeRepository.findById(alterar.getStatus().getCodigo());
						historico.setDsHistorico("O status da atividade foi alterado para " + statusConsulta.get().getStatus() + " por " + usu.getNome());
						historico.setDtHistorico(DatasUtil.getDataAtual());
						historico.setTpHistorico("A");
						historicoAtividadeRepository.save(historico);
					}
					
					//Mudanças de Datas
					if(alterar.getDataLimite() != null) {
						if(mudancaData) {
							if(atividadeAlterada.getTipo().equals("A")) {
								HistoricoAtividade historico = new HistoricoAtividade();
								historico.setAtividade(atividade);
								historico.setUsuario(alterar.getUsuario());
								historico.setDsHistorico("A data limite da atividade foi alterada para " + DatasUtil.formatarDataHoraTela(atividadeAlterada.getDtLimite() + 
										" por " + usu.getNome()));
								historico.setDtHistorico(DatasUtil.getDataAtual());
								historico.setTpHistorico("A");
								historicoAtividadeRepository.save(historico);
								
								//Inclui na Central
								List<AtividadeUsuario> usuariosAtividade = atividadeUsuarioRepository.findByAtividadeCodigo(atividade.getCodigo());
								for(AtividadeUsuario usuAtiv : usuariosAtividade) {
									if(usuAtiv.getUsuario().getCodigo() != alterar.getUsuario().getCodigo()) {
										List<Atividade> atividades = new ArrayList<Atividade>();
										atividades.add(atividade);
										this.incluirAtividadeCentral(atividades, "A data limite da atividade foi alterada", usuAtiv.getUsuario());
									}
								}
							} else {
								HistoricoAtividade historico = new HistoricoAtividade();
								historico.setAtividade(atividade);
								historico.setUsuario(alterar.getUsuario());
								historico.setDsHistorico("A data limite da atividade foi alterada para " + DatasUtil.formatarDataTela(atividadeAlterada.getDtLimite() + 
										" por " + usu.getNome()));
								historico.setDtHistorico(DatasUtil.getDataAtual());
								historico.setTpHistorico("A");
								historicoAtividadeRepository.save(historico);
								
								//Inclui na Central
								List<AtividadeUsuario> usuariosAtividade = atividadeUsuarioRepository.findByAtividadeCodigo(atividade.getCodigo());
								for(AtividadeUsuario usuAtiv : usuariosAtividade) {
									if(usuAtiv.getUsuario().getCodigo() != alterar.getUsuario().getCodigo()) {
										List<Atividade> atividades = new ArrayList<Atividade>();
										atividades.add(atividade);
										this.incluirAtividadeCentral(atividades, "A data limite da atividade foi alterada", usuAtiv.getUsuario());
									}
								}
							}
						}
					}
					
					
					List<AtividadeUsuario> usuariosAtividade = atividadeUsuarioRepository.findByAtividadeCodigo(atividade.getCodigo());
					AtividadeCadastroDTO dto = new AtividadeCadastroDTO();
					dto.setAtividade(atividadeAlterada);
					List<Usuario> responsaveis = new ArrayList<Usuario>();
					List<Usuario> interessados = new ArrayList<Usuario>();
					for(AtividadeUsuario usua : usuariosAtividade) {
						if(usua.getTipo().equals("R")) {
							responsaveis.add(usua.getUsuario());
						} else {
							interessados.add(usua.getUsuario());
						}
					}
					dto.setResponsaveis(responsaveis);
					dto.setInteressados(interessados);
					
					this.excluirAtividadesShort(dto.getAtividade());
					//Incluir AtividadeShort
					List<Atividade> atividades = new ArrayList<Atividade>();
					atividades.add(dto.getAtividade());
					this.incluirAtividadeShort(dto,atividades);
					//Agenda
					this.excluirAgenda(dto.getAtividade());
					this.incluirAtividadeAgenda(dto,atividades);
				}
			}
		}
		
		return alterou;
	}

	public void cadastrarAtividadeAgrupada(AtividadeAgrupadaDto atividadeDto) {
		if(atividadeDto.getTarefas() == null || atividadeDto.getTarefas().size() == 0) {
			throw new EntidadeNaoEncontradaException("Lista de tarefas vazia");
		}
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		for(TarefaDto ativ : atividadeDto.getTarefas()) {
			Atividade atividade = new Atividade();
			atividade.setTitulo(ativ.getTitulo());
			Date dataAtividade = null;
			try {
				dataAtividade = formato.parse(atividadeDto.getDataLimite());
			} catch( Exception e ) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataAtividade);
			if(ativ.getTempo().equals("Progressiva")) {
				calendar.add(Calendar.DATE,(int) ativ.getPrazo());
			} else {
				calendar.add(Calendar.DATE,(int) - ativ.getPrazo());
			}
			dataAtividade = calendar.getTime();
			atividade.setDtLimite(formato.format(dataAtividade));
			atividade.setDtConcluido(null);
			atividade.setDtRegistro(DatasUtil.getDataAtual());
			atividade.setProcesso(atividadeDto.getProcesso());
			atividade.setSubGrupo(atividadeDto.getGrupo());
			atividade.setUsuario(atividadeDto.getUsuario());
			atividade.setDescricao(atividadeDto.getDescricao());
			//Cria inicialmente com o status de criada
			Optional<StatusAtividade> statusAtividade =
					statusAtividadeRepository.findByStatusAndEmpresaCodigo("Criada",atividadeDto.getUsuario().getEmpresa().getCodigo());
			if(!statusAtividade.isPresent()) {
				throw new EntidadeNaoEncontradaException("Erro de configuração. Status de atividade não encontrada");
			}
			atividade.setStatus(statusAtividade.get());
			atividade.setTipo("T"); // Tarefa
			Atividade atividadeCriada = atividadeRepository.save(atividade);
			if(atividadeCriada != null) {
				List<Usuario> responsaveis = new ArrayList<>();
				AtividadeUsuario atividadeUsuario = new AtividadeUsuario();
				atividadeUsuario.setAtividade(atividadeCriada);
				atividadeUsuario.setUsuario(ativ.getResponsavel());
				atividadeUsuario.setTipo(this.USUARIO_TIPO_RESPONSAVEL);
				AtividadeUsuario atividadeUsuarioCadastrado = atividadeUsuarioRepository.save(atividadeUsuario);
				if(atividadeUsuarioCadastrado != null) {
					responsaveis.add(atividadeUsuarioCadastrado.getUsuario());
					/*Se este usuário não foi adicionado no ato do cadastro, se já é pertecente ao rodízio, será relocado para
					  para a última posição do ranking
					 */
					this.reordenarRodizio(atividadeDto.getGrupoTarefa(),ativ.getCodigo());

					//Atividade Short
					AtividadeCadastroDTO atividadeShort = new AtividadeCadastroDTO();
					atividadeShort.setAtividade(atividadeCriada);
					atividadeShort.setResponsaveis(responsaveis);
					atividadeShort.setInteressados(null);
					List<Atividade> atividades = new ArrayList<Atividade>();
					atividades.add(atividadeCriada);
					this.incluirAtividadeShort(atividadeShort,atividades);

					//Incluir na Agenda
					this.incluirAtividadeAgenda(atividadeShort,atividades);

					//Histórico de atividades
					HistoricoAtividadeDTO historicoDTO = new HistoricoAtividadeDTO();
					historicoDTO.setAtividade(atividadeCriada);
					historicoDTO.setUsuario(atividadeDto.getUsuario());
					Optional<Usuario> usu = usuarioRepository.findById(atividadeDto.getUsuario().getCodigo());
					historicoDTO.setDsHistorico("Atividade incluída pelo usuário: " + usu.get().getNome());
					historicoDTO.setDtHistorico(DatasUtil.getDataAtual());
					historicoDTO.setTpHistorico("A"); // Automático
					historicoAtividadeRepository.save(historicoDTO.transformeParaObjeto());
				}
			}
		}
	}

	private void reordenarRodizio(long grupoAtividade, long atividade) {
		List<RodizioUsuarioAtividade> rodizio = rodizioUsuarioAtividadeRepository.findByGrupoAtividadeCodigoAndAtividadeCodigoOrderByPosicao(
				grupoAtividade,atividade
		);

		List<RodizioUsuarioAtividade> r = new ArrayList<>();
		int ultima = rodizio.size() -1;
		long pos = rodizio.get(ultima).getPosicao();
		for(RodizioUsuarioAtividade rod : rodizio) {
			RodizioUsuarioAtividade rr = new RodizioUsuarioAtividade();
			rr.setAtividade(rod.getAtividade());
			rr.setPosicao(pos);
			rr.setCodigo(rod.getCodigo());
			rr.setGrupoAtividade(rod.getGrupoAtividade());
			rr.setGrupoTrabalho(rod.getGrupoTrabalho());
			rr.setDataAlteracao(rod.getDataAlteracao());
			rr.setUsuarioAtividade(rod.getUsuarioAtividade());
			r.add(rr);
			if(pos == rodizio.get(ultima).getPosicao()) {
				pos = 1;
			} else {
				pos++;
			}
		}

		for(RodizioUsuarioAtividade rod2 : rodizio) {
			rodizioUsuarioAtividadeRepository.deleteById(rod2.getCodigo());
		}

		for(RodizioUsuarioAtividade rod3 : r) {
			RodizioUsuarioAtividade novo = RodizioUsuarioAtividade.builder()
					.atividade(rod3.getAtividade())
					.grupoAtividade(rod3.getGrupoAtividade())
					.grupoTrabalho(rod3.getGrupoTrabalho())
					.dataAlteracao(DatasUtil.getDataAtual())
					.posicao(rod3.getPosicao())
					.usuarioAtividade(rod3.getUsuarioAtividade())
					.build();
			rodizioUsuarioAtividadeRepository.save(novo);
		}
	}
	
	private String retornaResponsaveis(long atividade) {
		List<AtividadeUsuario> atividadeUsuarios = atividadeUsuarioRepository.findByAtividadeCodigoAndTipoOrderByUsuarioNome(atividade, "R");
		String nome = "";
		if(atividadeUsuarios != null && atividadeUsuarios.size() > 0) {
			for(AtividadeUsuario au : atividadeUsuarios) {
				nome = nome + au.getUsuario().getNome() + ",";
			}
		}
		
		return nome;
	}
	
	@SuppressWarnings("unused")
	private String retornaGrupoTrabalho(long codigo) {
		Optional<GrupoTrabalho> grupo = grupoTrabalhoRepository.findById(codigo);
		if(grupo.isPresent()) {
			return grupo.get().getNome();
		}
		
		return null;
	}

	public List<AtividadesGeralHomeDto> consultarAtividadesGeralHome(FiltroMiniAgenda filtro) {
		List<AtividadesGeralHomeDto> atividades = atividadeRepository.consultarAtividadesGeralHome(filtro);
		List<AtividadesGeralHomeDto> dtos = new ArrayList<>();
		if(atividades != null && atividades.size() > 0) {
			for(AtividadesGeralHomeDto ativ : atividades) {
				dtos.add(AtividadesGeralHomeDto.build(ativ));
			}
		}
		return dtos;
	}
	
	public List<MiniAgendaDto> consultarAgendaMesUsuario(FiltroAgenda filtro) {
		List<MiniAgendaDto> lista = atividadeRepository.agendaPorMesUsuario(filtro);
		return lista;
	}
	
	private String retornaPartes(long processo, String parte) {
		List<Partes> partes = partesRepository.findByProcessoCodigoAndTipoParteOrderByPessoaNomeAsc(processo, parte);
		String nome = "";
		if(partes != null && partes.size() > 0) {
			for(Partes p : partes) {
				nome = nome + p.getPessoa().getNome() + ",";
			}
		}
		
		return nome;
	}
	
	private String retornaPartesTodos(long processo) {
		List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo);
		String nome = "";
		if(partes != null && partes.size() > 0) {
			for(Partes p : partes) {
				nome = nome + p.getPessoa().getNome() + ", ";
			}
		}
		
		return nome;
	}

	private void incluirHistoricoFaseProcessual(Atividade atividade) {
		if(atividade.getProcesso() != null) {
			Optional<Processo> processo = processoRepository.findById(atividade.getProcesso().getCodigo());
			Fase fase = null;
			if(processo.get().getFase() == null) {
				Optional<Usuario> usuario = usuarioRepository.findById(atividade.getUsuario().getCodigo());
				Optional<Fase> faseEntity = faseRepository.findByNomeAndEmpresaCodigo("Sem Fase",usuario.get().getEmpresa().getCodigo());
				fase = faseEntity.get();
			} else {
				fase = processo.get().getFase();
			}
			HistoricoFaseProcessual historico = HistoricoFaseProcessual.builder()
					.processo(atividade.getProcesso())
					.atividade(atividade)
					.fase(fase)
					.build();
			historicoFaseProcesualRepository.save(historico);
		}
	}

	public List<HistoricoFaseProcessualViewDto> consultarHistoricoFaseProcssual(FiltroHistoricoAtividadeFase filtro, long usuario) {
		List<HistoricoFaseProcessualViewDto> historicos = historicoFaseProcesualRepository.consultarAtividadesFases(filtro);
		List<HistoricoFaseProcessualViewDto> dtos = new ArrayList<>();
		if(historicos != null && historicos.size() > 0) {
			for(HistoricoFaseProcessualViewDto dto : historicos) {
				if(exibeAtividadePrivada(dto.getCodigo(),usuario)) {
					dtos.add(HistoricoFaseProcessualViewDto.build(dto));
				}
			}
		}

		return dtos;
	}

	public List<GraficoAtividadeFaseDto> graficoFase(long processo) {
		List<GraficoAtividadeFaseDto> grafico = historicoFaseProcesualRepository.graficoFaseProcessualAtividade(processo);
		List<GraficoAtividadeFaseDto> dtos = new ArrayList<>();
		if(grafico != null && grafico.size() > 0) {
			for(GraficoAtividadeFaseDto graf : grafico) {
				GraficoAtividadeFaseDto dto = new GraficoAtividadeFaseDto();
				dto.setFase(graf.getFase());
				dto.setTotal(graf.getTotal());
				dto.setCor(graf.getCor());
				dtos.add(dto);
			}
		}

		return dtos;
	}

	public List<GraficoAtividadesStatusDto> graficoStatus(long processo) {
		List<GraficoAtividadesStatusDto> grafico = historicoFaseProcesualRepository.graficoStatusAtividade(processo);
		List<GraficoAtividadesStatusDto> dtos = new ArrayList<>();
		if(grafico != null && grafico.size() > 0) {
			for(GraficoAtividadesStatusDto graf : grafico) {
				GraficoAtividadesStatusDto dto = new GraficoAtividadesStatusDto();
				dto.setStatus(graf.getStatus());
				dto.setTotal(graf.getTotal());
				dto.setCor(graf.getCor());
				dtos.add(dto);
			}
		}

		return dtos;
	}

	public AtividadeCustomDto consultarAtiviadeCustom(long codigo,long empresa, long usuariotiv) {
		AtividadeCustomDto atividade = atividadeRepository.consultarAtividadeCustom(codigo,empresa);
		if(atividade.getCodigoPessoa() != null) {
			Optional<Pessoa> pessoa = pessoaRepository.findById(Long.valueOf(atividade.getCodigoPessoa()));
			atividade.setPessoa(pessoa.get());
		}
		if(atividade != null) {
			//Responsáveis
			List<AtividadeUsuario> atividadesResponsaveis = atividadeUsuarioRepository.findByAtividadeCodigoAndTipoOrderByUsuarioNome(atividade.getCodigo(),"R");
			List<Usuario> responsaveis = new ArrayList<>();
			for(AtividadeUsuario au : atividadesResponsaveis) {
				Usuario usuario = usuarioRepository.findById(au.getUsuario().getCodigo()).get();
				responsaveis.add(usuario);
			}
			atividade.setResponsaveisAtividade(responsaveis);

			if(atividade.getProcesso() != null) {
				String cnjMascarado = "";
				if(atividade.getProcesso().length() == 20) {
					cnjMascarado = atividade.getProcesso().substring(0, 7);
					cnjMascarado = cnjMascarado + "-" + atividade.getProcesso().substring(7, 9);
					cnjMascarado = cnjMascarado + "." + atividade.getProcesso().substring(9, 13);
					cnjMascarado = cnjMascarado + "." + atividade.getProcesso().substring(13, 14);
					cnjMascarado = cnjMascarado + "." + atividade.getProcesso().substring(14, 16);
					cnjMascarado = cnjMascarado + "." + atividade.getProcesso().substring(16, 20);
				} else {
					cnjMascarado = atividade.getProcesso();
				}

				atividade.setProcesso(cnjMascarado);
			}

			//Interessados
			List<AtividadeUsuario> atividadesInteressados = atividadeUsuarioRepository.findByAtividadeCodigoAndTipoOrderByUsuarioNome(atividade.getCodigo(),"I");
			List<Usuario> interessaados = new ArrayList<>();
			if(atividadesInteressados != null && atividadesInteressados.size() > 0) {
				for (AtividadeUsuario au : atividadesInteressados) {
					Usuario usuario = usuarioRepository.findById(au.getUsuario().getCodigo()).get();
					interessaados.add(usuario);
				}
			}
			atividade.setInteressadosAtividade(interessaados);

			//Histórico das Atividades
			List<HistoricoAtividade> historico = historicoAtividadeRepository.findByAtividadeCodigoOrderByCodigoDesc(atividade.getCodigo());
			atividade.setHistorico(historico);
			//Usuários dos grupos, tanto para responsáveis quanto par interessados
			List<Usuario> usuariosGrupo = usuarioGrupoTrabalhoRepository.listarUsuariosGrupoAtivos(atividade.getCodigoGrupo());
			atividade.setUsuariosGrupo(usuariosGrupo);
			//Status da Atividade
			Optional<StatusAtividade> status = statusAtividadeRepository.findById(atividade.getStatusCodigo());
			atividade.setStatusEntty(status.get());

			if(atividade.getStatusEntty().getStatus().equals("Criada")) {
				//Alterar para ativo neste momento
				List<AtividadeUsuario> atividadeUsuario = atividadeUsuarioRepository.findByAtividadeCodigo(codigo);
				boolean usuarioIsResponsavel = false;
				for(AtividadeUsuario au : atividadeUsuario) {
					if(au.getUsuario().getCodigo() == usuariotiv && au.getTipo().equals("R")) {
						usuarioIsResponsavel = true;
					}
				}

				if(usuarioIsResponsavel) {
					Atividade ativ = new Atividade();
					ativ.setCodigo(atividade.getCodigo());
					Atividade atividadeLida = this.lerAtividade(ativ,usuariotiv);
					atividade.setStatusEntty(atividadeLida.getStatus());
				}
			}

			//Lista dos Status
			List<StatusAtividade> listaStatus = statusAtividadeRepository.findByEmpresaCodigo(empresa);
			atividade.setStatus(listaStatus);
		}

		return atividade;
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

	public List<DiasCalendario> diasCalendario(long mes, long ano) {
		List<DiasCalendario> dias = atividadeRepository.dias(mes,ano);
		return dias;
	}
}
