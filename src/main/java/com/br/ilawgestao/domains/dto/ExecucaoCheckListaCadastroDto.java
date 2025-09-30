package com.br.ilawgestao.domains.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecucaoCheckListaCadastroDto {
	private AtividadeDTO atividade;
	private List<CheckListAtividadeDto> checkList;
}
