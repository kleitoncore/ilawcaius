package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.repository.custom.GraficoProcessoCustom;
import com.br.ilawgestao.domains.repository.custom.ProcessoPainelIndicadoresRepositoryCustom;
import com.br.ilawgestao.domains.repository.custom.ProcessoRepositoryCustom;
import com.br.ilawgestao.domains.repository.custom.RelatorioProcessoCustom;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long>, ProcessoRepositoryCustom, RelatorioProcessoCustom, 
	GraficoProcessoCustom, ProcessoPainelIndicadoresRepositoryCustom {
	Optional<Processo> findByNrCnjAndEmpresaCodigoAndStatus(String cnj, long empresa, long status);
	Optional<Processo> findByNrProcessoAndEmpresaCodigoAndStatus(String processo, long empresa, long status);
	List<Processo> findByNrCnjAndStatus(String cnj, long status);
	@Query(value = "select * from processo where MONTH(dtcadastro) = MONTH(curdate()) and cdempresa =:codigoEmpresa order by dtcadastro desc", nativeQuery = true)
	List<Processo> consultarProcessosCadastradosEsseMes(long codigoEmpresa);
}
