package com.br.ilawgestao.domains.repository.custom;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.Tarefa;

public class TarefaRepositoryCustomImpl implements TarefaRepositoryCustom {

	@Autowired
	private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Tarefa> listarTarefasPorGrupo(long grupo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from tarefa t inner join agrupamento_tarefa at on t.cdtarefa = at.cdtarefa ");
		sql.append("                         inner join grupo_tarefa gt on gt.cdgrupo = at.cdgrupo ");
		sql.append(" where gt.cdgrupo =:grupo order by dstitulo ");
		
		Query query = manager.createNativeQuery(sql.toString(), Tarefa.class);
		query.setParameter("grupo", grupo);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tarefa> listarTarefasSemGrupo(long grupo, long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from tarefa where cdtarefa not in(select cdtarefa from agrupamento_tarefa where cdgrupo =:grupo) and cdempresa =:empresa order by dstitulo ");
		Query query = manager.createNativeQuery(sql.toString(), Tarefa.class);
		query.setParameter("grupo", grupo);
		query.setParameter("empresa", empresa);
		return query.getResultList();
	}

	@Override
	public List<Tarefa> listarTarefasPorGrupoTrabalhoEGrupoAtividade(long grupoAtividade,long grupoTrabalho) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from tarefa t inner join agrupamento_tarefa at on t.cdtarefa = at.cdtarefa\n" +
				"inner join grupo_tarefa gt on gt.cdgrupo = at.cdgrupo\n" +
				"where gt.cdgrupo = :grupoAtividade \n" +
				" and t.cdtarefa in(select rua.cdatividade " +
				"from rodizio_usuario_atividade rua where rua.cdgrupo_atividade = gt.cdgrupo and rua.cdgrupo_trabalho = :grupoTrabalho)");

		Query query = manager.createNativeQuery(sql.toString(), Tarefa.class);
		query.setParameter("grupoAtividade", grupoAtividade);
		query.setParameter("grupoTrabalho", grupoTrabalho);
		return query.getResultList();
	}

}
