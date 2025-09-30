package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.TipoAndamentoProcessual;

public interface TipoAndamentoProcessualRepository extends JpaRepository<TipoAndamentoProcessual, Long> {
	
	List<TipoAndamentoProcessual> findByEmpresaCodigoOrderByNomeAsc(long empresa);
	Optional<TipoAndamentoProcessual> findByNomeAndEmpresaCodigo(String nome, long empresa);
}
