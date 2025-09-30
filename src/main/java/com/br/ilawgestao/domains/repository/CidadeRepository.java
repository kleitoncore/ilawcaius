package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
	
	public List<Cidade> findCidadeByEstadoCodigo(long estado);
}
