package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
	List<Agenda> findByCodigoAtividade(long codigo);
}	
