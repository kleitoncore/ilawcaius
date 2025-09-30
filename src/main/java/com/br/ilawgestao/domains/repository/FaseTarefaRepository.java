package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.ilawgestao.domains.models.FaseTarefa;

public interface FaseTarefaRepository extends JpaRepository<FaseTarefa, Long> {
	
	List<FaseTarefa> findByEmpresaCodigoOrderByNomeAsc(long empresa);
	@Query(value = "select cdfase,nofase,cdempresa from fase_atividade where cdfase not in(select cdfase from titulo_atividade_fase where cdtitulo =:titulo) and cdempresa =:empresa", nativeQuery = true)
	List<FaseTarefa> listarFasesSemTitulo(long titulo, long empresa);
}
