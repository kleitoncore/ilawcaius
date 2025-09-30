package com.br.ilawgestao.domains.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.PerfilPessoa;

@Repository
public interface PerfilPessoaRepository extends JpaRepository<PerfilPessoa, String> {
	
}
