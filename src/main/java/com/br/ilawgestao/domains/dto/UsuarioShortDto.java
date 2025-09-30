package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioShortDto {
	
	private long codigo;
	private String nome;
	private String email;
	
	public static UsuarioShortDto build(Usuario usuario) {
		UsuarioShortDto dto = new UsuarioShortDto();
		dto.setCodigo(usuario.getCodigo());
		dto.setNome(usuario.getNome());
		dto.setEmail(usuario.getEmail());
		return dto;
	}
}
