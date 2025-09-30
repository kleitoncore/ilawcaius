package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.models.TransferenciaFinanceira;
import com.br.ilawgestao.domains.repository.filtros.FiltroTranferenciaFinanceira;

import java.util.List;

public interface TransferenciaFinanceiraRepositoryCustom {
    List<TransferenciaFinanceira> consultarTransferencias(FiltroTranferenciaFinanceira filtro);
}
