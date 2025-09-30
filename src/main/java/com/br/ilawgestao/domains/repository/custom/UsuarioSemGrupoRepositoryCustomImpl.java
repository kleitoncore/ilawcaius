package com.br.ilawgestao.domains.repository.custom;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.br.ilawgestao.domains.models.Usuario;

public class UsuarioSemGrupoRepositoryCustomImpl implements UsuarioSemGrupoRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<Usuario> listarUsuariosSemGrupo(long grupo, long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select u from Usuario u "
				+ "where u.codigo not in(select usuario.codigo from UsuarioGrupoTrabalho where grupo.codigo =:grupo) and u.empresa.codigo =:empresa "
				+ "order by u.nome");
		
		TypedQuery<Usuario> query = manager.createQuery(sql.toString(), Usuario.class);
		
		query.setParameter("grupo", grupo);
		query.setParameter("empresa", empresa);
		
		return query.getResultList();
	}

	@Override
	public List<Usuario> listarUsuariosSemGruposPessoa(long grupo, long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select u from Usuario u where u.empresa.codigo =:empresa and "
				+ "u.codigo not in(select usuario.codigo from GrupoClienteUsuario where grupo.codigo =:grupo) order by u.nome ");
		
		TypedQuery<Usuario> query = manager.createQuery(sql.toString(), Usuario.class);
		
		query.setParameter("grupo", grupo);
		query.setParameter("empresa", empresa);
		
		return query.getResultList();
	}
	
}
