package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.ArquivoAtividade;

@Repository
public interface ArquivoAtividadeRepository extends JpaRepository<ArquivoAtividade, Long> {
	
	List<ArquivoAtividade> findByAtividadeCodigoOrderByArquivo(long atividade);
}
