package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.Lancamento;
import com.br.ilawgestao.domains.repository.custom.LancamentoRepositoryCustom;

@Repository
public interface LancamentoRepostory extends JpaRepository<Lancamento, Long>, LancamentoRepositoryCustom {
	
	List<Lancamento> findByProcessoCodigoOrderByDtVencimento(long processo);
	
}
