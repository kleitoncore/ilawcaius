package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.ProcessoExcluido;

public interface ProcessoExcluidoRepository extends JpaRepository<ProcessoExcluido, Long> {
	List<ProcessoExcluido> findByProcessoEmpresaCodigoOrderByDataExcluidoDesc(long empresa);
	Optional<ProcessoExcluido> findByProcessoCodigo(long processo);
}
