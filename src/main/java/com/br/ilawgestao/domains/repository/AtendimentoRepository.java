package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.Atendimento;
import com.br.ilawgestao.domains.repository.custom.AtendimentoRepositoryCustom;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>, AtendimentoRepositoryCustom {
	
	List<Atendimento> findByPessoaCodigoOrderByCodigoDesc(long pessoa);
	
}
