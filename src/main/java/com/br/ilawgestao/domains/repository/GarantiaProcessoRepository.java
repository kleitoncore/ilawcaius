package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.GarantiaProcesso;

@Repository
public interface GarantiaProcessoRepository extends JpaRepository<GarantiaProcesso, Long> {
	
	List<GarantiaProcesso> findByProcessoCodigoOrderByGarantiaNomeAsc(long processo);
}
