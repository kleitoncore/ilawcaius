package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.Tarefa;
import com.br.ilawgestao.domains.repository.custom.TarefaRepositoryCustom;

public interface TarefaRepository extends JpaRepository<Tarefa, Long>, TarefaRepositoryCustom {
	List<Tarefa> findByEmpresaCodigoOrderByTitulo(long empresa);
}
