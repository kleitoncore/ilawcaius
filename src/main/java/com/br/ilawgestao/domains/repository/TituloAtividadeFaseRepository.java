package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.TituloAtividadeFase;

public interface TituloAtividadeFaseRepository extends JpaRepository<TituloAtividadeFase, Long> {
	
	List<TituloAtividadeFase> findByTituloCodigoOrderByFaseNomeAsc(long codigo);
	Optional<TituloAtividadeFase> findByTituloCodigoAndFaseCodigo(long titulo, long fase);
	List<TituloAtividadeFase> findByFaseCodigoOrderByTituloTituloAsc(long codigo);
	Optional<TituloAtividadeFase> findByTituloCodigo(long codigo);
}
