package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.ArquivoFinanceiro;

public interface ArquivoFinanceiroRepository extends JpaRepository<ArquivoFinanceiro, Long> {
	
	List<ArquivoFinanceiro> findByLancamentoCodigo(long lancamento);
}
