package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.CheckListAtividade;
import com.br.ilawgestao.domains.models.TituloAtividade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckListAtividadeDto {
	private long codigo;
	private TituloAtividade atividade;
	private long ordem;
	private String tarefa;
	private String descricao;
	
	public static CheckListAtividadeDto build(CheckListAtividade checkList) {
		CheckListAtividadeDto dto = new CheckListAtividadeDto();
		dto.setCodigo(checkList.getCodigo());
		dto.setAtividade(checkList.getAtividade());
		dto.setOrdem(checkList.getOrdem());
		dto.setTarefa(checkList.getTarefa());
		dto.setDescricao(checkList.getDescricao());
		return dto;
	}
}
