package com.br.ilawgestao.domains.dto;

import java.util.List;


import com.br.ilawgestao.domains.models.AreaAtuacao;
import com.br.ilawgestao.domains.models.ArquivoProcesso;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.CustasProcesso;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.Fase;
import com.br.ilawgestao.domains.models.GarantiaProcesso;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.ObjetoAcaoProcesso;
import com.br.ilawgestao.domains.models.PagamentoProcesso;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.Rito;
import com.br.ilawgestao.domains.models.StatusProcessual;
import com.br.ilawgestao.domains.models.TipoAcao;
import com.br.ilawgestao.domains.models.TipoDecisao;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.utils.DatasUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessoDto {	
	private long codigo;	
	private GrupoTrabalho grupoTrabalho;
	private String pasta;
	private String nrProcesso;
	private String nrCnj;
	private String nrInstancia;
	private String dsComarca;
	private TipoAcao tipoAcao;
	private StatusProcessual statusProcessual;
	private AreaAtuacao areaAtuacao;
	private TipoDecisao tipoDecisao;
	private String dataDistribuicao;
	private String dataUltimaDecisao;
	private double vlProvavel;
	private double vlPossivel;
	private double vlRemoto;
	private double vlCausa;
	private String dsPedidos;
	private String observacao;
	private Usuario usuario;
	private Usuario responsavel;
	private long statusInterno;
	private String dataCadastro;
	private String snPush;
	private String snHistorico;
	private String snEmail;
	private long status;
	private Empresa empresa;
	private String dataUltimaMovimentacao;
	private String dataSentenca;
	private String snImportante;
	private List<PartesDto> partes;
	private String montaPartes;
	private List<ObjetoAcaoProcesso> objetos;
	private List<PagamentoProcesso> pagamentos;
	private List<CustasProcesso> custas;
	private List<GarantiaProcesso> garantias;
	private List<HistoricoProcessoDto> historico;
	private List<Atividade> atividades;
	private List<GraficoAtividadeFaseDto> graficoAtividadesFase;
	private List<GraficoAtividadesStatusDto> graficoAtividadesStatus;
	private List<ArquivoProcesso> arquivos;
	private List<LancamentoDto> lancamentos;
	private List<PedidoProcessoDto> pedidos;
	private String importanteParaMim;
	private String importanteParaEmpresa;
	private String tipoContingenciaContabil;
	private String action;
	private MotivoResultadoDto motivoDto;
	private List<AgravoApensoDto> agravosApensos;
	private ProcessoDto processoPrincipal;
	private Rito rito;
	private Fase fase;
	private String uf;
	private String estrategico;
	private String orgaoColegiado;
	private String relator;
	private String agravoApenso;
	private String partesDesc;
	private Pessoa bancada;
	private List<HistoricoFaseProcessualViewDto> historicoFases;
	private String dataArquivamento;
	private String dataExclusao;
	
	public static ProcessoDto build(Processo processo) {	
		ProcessoDto dto = new ProcessoDto();
		dto.setCodigo(processo.getCodigo());
		dto.setGrupoTrabalho(processo.getGrupoTrabalho());
		dto.setPasta(processo.getPasta());
		dto.setNrProcesso(processo.getNrProcesso());
		dto.setNrCnj(processo.getNrCnj());
		dto.setNrInstancia(processo.getNrInstancia());
		dto.setDsComarca(processo.getDsComarca());
		dto.setTipoAcao(processo.getTipoAcao());
		dto.setStatusProcessual(processo.getStatusProcessual());
		dto.setAreaAtuacao(processo.getAreaAtuacao());
		dto.setTipoDecisao(processo.getTipoDecisao());
		dto.setDataDistribuicao(processo.getDataDistribuicao());
		dto.setDataUltimaDecisao(processo.getDataUltimaDecisao());
		dto.setVlProvavel(processo.getVlProvavel());
		dto.setVlPossivel(processo.getVlPossivel());
		dto.setVlRemoto(processo.getVlRemoto());
		dto.setVlCausa(processo.getVlCausa());
		dto.setDsPedidos(processo.getDsPedidos());
		dto.setObservacao(processo.getObservacao());
		dto.setUsuario(processo.getUsuario());
		dto.setResponsavel(processo.getResponsavel());
		dto.setStatusInterno(processo.getStatusInterno());
		dto.setDataCadastro(processo.getDataCadastro());
		dto.setSnPush(processo.getSnPush());
		dto.setSnEmail(processo.getSnEmail());
		dto.setSnHistorico(processo.getSnHistorico());
		dto.setStatus(processo.getStatus());
		dto.setEmpresa(processo.getEmpresa());
		dto.setDataUltimaMovimentacao(processo.getDataUltimaMovimentacao());
		dto.setSnImportante(processo.getSnImportante());
		dto.setDataSentenca(processo.getDataSentenca());
		dto.setTipoContingenciaContabil(processo.getTipoContingenciaContabil());
		if(processo.getMotivoResultado() != null) {
			dto.setMotivoDto(MotivoResultadoDto.build(processo.getMotivoResultado()));
		} else {
			dto.setMotivoDto(null);
		}
		if(processo.getFase() != null) {
			dto.setFase(processo.getFase());
		} else {
			dto.setFase(null);
		}
		if(processo.getRito() != null) {
			dto.setRito(processo.getRito());
		} else {
			dto.setRito(null);
		}
		dto.setUf(processo.getUf());
		dto.setEstrategico(processo.getEstrategico());
		dto.setOrgaoColegiado(processo.getOrgaoColegiado());
		dto.setRelator(processo.getRelator());
		dto.setBancada(processo.getBancada());
		return dto;
	}
	
	public static ProcessoDto buildConsulta(Processo processo, List<PartesDto> partes, List<ObjetoAcaoProcesso> objetos,
			List<PedidoProcessoDto> pedidos, List<PagamentoProcesso> pagamentos, List<CustasProcesso> custas,
			List<GarantiaProcesso> garantias, List<HistoricoProcessoDto> historico, List<Atividade> atividades,
			List<ArquivoProcesso> arquivos, List<LancamentoDto> lancamentos, List<AgravoApensoDto> agravosApensos, 
			ProcessoDto processoPrincipal, List<GraficoAtividadeFaseDto> graficoAtividadesFase, 
			List<GraficoAtividadesStatusDto> graficoAtividadesStatus, List<HistoricoFaseProcessualViewDto> historicoFases) {
		
		ProcessoDto dto = new ProcessoDto();
		dto.setCodigo(processo.getCodigo());
		dto.setGrupoTrabalho(processo.getGrupoTrabalho());
		dto.setPasta(processo.getPasta());
		dto.setNrProcesso(processo.getNrProcesso());
		dto.setNrCnj(processo.getNrCnj());
		dto.setNrInstancia(processo.getNrInstancia());
		dto.setDsComarca(processo.getDsComarca());
		dto.setTipoAcao(processo.getTipoAcao());
		dto.setStatusProcessual(processo.getStatusProcessual());
		dto.setAreaAtuacao(processo.getAreaAtuacao());
		dto.setTipoDecisao(processo.getTipoDecisao());
		dto.setDataDistribuicao(processo.getDataDistribuicao());
		dto.setDataUltimaDecisao(processo.getDataUltimaDecisao());
		dto.setVlProvavel(processo.getVlProvavel());
		dto.setVlPossivel(processo.getVlPossivel());
		dto.setVlRemoto(processo.getVlRemoto());
		dto.setVlCausa(processo.getVlCausa());
		dto.setDsPedidos(processo.getDsPedidos());
		dto.setObservacao(processo.getObservacao());
		dto.setUsuario(processo.getUsuario());
		dto.setResponsavel(processo.getResponsavel());
		dto.setStatusInterno(processo.getStatusInterno());
		dto.setDataCadastro(processo.getDataCadastro());
		dto.setSnPush(processo.getSnPush());
		dto.setSnEmail(processo.getSnEmail());
		dto.setSnHistorico(processo.getSnHistorico());
		dto.setStatus(processo.getStatus());
		dto.setEmpresa(processo.getEmpresa());
		dto.setDataUltimaMovimentacao(processo.getDataUltimaMovimentacao());
		dto.setSnImportante(processo.getSnImportante());
		dto.setEstrategico(processo.getEstrategico());
		dto.setDataSentenca(processo.getDataSentenca());
		dto.setPartes(partes);
		dto.setPedidos(pedidos);
		dto.setObjetos(objetos);
		dto.setPagamentos(pagamentos);
		dto.setCustas(custas);
		dto.setGarantias(garantias);
		dto.setHistorico(historico);
		dto.setAtividades(atividades);
		dto.setArquivos(arquivos);
		dto.setLancamentos(lancamentos);
		dto.setTipoContingenciaContabil(processo.getTipoContingenciaContabil());
		if(processo.getMotivoResultado() != null) {
			dto.setMotivoDto(MotivoResultadoDto.build(processo.getMotivoResultado()));
		} else {
			dto.setMotivoDto(null);
		}
		if(processo.getFase() != null) {
			dto.setFase(processo.getFase());
		} else {
			dto.setFase(null);
		}
		if(processo.getRito() != null) {
			dto.setRito(processo.getRito());
		} else {
			dto.setRito(null);
		}
		dto.setAgravosApensos(agravosApensos);
		dto.setProcessoPrincipal(processoPrincipal);
		dto.setUf(processo.getUf());
		dto.setGraficoAtividadesFase(graficoAtividadesFase);
		dto.setGraficoAtividadesStatus(graficoAtividadesStatus); 
		dto.setOrgaoColegiado(processo.getOrgaoColegiado());
		dto.setRelator(processo.getRelator());
		dto.setBancada(processo.getBancada());
		dto.setHistoricoFases(historicoFases);
		return dto;
	}
	
	public static ProcessoDto buildSimples(Processo processo, List<PartesDto> partes) {
		ProcessoDto dto = new ProcessoDto();
		dto.setCodigo(processo.getCodigo());
		dto.setNrCnj(mascaraProcessoCnj(processo.getNrCnj()));
		dto.setNrProcesso(processo.getNrProcesso());
		dto.setPartes(partes);
		return dto;
	}
	
	public static ProcessoDto buildAgravoApenso(Processo processo, List<PartesDto> partes) {
		ProcessoDto dto = new ProcessoDto();
		dto.setCodigo(processo.getCodigo());
		dto.setGrupoTrabalho(processo.getGrupoTrabalho());
		dto.setPasta(processo.getPasta());
		dto.setNrProcesso(processo.getNrProcesso());
		dto.setNrCnj(processo.getNrCnj());
		dto.setNrInstancia(processo.getNrInstancia());
		dto.setDsComarca(processo.getDsComarca());
		dto.setTipoAcao(processo.getTipoAcao());
		dto.setStatusProcessual(processo.getStatusProcessual());
		dto.setAreaAtuacao(processo.getAreaAtuacao());
		dto.setTipoDecisao(processo.getTipoDecisao());
		dto.setDataDistribuicao(processo.getDataDistribuicao());
		dto.setDataUltimaDecisao(processo.getDataUltimaDecisao());
		dto.setVlProvavel(processo.getVlProvavel());
		dto.setVlPossivel(processo.getVlPossivel());
		dto.setVlRemoto(processo.getVlRemoto());
		dto.setVlCausa(processo.getVlCausa());
		dto.setDsPedidos(processo.getDsPedidos());
		dto.setObservacao(processo.getObservacao());
		dto.setUsuario(processo.getUsuario());
		dto.setResponsavel(processo.getResponsavel());
		dto.setStatusInterno(processo.getStatusInterno());
		dto.setDataCadastro(processo.getDataCadastro());
		dto.setSnPush(processo.getSnPush());
		dto.setSnEmail(processo.getSnEmail());
		dto.setSnHistorico(processo.getSnHistorico());
		dto.setStatus(processo.getStatus());
		dto.setEmpresa(processo.getEmpresa());
		dto.setDataUltimaMovimentacao(processo.getDataUltimaMovimentacao());
		dto.setSnImportante(processo.getSnImportante());
		dto.setDataSentenca(processo.getDataSentenca());
		dto.setPartes(partes);
		dto.setEstrategico(processo.getEstrategico());
		return dto;
	}
	
	public static ProcessoDto buildConsultaApenso(Processo processo) {
		ProcessoDto dto = new ProcessoDto();
		dto.setCodigo(processo.getCodigo());
		if(processo.getNrCnj() != null) {
			dto.setNrCnj(mascaraProcessoCnj(processo.getNrCnj()));
		}
		dto.setNrProcesso(processo.getNrProcesso());
		return dto;
	}
	
	public static String mascaraProcessoCnj(String cnj) {
		if(cnj != null) {
			if(cnj.length() == 20) {
				String cnjMascarado = cnj.substring(0, 7);
				cnjMascarado = cnjMascarado + "-" + cnj.substring(7, 9);
				cnjMascarado = cnjMascarado + "." + cnj.substring(9, 13);
				cnjMascarado = cnjMascarado + "." + cnj.substring(13, 14);
				cnjMascarado = cnjMascarado + "." + cnj.substring(14, 16);
				cnjMascarado = cnjMascarado + "." + cnj.substring(16, 20);
				return cnjMascarado;
			}
		}
		
		return cnj;
	}
	
	public static Processo build(ProcessoDto dto) {
		Processo processo = new Processo();
		processo.setCodigo(dto.getCodigo());
		processo.setNrCnj(dto.getNrCnj());
		processo.setNrProcesso(dto.getNrProcesso());
		processo.setGrupoTrabalho(dto.getGrupoTrabalho());
		processo.setStatusProcessual(dto.getStatusProcessual());
		processo.setAreaAtuacao(dto.getAreaAtuacao());
		processo.setTipoAcao(dto.getTipoAcao());
		processo.setStatus(dto.getStatus());
		processo.setDataCadastro(dto.getDataCadastro());
		processo.setUsuario(dto.getUsuario());
		processo.setResponsavel(dto.getResponsavel());
		processo.setDataUltimaMovimentacao(dto.getDataUltimaMovimentacao());
		processo.setEmpresa(dto.getEmpresa());
		processo.setSnPush(dto.getSnPush());
		processo.setSnEmail(dto.getSnEmail());
		processo.setSnHistorico(dto.getSnHistorico());
		processo.setTipoDecisao(dto.getTipoDecisao());
		return processo;
	}
	
	public static ProcessoDto buildShort(Processo processo, List<PartesDto> partes, String processoAgravoApenso, String favorito) {
		ProcessoDto dto = new ProcessoDto();
		dto.setCodigo(processo.getCodigo());
		dto.setGrupoTrabalho(processo.getGrupoTrabalho());
		dto.setPasta(processo.getPasta());
		dto.setNrProcesso(processo.getNrProcesso());
		dto.setNrCnj(mascaraProcessoCnj(processo.getNrCnj()));
		dto.setPartes(partes);
		dto.setStatusProcessual(processo.getStatusProcessual());
		dto.setResponsavel(processo.getResponsavel());
		dto.setAreaAtuacao(processo.getAreaAtuacao());
		dto.setSnImportante(processo.getSnImportante());
		if(processo.getDataCadastro() != null) {
			dto.setDataCadastro(DatasUtil.formatarDataTela(processo.getDataCadastro()));
		} else {
			dto.setDataCadastro(null);
		}
		dto.setEstrategico(processo.getEstrategico());
		dto.setAgravoApenso(processoAgravoApenso);
		dto.setImportanteParaMim(favorito);
		dto.setPartesDesc(montaPartes(partes));
		dto.setDataCadastro(processo.getDataCadastro() == null ? null : DatasUtil.formatarDataTela(processo.getDataCadastro()));
		return dto;
	}

	public static ProcessoDto buildArquivamento(Processo processo, List<PartesDto> partes, String dataArquivamento) {
		ProcessoDto dto = new ProcessoDto();
		dto.setCodigo(processo.getCodigo());
		dto.setGrupoTrabalho(processo.getGrupoTrabalho());
		dto.setPasta(processo.getPasta());
		dto.setNrProcesso(processo.getNrProcesso());
		dto.setNrCnj(mascaraProcessoCnj(processo.getNrCnj()));
		dto.setPartes(partes);
		dto.setStatusProcessual(processo.getStatusProcessual());
		dto.setResponsavel(processo.getResponsavel());
		dto.setAreaAtuacao(processo.getAreaAtuacao());
		dto.setSnImportante(processo.getSnImportante());
		dto.setDataCadastro(DatasUtil.formatarDataTela(processo.getDataCadastro()));
		dto.setEstrategico(processo.getEstrategico());
		dto.setPartesDesc(montaPartes(partes));
		dto.setDataCadastro(processo.getDataCadastro() == null ? null : DatasUtil.formatarDataTela(processo.getDataCadastro()));
		dto.setDataArquivamento(DatasUtil.formatarDataTela(dataArquivamento));
		return dto;
	}

	public static ProcessoDto buildLixeira(Processo processo, List<PartesDto> partes, String dataExclusao) {
		ProcessoDto dto = new ProcessoDto();
		dto.setCodigo(processo.getCodigo());
		dto.setGrupoTrabalho(processo.getGrupoTrabalho());
		dto.setPasta(processo.getPasta());
		dto.setNrProcesso(processo.getNrProcesso());
		dto.setNrCnj(mascaraProcessoCnj(processo.getNrCnj()));
		dto.setPartes(partes);
		dto.setStatusProcessual(processo.getStatusProcessual());
		dto.setResponsavel(processo.getResponsavel());
		dto.setAreaAtuacao(processo.getAreaAtuacao());
		dto.setSnImportante(processo.getSnImportante());
		dto.setDataCadastro(DatasUtil.formatarDataTela(processo.getDataCadastro()));
		dto.setEstrategico(processo.getEstrategico());
		dto.setPartesDesc(montaPartes(partes));
		dto.setDataCadastro(processo.getDataCadastro() == null ? null : DatasUtil.formatarDataTela(processo.getDataCadastro()));
		dto.setDataExclusao(DatasUtil.formatarDataTela(dataExclusao));
		return dto;
	}

	private static String montaPartes(List<PartesDto> partes) {
		String strPartes = "";
		if(partes != null && partes.size() > 0) {
			for(PartesDto parte : partes) {
				strPartes = strPartes + parte.getPessoa().getNome() + ", ";
			}
		}

		return strPartes;
	}
}
