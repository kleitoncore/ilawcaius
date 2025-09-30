package com.br.ilawgestao.domains.repository;

import com.br.ilawgestao.domains.models.TransacaoFinanceira;
import com.br.ilawgestao.domains.models.TransferenciaFinanceira;
import com.br.ilawgestao.domains.repository.custom.TransferenciaFinanceiraRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransferenciaFinanceiraRepository extends JpaRepository<TransferenciaFinanceira,Long>, TransferenciaFinanceiraRepositoryCustom {
}
