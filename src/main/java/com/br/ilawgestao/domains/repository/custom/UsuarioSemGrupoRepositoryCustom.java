package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.Usuario;

public interface UsuarioSemGrupoRepositoryCustom {
	
	public List<Usuario> listarUsuariosSemGrupo(long grupo, long empresa);
	public List<Usuario> listarUsuariosSemGruposPessoa(long grupo, long empresa);
}
