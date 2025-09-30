package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.TipoPagamento;

public interface TipoPagamentoRepositoryCustom {
	
	List<TipoPagamento> listaPagamentosPorCodigos(String codigos);
}
