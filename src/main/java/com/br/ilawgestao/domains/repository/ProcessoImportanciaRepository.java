package com.br.ilawgestao.domains.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.ProcessoImportancia;

import java.util.List;

@Repository
public interface ProcessoImportanciaRepository extends JpaRepository<ProcessoImportancia, Long> {
	ProcessoImportancia findProcessoImportanciaByUsuarioCodigoAndProcessoCodigo(long usuario, long processo);
	List<ProcessoImportancia> findByProcessoCodigo(long processo);
}
