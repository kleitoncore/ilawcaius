package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.CamposRelatorioProcesso;

@Repository
public interface CamposRelatorioProcessoRepository extends JpaRepository<CamposRelatorioProcesso, Long> {
	
	@Query(value = "select * cdcempo, nocampo from campo_relatorio_processo where cdcampo in(:codigos)", nativeQuery = true)
	List<CamposRelatorioProcesso> listarCamposRelatorio(String codigos);
}
