package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.GrupoCliente;

public class GrupoClienteUsuarioCustomImpl implements GrupoClienteUsuarioCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<GrupoCliente> listarGruposUsuario(long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select g from GrupoCliente g where g.codigo in(select grupo.codigo from GrupoClienteUsuario where usuario.codigo =:usuario) order by g.nome");
		TypedQuery<GrupoCliente> query = manager.createQuery(sql.toString(), GrupoCliente.class);
		query.setParameter("usuario", usuario);
		return query.getResultList();
	}

}
