package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.HistoricoProcesso;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.TipoAndamentoProcessual;
import com.br.ilawgestao.domains.models.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoricoProcessoDto {
	private long codigo;
	private Processo processo;
	private Usuario usuario;
	private String historico;
	private String dataHistorico;
	private String dataOcorrencia;
	private TipoAndamentoProcessual tipoAndamento;
	
	public static HistoricoProcessoDto build(HistoricoProcesso historico) {
		HistoricoProcessoDto dto = new HistoricoProcessoDto();
		dto.setCodigo(historico.getCodigo());
		dto.setHistorico(historico.getHistorico());
		dto.setDataHistorico(historico.getDataHistorico());
		dto.setDataOcorrencia(historico.getDataOcorrencia());
		dto.setProcesso(historico.getProcesso());
		dto.setUsuario(historico.getUsuario());
		dto.setTipoAndamento(historico.getTipoAndamento());
		return dto;
	}
}