package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.MotivoResultado;

public interface MotivoResultadoRepository extends JpaRepository<MotivoResultado, Long> {
	
	List<MotivoResultado> findByEmpresaCodigoOrderByNomeAsc(long empresa);
}
