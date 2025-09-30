package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.models.HistoricoProcesso;

public interface HistoricoProcessoRepositoryCustom {
	
	HistoricoProcesso consultarUltimaMovimentacao(long processo);
}
