package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.GrupoTarefa;

public class GrupoTarefaRepositoryCustomImpl implements GrupoTarefaRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoTarefa> listarGruposPorTarefa(long tarefa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from grupo_tarefa where cdgrupo in(select cdgrupo from agrupamento_tarefa where cdtarefa =:tarefa) order by nogrupo ");
		Query query = manager.createNativeQuery(sql.toString(), GrupoTarefa.class);
		query.setParameter("tarefa", tarefa);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoTarefa> listarGruposSemTarefa(long tarefa, long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from grupo_tarefa where cdgrupo not in(select cdgrupo from agrupamento_tarefa where cdtarefa =:tarefa) and cdempresa =:empresa order by nogrupo ");
		Query query = manager.createNativeQuery(sql.toString(), GrupoTarefa.class);
		query.setParameter("tarefa", tarefa);
		query.setParameter("empresa", empresa);
		return query.getResultList();
	}

}
