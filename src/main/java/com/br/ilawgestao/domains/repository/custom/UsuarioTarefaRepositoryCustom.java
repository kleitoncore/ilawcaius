package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.models.UsuarioTarefa;

public interface UsuarioTarefaRepositoryCustom {
	List<UsuarioTarefa> listarUsuariosTarefa(long tarefa);
	List<UsuarioTarefa> listarUsuariosTarefaGrupo(long tarefa, long grupo);
	List<Usuario> listarUsuariosNaoCadastrados(long tarefa, long empresa);
}
