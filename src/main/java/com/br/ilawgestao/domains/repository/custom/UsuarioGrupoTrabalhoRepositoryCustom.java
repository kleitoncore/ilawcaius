package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.Usuario;

public interface UsuarioGrupoTrabalhoRepositoryCustom {
	List<Usuario> listarUsuariosGrupo(long grupo);
	List<Usuario> listarUsuariosGrupoAtivos(long grupo);
}
