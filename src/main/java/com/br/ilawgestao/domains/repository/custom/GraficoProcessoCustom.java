package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.dto.GraficoProcessoDto;
import com.br.ilawgestao.domains.dto.GraficoProcessoMesDto;

public interface GraficoProcessoCustom {
	
	List<GraficoProcessoDto> graficoStatus(long empresa);
	List<GraficoProcessoDto> graficoGrupoTrabalho(long empresa);
	int graficoGrupoTrabalhoOutros(String grupos, long empresa);
	List<GraficoProcessoDto> graficoAreaAtuacao(long empresa);
	List<GraficoProcessoDto> graficoTipoAcao(long empresa);
	List<GraficoProcessoDto> graficoObjetos(long empresa);
	List<GraficoProcessoDto> graficoPagamentos(long empresa);
	List<GraficoProcessoDto> graficoCustas(long empresa);
	List<GraficoProcessoMesDto> graficoProcessoMes(long empresa, String dataInicial, String dataFinal);
	
}
