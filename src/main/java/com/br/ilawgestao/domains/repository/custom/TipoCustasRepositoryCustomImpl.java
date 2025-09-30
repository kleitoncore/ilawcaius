package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.TipoCusta;

public class TipoCustasRepositoryCustomImpl implements TipoCustasRepositoryCustom {

	@Autowired
	private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoCusta> listaTiposCustasPorCodigos(String codigos) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from tipo_custa where cdtipo in(" + codigos + ")");
		Query query = manager.createNativeQuery(sql.toString(), TipoCusta.class);
		return query.getResultList();
	}

}
