package com.br.ilawgestao.domains.repository;

import com.br.ilawgestao.domains.models.TransacaoFinanceira;
import com.br.ilawgestao.domains.repository.custom.TransacaoFinanceiraReposioryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransacaoFinanceiraRepoository extends JpaRepository<TransacaoFinanceira,Long>, TransacaoFinanceiraReposioryCustom {
    Optional<TransacaoFinanceira> findByLancamentoCodigo(long lancamento);
    List<TransacaoFinanceira> findByLancamentoCodigoOrderByCodigo(long lancamento);
}
