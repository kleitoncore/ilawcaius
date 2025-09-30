package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.TipoDespesaReceita;

@Repository
public interface TipoDespesaReceitaRepository extends JpaRepository<TipoDespesaReceita, Long> {
	
	List<TipoDespesaReceita> findByEmpresaCodigoOrderByNome(long empresa);
	Optional<TipoDespesaReceita> findByNomeAndEmpresaCodigo(String nome, long empresa);
	List<TipoDespesaReceita> findByEmpresaCodigoAndTipoOrderByNome(long empresa, String tipo);
}
