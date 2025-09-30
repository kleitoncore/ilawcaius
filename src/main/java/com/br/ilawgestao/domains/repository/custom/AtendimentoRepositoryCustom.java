package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.Atendimento;

public interface AtendimentoRepositoryCustom {
	
	List<Atendimento> listarAtendimentos(long empresa, String dataInicial, String dataFinal, long pessoa, long grupo);
}
