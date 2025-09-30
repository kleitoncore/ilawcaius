package com.br.ilawgestao.domains.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.PagamentoProcesso;

@Repository
public interface PagamentoProcessoRepository extends JpaRepository<PagamentoProcesso, Long> {

	List<PagamentoProcesso> findByProcessoCodigoOrderByDtPagamentoDesc(long processo);
}
