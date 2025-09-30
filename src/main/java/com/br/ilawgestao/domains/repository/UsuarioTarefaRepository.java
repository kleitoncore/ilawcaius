package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.UsuarioTarefa;
import com.br.ilawgestao.domains.repository.custom.UsuarioTarefaRepositoryCustom;

public interface UsuarioTarefaRepository extends JpaRepository<UsuarioTarefa, Long>, UsuarioTarefaRepositoryCustom {
	List<UsuarioTarefa> findByAgrupamentoCodigo(long codigo);
}
