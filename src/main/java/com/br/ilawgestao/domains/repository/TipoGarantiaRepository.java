package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.TipoGarantia;

@Repository
public interface TipoGarantiaRepository extends JpaRepository<TipoGarantia, Long> {
	
	List<TipoGarantia> findByEmpresaCodigoOrderByNome(long empresa);
	Optional<TipoGarantia> findByNomeAndEmpresaCodigo(String nome, long empresa);
	
}
