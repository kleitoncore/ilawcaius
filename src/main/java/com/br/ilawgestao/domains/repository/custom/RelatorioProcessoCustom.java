package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.dto.RelatorioProcessoDto;
import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioProcesso;

public interface RelatorioProcessoCustom {
	
	List<RelatorioProcessoDto> relatorioProcesso(FiltroRelatorioProcesso filtro);
}
