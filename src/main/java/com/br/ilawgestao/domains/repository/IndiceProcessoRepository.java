package com.br.ilawgestao.domains.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.IndiceProcesso;

@Repository
public interface IndiceProcessoRepository extends JpaRepository<IndiceProcesso, Long>{
	
	Optional<IndiceProcesso> findByProcessoCodigo(long processo);
}
