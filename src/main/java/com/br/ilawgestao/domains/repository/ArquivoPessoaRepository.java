package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.ArquivoPessoa;

@Repository
public interface ArquivoPessoaRepository extends JpaRepository<ArquivoPessoa, Long> {
	
	List<ArquivoPessoa> findByPessoaCodigoOrderByArquivo(long cliente);
	
	@Query("from ArquivoPessoa where pessoa.codigo = :pessoa and arquivo like %:nome% order by arquivo ")
	List<ArquivoPessoa> listarArquivosPorNome(String nome, long pessoa);
	
	@Query("from ArquivoPessoa where pessoa.codigo = :pessoa and arquivo = :nome ")
	Optional<ArquivoPessoa> consultarArquivoPorNome(String nome, long pessoa);
	
}
