package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.TipoAcao;

@Repository
public interface TipoAcaoRepository extends JpaRepository<TipoAcao, Long> {
	
	List<TipoAcao> findByEmpresaCodigoOrderByNomeAsc(long codigo);
	Optional<TipoAcao> findByNomeAndEmpresaCodigo(String nome, long empresa);
}
