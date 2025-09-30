package com.br.ilawgestao.domains.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.ArquivoProcesso;

@Repository
public interface ArquivoProcessoRepository extends JpaRepository<ArquivoProcesso, Long> {
	
	List<ArquivoProcesso> findByProcessoCodigoOrderByNomeAsc(long processo);
}
