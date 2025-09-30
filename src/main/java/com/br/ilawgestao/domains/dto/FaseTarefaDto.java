package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.FaseTarefa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaseTarefaDto {
	
	private long codigo;
	private String nome;
	private Empresa empresa;
	
	public static FaseTarefaDto build(FaseTarefa fase) {
		FaseTarefaDto dto = new FaseTarefaDto();
		dto.setCodigo(fase.getCodigo());
		dto.setNome(fase.getNome());
		dto.setEmpresa(fase.getEmpresa());
		return dto;
	}
}
