package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.MovimentoProcessualRelatorio;
import com.br.ilawgestao.domains.repository.custom.MovimentoProcessualRelatorioRepositoryCustom;

public interface MovimentoProcessualRelatorioRepository extends JpaRepository<MovimentoProcessualRelatorio, Long>, MovimentoProcessualRelatorioRepositoryCustom {
	
	List<MovimentoProcessualRelatorio> findByCdempresaAndDtCarregamentoOrderByCodigoDesc(long empresa, String dataCarregamento);

}
