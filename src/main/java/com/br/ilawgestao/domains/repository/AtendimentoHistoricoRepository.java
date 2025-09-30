package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.AtendimentoHistorico;

@Repository
public interface AtendimentoHistoricoRepository extends JpaRepository<AtendimentoHistorico, Long> {
	
	List<AtendimentoHistorico> findByAtendimentoCodigoOrderByCodigoDesc(long atendimento);
}
