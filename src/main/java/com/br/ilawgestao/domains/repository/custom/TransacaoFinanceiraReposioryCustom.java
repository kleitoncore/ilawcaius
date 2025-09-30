package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.models.TransacaoFinanceira;

import java.util.List;

public interface TransacaoFinanceiraReposioryCustom {
    List<TransacaoFinanceira> consultarTransacoesPorData(long empresa, String dataInicial, String dataFinal);
}
