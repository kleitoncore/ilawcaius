package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.TipoPagamento;

public class TipoPagamentoRepositoryCustomImpl implements TipoPagamentoRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoPagamento> listaPagamentosPorCodigos(String codigos) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from tipo_pagamento where cdtipo in(" + codigos + ")");
		Query query = manager.createNativeQuery(sql.toString(), TipoPagamento.class);
		return query.getResultList();
	}

}
