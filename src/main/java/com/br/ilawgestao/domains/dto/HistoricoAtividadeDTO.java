package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.HistoricoAtividade;
import com.br.ilawgestao.domains.models.Usuario;

import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.Data;

@Data
public class HistoricoAtividadeDTO {
	
	public HistoricoAtividade transformeParaObjeto() {
		return new HistoricoAtividade(codigo, atividade, dsHistorico, dtHistorico, usuario, tpHistorico);
	}
	
	private long codigo;
	private Atividade atividade;	
	private String dsHistorico;
	private String dtHistorico;
	private Usuario usuario;
	private String tpHistorico;

	public static HistoricoAtividadeDTO build(HistoricoAtividade historico) {
		HistoricoAtividadeDTO dto = new HistoricoAtividadeDTO();
		dto.setCodigo(historico.getCodigo());
		dto.setDsHistorico(historico.getDsHistorico());
		dto.setDtHistorico(DatasUtil.formatarDataTela(historico.getDtHistorico()));
		dto.setUsuario(historico.getUsuario());
		dto.setAtividade(historico.getAtividade());
		return dto;
	}
}
