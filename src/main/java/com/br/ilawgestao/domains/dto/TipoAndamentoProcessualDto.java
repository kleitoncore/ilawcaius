package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.TipoAndamentoProcessual;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoAndamentoProcessualDto {
	
	private Long codigo;
	private String nome;
	private Empresa empresa;
	
	public static TipoAndamentoProcessualDto build(TipoAndamentoProcessual tipo) {
		TipoAndamentoProcessualDto dto = new TipoAndamentoProcessualDto();
		dto.setCodigo(tipo.getCodigo());
		dto.setNome(tipo.getNome());
		dto.setEmpresa(tipo.getEmpresa());
		return dto;
	}
}
