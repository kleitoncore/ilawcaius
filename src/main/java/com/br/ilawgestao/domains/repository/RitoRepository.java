package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.Rito;

public interface RitoRepository extends JpaRepository<Rito, Long> {
	
	List<Rito> findByEmpresaCodigoOrderByNomeAsc(long empresa);
}
