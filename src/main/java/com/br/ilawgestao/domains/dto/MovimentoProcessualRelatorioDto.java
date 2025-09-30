package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.MovimentoProcessualRelatorio;
import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimentoProcessualRelatorioDto {
	private long codigo;
	private long cdprocesso;
	private String nrcnj;
	private String nrpasta;
	private String autor;
	private String reu;
	private String nosituacao;
	private String dtMovimentacao;
	private String dsmovimentacao;
	private long cdgrupo;
	private String nogrupo;
	private long cdempresa;
	private String snEnviou;
	private String dtCarregamento;
	private String temHistorico;
	private String snOculto;
	private String ultimaMovimentacao;
	private String dtUltimaMovimentacao;
	private String dataRegistroHistorico;

	public static MovimentoProcessualRelatorioDto build(MovimentoProcessualRelatorio rel, String ultimaMovimentacao,
														String dtUltimaMovimentacao) {
		MovimentoProcessualRelatorioDto dto = new MovimentoProcessualRelatorioDto();
		dto.setCodigo(rel.getCodigo());
		dto.setCdprocesso(rel.getCdprocesso());
		dto.setNrcnj(rel.getNrcnj());
		dto.setNrpasta(rel.getNrpasta());
		dto.setAutor(rel.getAutor());
		dto.setReu(rel.getReu());
		dto.setNosituacao(rel.getNosituacao());
		dto.setDtMovimentacao(DatasUtil.formatarDataTela(rel.getDtMovimentacao()));
		dto.setDsmovimentacao(rel.getDsmovimentacao());
		dto.setCdgrupo(rel.getCdgrupo());
		dto.setNogrupo(rel.getNogrupo());
		dto.setCdempresa(rel.getCdempresa());
		dto.setSnEnviou(rel.getSnEnviou());
		dto.setDtCarregamento(DatasUtil.formatarDataTela(rel.getDtCarregamento()));
		dto.setTemHistorico(rel.getTemHistorico());
		dto.setSnOculto(rel.getSnOculto());
		dto.setUltimaMovimentacao(ultimaMovimentacao);
		dto.setDtUltimaMovimentacao(DatasUtil.formatarDataTela(dtUltimaMovimentacao));
		return dto;
	}
}
