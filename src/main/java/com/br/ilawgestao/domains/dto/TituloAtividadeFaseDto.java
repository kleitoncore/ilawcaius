package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.FaseTarefa;
import com.br.ilawgestao.domains.models.TituloAtividade;
import com.br.ilawgestao.domains.models.TituloAtividadeFase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TituloAtividadeFaseDto {
	
	private long codigo;
	private FaseTarefa fase;
	private TituloAtividade titulo;
	
	public static TituloAtividadeFaseDto build(TituloAtividadeFase entity) {
		TituloAtividadeFaseDto dto = new TituloAtividadeFaseDto();
		dto.setCodigo(entity.getCodigo());
		dto.setFase(entity.getFase());
		dto.setTitulo(entity.getTitulo());
		return dto;
	}
}
