package com.br.ilawgestao.domains.repository.custom;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;

public class GrupoTrabalhoRepositoryCustomImpl implements GrupoTrabalhoRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<GrupoTrabalho> listarGruposTrabalho(String nome, Empresa empresa) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select g from GrupoTrabalho g where g.codigo = g.grupoPai and g.empresa.codigo = :empresaCodigo ");
		sql.append(" and g.nome not in('Agravo de Instrumento','Processo Apenso') ");
			
		if(nome != "") {
			sql.append(" and g.nome like :nome ");
		}
		
		sql.append(" order by g.nome ");
		
		TypedQuery<GrupoTrabalho> query = manager.createQuery(sql.toString(), GrupoTrabalho.class);
		
		query.setParameter("empresaCodigo", empresa.getCodigo());
		
		if(nome != "" ) {
			query.setParameter("nome", "%" + nome + "%");
		}
		
		return query.getResultList();
	}

	@Override
	public List<GrupoTrabalho> listarGruposTrabalhoUsuario(long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select g from GrupoTrabalho g where g.codigo in(select grupo.codigo from UsuarioGrupoTrabalho where usuario.codigo =:usuario) ");
		sql.append(" and g.nome not in('Agravo de Instrumento','Processo Apenso') order by g.nome ");
		
		TypedQuery<GrupoTrabalho> query = manager.createQuery(sql.toString(), GrupoTrabalho.class);
		
		query.setParameter("usuario", usuario);
		
		return query.getResultList();
	}
	
	@Override
	public List<GrupoTrabalho> listarGruposTrabalhoUsuario2(long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select g from GrupoTrabalho g where g.codigo in(select grupo.codigo from UsuarioGrupoTrabalho where usuario.codigo =:usuario) order by g.nome ");
		
		TypedQuery<GrupoTrabalho> query = manager.createQuery(sql.toString(), GrupoTrabalho.class);
		
		query.setParameter("usuario", usuario);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoTrabalho> consultarGruposPorCodigos(String codigos) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from grupo_trabalho where cdgrupo in(" + codigos + ")");
		Query query = manager.createNativeQuery(sql.toString(), GrupoTrabalho.class);
		return query.getResultList();
	}

	@Override
	public List<GrupoTrabalho> listarGruposTrabalhosAtivos(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select g from GrupoTrabalho g where g.codigo = g.grupoPai and g.empresa.codigo = :empresaCodigo ");
		sql.append(" and g.nome not in('Agravo de Instrumento','Processo Apenso') and g.status = 'S' ");
		sql.append(" order by g.nome ");

		TypedQuery<GrupoTrabalho> query = manager.createQuery(sql.toString(), GrupoTrabalho.class);

		query.setParameter("empresaCodigo", empresa);

		return query.getResultList();
	}

}
