package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.AreaAtuacao;

@Repository
public interface AreaAtuacaoRepository extends JpaRepository<AreaAtuacao, Long> {
	
	List<AreaAtuacao> findByEmpresaCodigoOrderByNomeAsc(long codigo);
	Optional<AreaAtuacao> findByNomeAndEmpresaCodigo(String nome, long empresa);
	
}
