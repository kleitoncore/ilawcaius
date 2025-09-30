package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.ProcessoArquivado;
import com.br.ilawgestao.domains.repository.custom.ProcessoArquivadoCustom;
import org.springframework.data.jpa.repository.Query;

public interface ProcessoArquivadoRepository extends JpaRepository<ProcessoArquivado, Long>, ProcessoArquivadoCustom {
	Optional<ProcessoArquivado> findByProcessoCodigo(long processo);
	@Query(value = "select * from processo_arquivado where cdprocesso in(select cdprocesso from processo where cdempresa = :empresaCodigo) order by dtarquivamento desc",nativeQuery = true)
	List<ProcessoArquivado> consultarProcessosArquivados(long empresaCodigo);
}
