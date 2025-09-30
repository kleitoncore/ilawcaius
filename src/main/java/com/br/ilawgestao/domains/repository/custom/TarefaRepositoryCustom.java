package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.Tarefa;

public interface TarefaRepositoryCustom {
	
	List<Tarefa> listarTarefasPorGrupo(long grupo);
	List<Tarefa> listarTarefasSemGrupo(long grupo, long empresa);
	List<Tarefa> listarTarefasPorGrupoTrabalhoEGrupoAtividade(long grupoAtividade, long grupoTrabalho);
	
}
