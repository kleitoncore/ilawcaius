package com.br.ilawgestao.domains.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import com.br.ilawgestao.domains.utils.DatasUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioProcessoViewDto {
	private long codigo;
	private String numeroProcesso;
	private String numeroCnj;
	private String pasta;
	private String dataDistribuicao;
	private String dataUltimaDecisao;
	private String dataUltimaMovimentacao;
	private String dataSentenca;
	private String dataCadastro;
	private String comarca;
	private String pedidos;
	private String snPush;
	private String snEmail;
	private String snHistorico;
	private String importanteEmpresa;
	private String importanteParaMim;
	private String estrategico;
	private String ultimaAtividade;
	private String dataUltimaAtividade;
	private String ultimoHistorico;
	private String dataUltimoHistorico;
	private String objetos;
	private String autores;
	private String reus;
	private String advogados;
	private String grupoTrabalho;
	private String statusProcessual;
	private String tipoAcao;
	private String tipoDecisao;
	private String areaAtuacao;
	private String pagamentos;
	private String valorPagamentos;
	private String custas;
	private String valorCustas;
	private String valorProvavel;
	private String valorPossivel;
	private String valorRemoto;
	private String valorCausa;
	private String uf;
	private String fase;
	private String rito;
	private String motivoResultado;
	private String dataArquivamento;
	private String responsavel;
	private String observacao;
	private String banca;
	
	public static RelatorioProcessoViewDto build (RelatorioProcessoDto dto) {
		RelatorioProcessoViewDto relatorio = new RelatorioProcessoViewDto();
		relatorio.setCodigo(dto.getCodigo());
		relatorio.setNumeroProcesso(dto.getNumeroProcesso());
		relatorio.setNumeroCnj(mascaraProcessoCnj(dto.getNumeroCnj()));
		relatorio.setPasta(dto.getPasta());
		relatorio.setDataDistribuicao(Objects.nonNull(dto.getDataDistribuicao()) ? DatasUtil.formatarDataTela(dto.getDataDistribuicao()) : null);
		relatorio.setDataUltimaDecisao(Objects.nonNull(dto.getDataUltimaDecisao()) ? DatasUtil.formatarDataTela(dto.getDataUltimaDecisao()) : null);
		relatorio.setDataUltimaMovimentacao(Objects.nonNull(dto.getDataUltimaMovimentacao()) ? DatasUtil.formatarDataTela(dto.getDataUltimaMovimentacao()) : null);
		relatorio.setDataSentenca(Objects.nonNull(dto.getDataSentenca()) ? DatasUtil.formatarDataTela(dto.getDataSentenca()) : null);
		relatorio.setDataCadastro(Objects.nonNull(dto.getDataCadastro()) ? DatasUtil.formatarDataTela(dto.getDataCadastro()) : null);
		relatorio.setComarca(dto.getComarca());
		relatorio.setPedidos(dto.getPedidos());
		relatorio.setSnPush(dto.getSnPush());
		relatorio.setSnEmail(dto.getSnEmail());
		relatorio.setSnHistorico(dto.getSnHistorico());
		relatorio.setImportanteEmpresa(dto.getImportanteEmpresa());
		relatorio.setImportanteParaMim(dto.getImportanteParaMim());
		relatorio.setEstrategico(dto.getEstrategico());
		relatorio.setUltimaAtividade(dto.getUltimaAtividade());
		relatorio.setDataUltimaAtividade(Objects.nonNull(dto.getDataUltimaAtividade()) ? DatasUtil.formatarDataTela(dto.getDataUltimaAtividade()) : null);
		relatorio.setUltimoHistorico(dto.getUltimoHistorico());
		relatorio.setDataUltimoHistorico(Objects.nonNull(dto.getDataUltimoHistorico()) ? DatasUtil.formatarDataTela(dto.getDataUltimoHistorico()) : null);
		relatorio.setDataArquivamento(Objects.nonNull(dto.getDataArquivamento()) ? DatasUtil.formatarDataTela(dto.getDataArquivamento()) : null);
		relatorio.setObjetos(dto.getObjetos());
		relatorio.setAutores(dto.getAutores());
		relatorio.setReus(dto.getReus());
		relatorio.setAdvogados(dto.getAdvogados());
		relatorio.setGrupoTrabalho(dto.getGrupoTrabalho());
		relatorio.setStatusProcessual(dto.getStatusProcessual());
		relatorio.setTipoAcao(dto.getTipoAcao());
		relatorio.setTipoDecisao(dto.getTipoDecisao());
		relatorio.setAreaAtuacao(dto.getAreaAtuacao());
		relatorio.setPagamentos(dto.getPagamentos());
		if(dto.getPagamentos() != null) {
			Locale ptBr = new Locale("pt", "BR");
			NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
			
			BigDecimal valorPagamentos = new BigDecimal(dto.getValorPagamentos());
			String formato = nf.format(valorPagamentos);
			relatorio.setValorPagamentos(formato);
		} else {
			relatorio.setValorPagamentos(null);
		}
		relatorio.setCustas(dto.getCustas());
		if(dto.getCustas() != null) {
			Locale ptBr = new Locale("pt", "BR");
			NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
			
			BigDecimal valorCustas = new BigDecimal(dto.getValorCustas());
			String formato = nf.format(valorCustas);
			relatorio.setValorCustas(formato);
		} else {
			relatorio.setValorCustas(null);
		}
		if(dto.getPedidos() != null) {
			Locale ptBr = new Locale("pt", "BR");
			NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
			//Valor Provável
			BigDecimal valorProvavel = new BigDecimal(dto.getValorProvavel());
			String formatoValorProvavel = nf.format(valorProvavel);
			relatorio.setValorProvavel(formatoValorProvavel);
			//Valor Possível
			BigDecimal valorPossivel = new BigDecimal(dto.getValorPossivel());
			String formatoValorPossivel = nf.format(valorPossivel);
			relatorio.setValorPossivel(formatoValorPossivel);
			//Valor Remoto
			BigDecimal valorRemoto = new BigDecimal(dto.getValorRemoto());
			String formatoValorRemoto = nf.format(valorRemoto);
			relatorio.setValorRemoto(formatoValorRemoto);
		} else {
			relatorio.setValorProvavel(null);
			relatorio.setValorPossivel(null);
			relatorio.setValorRemoto(null);
		}
		
		if(dto.getValorCausa() != 0) {
			Locale ptBr = new Locale("pt", "BR");
			NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
			//Valor Provável
			BigDecimal valorCausa = new BigDecimal(dto.getValorCausa());
			String formatoValorCausa = nf.format(valorCausa);
			relatorio.setValorCausa(formatoValorCausa);
		} else {
			relatorio.setValorCausa(null);
		}
		relatorio.setUf(dto.getUf());
		relatorio.setFase(dto.getFase());
		relatorio.setRito(dto.getRito());
		relatorio.setMotivoResultado(dto.getMotivoResultado());
		relatorio.setResponsavel(dto.getResponsavel());
		relatorio.setObservacao(dto.getObservacao());
		relatorio.setBanca(dto.getBanca());
		return relatorio;
	}
	
	private static String mascaraProcessoCnj(String cnj) {
		String cnjMascarado = "";
		if(cnj != null && cnj != "" && !cnj.equals("0")) {
			cnjMascarado = cnj.substring(0, 7);
			cnjMascarado = cnjMascarado + "-" + cnj.substring(7, 9);
			cnjMascarado = cnjMascarado + "." + cnj.substring(9, 13);
			cnjMascarado = cnjMascarado + "." + cnj.substring(13, 14);
			cnjMascarado = cnjMascarado + "." + cnj.substring(14, 16);
			cnjMascarado = cnjMascarado + "." + cnj.substring(16, 20);
		}
		
		return cnjMascarado;
	}
}
