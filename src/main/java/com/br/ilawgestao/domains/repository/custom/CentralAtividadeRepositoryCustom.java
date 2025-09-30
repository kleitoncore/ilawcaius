package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.CentralAtividade;

public interface CentralAtividadeRepositoryCustom {
	
	List<CentralAtividade> consultarCentralAtividades(String dataInicial, String dataFinal, long usuario);
}
