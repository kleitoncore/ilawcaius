package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.dto.relatorios.ContingenciaDto;
import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioProcesso;

public interface OutrosRelatoriosProcessosRepositoryCustom {
	
	List<ContingenciaDto> relatorioContingencia(FiltroRelatorioProcesso filtro);
}
