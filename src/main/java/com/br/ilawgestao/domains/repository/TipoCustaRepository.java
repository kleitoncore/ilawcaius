package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.TipoCusta;
import com.br.ilawgestao.domains.repository.custom.TipoCustasRepositoryCustom;

@Repository
public interface TipoCustaRepository extends JpaRepository<TipoCusta, Long>, TipoCustasRepositoryCustom {
	
	List<TipoCusta> findByEmpresaCodigoOrderByNomeAsc(long empresa);
	Optional<TipoCusta> findByNomeAndEmpresaCodigo(String nome, long empresa);
	
}
