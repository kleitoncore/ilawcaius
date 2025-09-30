package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.ProjecaoUsuario;
import com.br.ilawgestao.domains.repository.filtros.FiltroProjecaoUsuario;

public class ProjecaoUsuarioRepositoryCustomImpl implements ProjecaoUsuarioRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<ProjecaoUsuario> consultarProjecoesUsuario(FiltroProjecaoUsuario filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p from ProjecaoUsuario p where p.empresa.codigo = " + filtro.getEmpresa());
		
		if(!filtro.getUsuario().equals("")) {
			sql.append(" and p.usuario.nome like '" + "%" + filtro.getUsuario() + "%" + "'");
		}
		
		if(filtro.getAno() != 0) {
			sql.append(" and p.ano = " + filtro.getAno());
		}
		
		if(filtro.getMes() != 0) {
			sql.append(" and p.mes = " + filtro.getMes());
		}
		
		sql.append(" order by p.usuario.nome, ano, mes ");
		
		TypedQuery<ProjecaoUsuario> query = manager.createQuery(sql.toString(), ProjecaoUsuario.class);
		
		return query.getResultList();
	}

}
