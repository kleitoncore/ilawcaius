package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.ArquivoPessoaTemp;

@Repository
public interface ArquivoPessoaTempRepository extends JpaRepository<ArquivoPessoaTemp, Long>{
	
	List<ArquivoPessoaTemp> findByUsuarioCodigoOrderByArquivoAsc(long usuario);

}
