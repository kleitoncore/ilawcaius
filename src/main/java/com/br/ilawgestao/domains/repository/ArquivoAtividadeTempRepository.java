package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.ArquivoAtividadeTemp;

public interface ArquivoAtividadeTempRepository extends JpaRepository<ArquivoAtividadeTemp, Long> {
	
	List<ArquivoAtividadeTemp> findByUsuarioCodigoOrderByArquivo(long usuario);
}
