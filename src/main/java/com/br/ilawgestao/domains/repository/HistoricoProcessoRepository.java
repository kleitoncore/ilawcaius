package com.br.ilawgestao.domains.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.HistoricoProcesso;
import com.br.ilawgestao.domains.repository.custom.HistoricoProcessoRepositoryCustom;

@Repository
public interface HistoricoProcessoRepository extends JpaRepository<HistoricoProcesso, Long>, HistoricoProcessoRepositoryCustom {
	
	List<HistoricoProcesso> findByProcessoCodigoOrderByCodigoDesc(long processo);
	
}
