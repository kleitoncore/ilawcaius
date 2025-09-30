package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.ObjetoAcaoProcesso;

@Repository
public interface ObjetoAcaoProcessoRepository extends JpaRepository<ObjetoAcaoProcesso, Long> {
	
	List<ObjetoAcaoProcesso> findByProcessoCodigoOrderByObjetoNomeAsc(long processo);
}
