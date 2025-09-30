package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import com.br.ilawgestao.domains.repository.custom.TituloAtividadeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.TituloAtividade;

public interface TituloAtividadeRepository extends JpaRepository<TituloAtividade, Long>, TituloAtividadeRepositoryCustom {
	List<TituloAtividade> findByEmpresaCodigoOrderByTitulo(long empresa);
	Optional<TituloAtividade> findByTituloAndEmpresaCodigo(String nome, long empresa);
	Optional<TituloAtividade> findByTituloAndEmpresaCodigoAndFaseCodigo(String nome, long empresa, long fase);
	List<TituloAtividade> findByFaseCodigo(long fase);
	Optional<TituloAtividade> findFirstByTituloAndEmpresaCodigo(String nome, long empresa);
}
