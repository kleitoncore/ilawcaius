package com.br.ilawgestao.domains.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.AgrupamentoTarefa;

public interface AgrupamentoTarefaRepository extends JpaRepository<AgrupamentoTarefa, Long> {
	Optional<AgrupamentoTarefa> findByTarefaCodigoAndGrupoCodigo(long tarefa, long grupo);
}
