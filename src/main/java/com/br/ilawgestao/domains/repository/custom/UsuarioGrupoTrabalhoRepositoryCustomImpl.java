package com.br.ilawgestao.domains.repository.custom;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.Usuario;

public class UsuarioGrupoTrabalhoRepositoryCustomImpl implements UsuarioGrupoTrabalhoRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<Usuario> listarUsuariosGrupo(long grupo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select u from Usuario u where u.codigo in(select usuario.codigo from UsuarioGrupoTrabalho where grupo.codigo =:grupo) order by u.nome");
		
		TypedQuery<Usuario> query = manager.createQuery(sql.toString(), Usuario.class);
		
		query.setParameter("grupo", grupo);
		
		return query.getResultList();
	}

	@Override
	public List<Usuario> listarUsuariosGrupoAtivos(long grupo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select u from Usuario u where u.codigo in(select usuario.codigo from UsuarioGrupoTrabalho where grupo.codigo =:grupo) and situacao = 0 order by u.nome");

		TypedQuery<Usuario> query = manager.createQuery(sql.toString(), Usuario.class);

		query.setParameter("grupo", grupo);

		return query.getResultList();
	}

}
