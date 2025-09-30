package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.CentralAtividade;

public class CentralAtividadeRepositoryCustomImpl implements CentralAtividadeRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<CentralAtividade> consultarCentralAtividades(String dataInicial, String dataFinal, long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select c from CentralAtividade c where usuario.codigo =:usuario and dataRegistro >=:dataIni and dataRegistro <=:dataFim order by dataRegistro desc ");
		
		TypedQuery<CentralAtividade> query = manager.createQuery(sql.toString(), CentralAtividade.class);
		
		query.setParameter("usuario", usuario);
		query.setParameter("dataIni", dataInicial);
		query.setParameter("dataFim", dataFinal);
		
		return query.getResultList();
	}

}
