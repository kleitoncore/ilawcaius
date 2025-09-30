package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.GrupoTarefa;
import com.br.ilawgestao.domains.repository.custom.GrupoTarefaRepositoryCustom;

public interface GrupoTarefaRepository extends JpaRepository<GrupoTarefa, Long>, GrupoTarefaRepositoryCustom {
	List<GrupoTarefa> findByEmpresaCodigoOrderByNome(long empresa);
}
