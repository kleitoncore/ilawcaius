package com.br.ilawgestao.domains.dto;

import java.util.List;

import com.br.ilawgestao.domains.models.Usuario;

import lombok.Data;

@Data
public class AlterarAtividadeUsuarioDto {
	
	private long atividade;
	private List<Usuario> usuariosAlterados;
	private long usuarioAlterou;
	private String tipoUsuario;
	
}
