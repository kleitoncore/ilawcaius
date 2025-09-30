package com.br.ilawgestao.domains.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.br.ilawgestao.domains.models.HistoricoAlteracaoProcesso;

import java.util.List;

public interface HistoricoAlteracaoProcessoRepository extends JpaRepository<HistoricoAlteracaoProcesso, Long> {
	List<HistoricoAlteracaoProcesso> findByProcessoCodigo(long codigo);
}
