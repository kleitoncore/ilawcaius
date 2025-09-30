package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.PedidoProcesso;

public interface PedidoProcessoRepository extends JpaRepository<PedidoProcesso, Long> {
	
	List<PedidoProcesso> findByProcessoCodigo(long processo);
}
