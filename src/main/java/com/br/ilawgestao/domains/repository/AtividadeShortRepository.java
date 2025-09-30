package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.AtividadeShort;

public interface AtividadeShortRepository extends JpaRepository<AtividadeShort, Long> {
	List<AtividadeShort> findByCodigoAtividade(long codigo);
	List<AtividadeShort> findByCodigoProcesso(long processo);
}
