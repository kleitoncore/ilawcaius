package com.br.ilawgestao.domains.dto;


import com.br.ilawgestao.domains.models.AgrupamentoTarefa;
import com.br.ilawgestao.domains.models.GrupoTarefa;
import com.br.ilawgestao.domains.models.Tarefa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrupamentoTarefaDto {
	private long codigo;
	private GrupoTarefa grupo;
	private Tarefa tarefa;
	
	public static AgrupamentoTarefaDto build(AgrupamentoTarefa entity) {
		AgrupamentoTarefaDto dto = new AgrupamentoTarefaDto();
		dto.setCodigo(entity.getCodigo());
		dto.setGrupo(entity.getGrupo());
		dto.setTarefa(entity.getTarefa());
		return dto;
	}
}
