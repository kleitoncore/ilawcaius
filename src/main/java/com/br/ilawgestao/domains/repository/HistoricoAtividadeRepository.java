package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.HistoricoAtividade;

@Repository
public interface HistoricoAtividadeRepository extends JpaRepository<HistoricoAtividade, Long> {
	List<HistoricoAtividade> findByAtividadeCodigoOrderByCodigoDesc(long atividade);

	@Query(value = "select * from historico_atividade where dthistorico = curdate() and tphistorico = 'D' and cdusuario =:usuario limit 1", nativeQuery = true)
	Optional<HistoricoAtividade> consultarUltimoHistoricoAlteracaoData(long  usuario);
}
