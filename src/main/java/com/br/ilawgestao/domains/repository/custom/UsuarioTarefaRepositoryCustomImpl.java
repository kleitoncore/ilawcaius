package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.models.UsuarioTarefa;

public class UsuarioTarefaRepositoryCustomImpl implements UsuarioTarefaRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioTarefa> listarUsuariosTarefa(long tarefa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ut.* from usuario_tarefa ut left join agrupamento_tarefa at on at.cdagrupamento = ut.cdagrupamento ");
		sql.append("                          		   left join tarefa t on t.cdtarefa = at.cdtarefa ");
		sql.append(" where t.cdtarefa =:tarefa ");
		
		Query query = manager.createNativeQuery(sql.toString(), UsuarioTarefa.class);
		query.setParameter("tarefa", tarefa);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> listarUsuariosNaoCadastrados(long tarefa, long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from usuario ");
		sql.append(" where cdusuario not in(select cdusuario from usuario_tarefa ");
		sql.append(" where cdagrupamento not in(select cdagrupamento from agrupamento_tarefa where cdtarefa =:tarefa))");
		sql.append(" and cdempresa =:empresa ");
		
		Query query = manager.createNativeQuery(sql.toString(), Usuario.class);
		query.setParameter("tarefa", tarefa);
		query.setParameter("empresa", empresa);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioTarefa> listarUsuariosTarefaGrupo(long tarefa, long grupo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ut.* from usuario_tarefa ut left join agrupamento_tarefa at on at.cdagrupamento = ut.cdagrupamento ");
		sql.append("                          		   left join tarefa t on t.cdtarefa = at.cdtarefa ");
		sql.append(" where t.cdtarefa =:tarefa and at.cdgrupo =:grupo");
		
		Query query = manager.createNativeQuery(sql.toString(), UsuarioTarefa.class);
		query.setParameter("tarefa", tarefa);
		query.setParameter("grupo", grupo);
		return query.getResultList();
	}

}
