package com.br.ilawgestao.domains.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;

public class GrupoTrabalhoRepositoryImpl implements GrupoTrabalhoRepositoryQuery {

	@Autowired
	private EntityManager manager;
	
	@Override
	public List<GrupoTrabalho> listarGrupoTrabalhoPorNome(String nome, Empresa empresa) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<GrupoTrabalho> criteriaQuery = builder.createQuery(GrupoTrabalho.class);
		
		Root<GrupoTrabalho> root = criteriaQuery.from(GrupoTrabalho.class);
		criteriaQuery.select(root);
		
		Predicate[] predicates = criarFiltros(nome, empresa, builder, root);
		criteriaQuery.where(predicates);
		
		criteriaQuery.orderBy(builder.asc(root.get("nome")));
		
		TypedQuery<GrupoTrabalho> typeQuery = manager.createQuery(criteriaQuery);
		return typeQuery.getResultList();
		
	}
	
	private Predicate[] criarFiltros(String nome, Empresa empresa, CriteriaBuilder criteriaBuilder, Root<GrupoTrabalho> root) {
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
		predicates.add(criteriaBuilder.equal(root.get("empresa"), empresa.getCodigo()));
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}
