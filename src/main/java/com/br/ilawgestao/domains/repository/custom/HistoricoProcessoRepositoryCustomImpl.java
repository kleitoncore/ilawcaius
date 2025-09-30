package com.br.ilawgestao.domains.repository.custom;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.br.ilawgestao.domains.models.HistoricoProcesso;

public class HistoricoProcessoRepositoryCustomImpl implements HistoricoProcessoRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public HistoricoProcesso consultarUltimaMovimentacao(long processo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select h from HistoricoProcesso h where h.codigo = (select max(h2.codigo) from HistoricoProcesso h2 ");
		sql.append(" where h2.processo.codigo = h.processo.codigo) and h.processo.codigo =:processo ");
		
		TypedQuery<HistoricoProcesso> query = manager.createQuery(sql.toString(), HistoricoProcesso.class);
		
		query.setParameter("processo", processo);
		
		return query.getSingleResult();
	}
	
}
