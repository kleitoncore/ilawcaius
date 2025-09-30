package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.Fase;

import javax.swing.text.html.Option;

public interface FaseRepository extends JpaRepository<Fase, Long> {
	List<Fase> findByEmpresaCodigoOrderByNomeAsc(long empresa);
	Optional<Fase> findByNomeAndEmpresaCodigo(String nome, long empresa);
}
