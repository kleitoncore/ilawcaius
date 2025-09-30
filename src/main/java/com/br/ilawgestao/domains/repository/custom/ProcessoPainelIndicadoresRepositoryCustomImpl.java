package com.br.ilawgestao.domains.repository.custom;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

public class ProcessoPainelIndicadoresRepositoryCustomImpl implements ProcessoPainelIndicadoresRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public int totalProcessos(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(p.cdprocesso) from processo p where p.cdempresa =:empresa ");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("empresa", empresa);
		
		return query.getSingleResult().hashCode();
		
	}

	@Override
	public int totalAtivos(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(p.cdprocesso) from processo p where p.cdempresa =:empresa and p.cdstatus = 0 ");
		sql.append(" and p.cdprocesso not in (select cdprocesso from processo_arquivado)");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("empresa", empresa);
		
		return query.getSingleResult().hashCode();
	}

	@Override
	public int parados(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(p.cdprocesso) from processo p"); 
		sql.append(" where p.cdprocesso in(select cdprocesso from historico_alteracao_processo where DATEDIFF(curdate(),dtalteracao) >= 90)"); 
		sql.append("   and p.cdprocesso in(select cdprocesso from historico_processo where DATEDIFF(curdate(),dthistorico) >= 90)"); 
		sql.append("   and p.cdprocesso in(select cdprocesso from processo_arquivado where DATEDIFF(curdate(),dtarquivamento) >= 90)");
		sql.append("   and p.cdempresa =:empresa ");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("empresa", empresa);
		
		return query.getSingleResult().hashCode();
	}

	@Override
	public int arquivados(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(p.cdprocesso) from processo p where p.cdempresa =:empresa and p.cdstatus_processual = (select cdstatus from status_processual where descricao = 'Arquivado' and cdempresa =:empresa)");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("empresa", empresa);
		query.setParameter("empresa", empresa);
		
		return query.getSingleResult().hashCode();
	}

	@Override
	public int excluidos(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(p.cdprocesso) from processo p where p.cdempresa =:empresa and p.cdstatus = 3 ");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("empresa", empresa);
		
		return query.getSingleResult().hashCode();
	}

	@Override
	public int processoPush(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(p.cdprocesso) from processo p where p.cdempresa =:empresa and p.snpush = 'S' and p.cdstatus = 0 ");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("empresa", empresa);
		
		return query.getSingleResult().hashCode();
	}

}
