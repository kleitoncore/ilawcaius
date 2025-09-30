package com.br.ilawgestao.domains.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.TipoEndereco;

@Repository
public interface TipoEnderecoRepository extends JpaRepository<TipoEndereco, String> {

}
