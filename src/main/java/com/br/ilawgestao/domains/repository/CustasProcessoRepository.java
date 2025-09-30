package com.br.ilawgestao.domains.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.CustasProcesso;

@Repository
public interface CustasProcessoRepository extends JpaRepository<CustasProcesso, Long> {
	
	List<CustasProcesso> findByProcessoCodigoOrderByCustasNomeAsc(long processo);
}
