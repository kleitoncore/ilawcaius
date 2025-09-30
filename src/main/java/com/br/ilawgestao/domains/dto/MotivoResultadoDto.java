package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.MotivoResultado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotivoResultadoDto {
	
	private long codigo;
	private String nome;
	private Empresa empresa;
	
	public static MotivoResultadoDto build(MotivoResultado motivo) {
		MotivoResultadoDto dto = new MotivoResultadoDto();
		dto.setCodigo(motivo.getCodigo());
		dto.setNome(motivo.getNome());
		dto.setEmpresa(motivo.getEmpresa());
		return dto;
	}
}
