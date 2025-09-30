package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.Rito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RitoDto {
	
	private long codigo;
	private String nome;
	private Empresa empresa;
	
	public static RitoDto build(Rito rito) {
		RitoDto dto = new RitoDto();
		dto.setCodigo(rito.getCodigo());
		dto.setNome(rito.getNome());
		dto.setEmpresa(rito.getEmpresa());
		return dto;
	}
}
