package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.GrupoTarefa;

public interface GrupoTarefaRepositoryCustom {
	List<GrupoTarefa> listarGruposPorTarefa(long tarefa);
	List<GrupoTarefa> listarGruposSemTarefa(long tarefa, long empresa);
}
