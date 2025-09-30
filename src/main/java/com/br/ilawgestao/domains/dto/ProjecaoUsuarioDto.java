package com.br.ilawgestao.domains.dto;


import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.ProjecaoUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjecaoUsuarioDto {
	private long codigo;
	private UsuarioDto usuario;
	private Empresa empresa;
	private long ano;
	private long pontos;
	private long mes;
	
	public static ProjecaoUsuarioDto build(ProjecaoUsuario projecao, UsuarioDto usuario) {
		return ProjecaoUsuarioDto.builder()
				.codigo(projecao.getCodigo())
				.usuario(usuario)
				.ano(projecao.getAno())
				.mes(projecao.getMes())
				.pontos(projecao.getPontos())
				.empresa(projecao.getEmpresa())
				.mes(projecao.getMes())
				.build();
	}
}
