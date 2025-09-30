package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.HistoricoPessoa;

@Repository
public interface HistoricoPessoaRepository extends JpaRepository<HistoricoPessoa, Long> {
	public List<HistoricoPessoa> findByPessoaCodigoOrderByDataHistoricoDesc(long pessoa);
	@Query(value = "select * from historico_pessoa where dthistorico >= :dataInicial and dthistorico <= :dataFinal order by dthistorico desc", nativeQuery = true)
	public List<HistoricoPessoa> consultarHistoricoPessoa(String dataInicial, String dataFinal);
	@Query(value = "select * from historico_pessoa h where h.cdhistorico = (select max(h2.cdhistorico) from historico_pessoa h2 where h2.cdpessoa = h.cdpessoa) and cdpessoa = :pessoa",nativeQuery = true)
	public Optional<HistoricoPessoa> ultimaAlteracao(long pessoa);
}
