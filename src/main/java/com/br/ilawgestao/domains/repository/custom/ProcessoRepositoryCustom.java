package com.br.ilawgestao.domains.repository.custom;

import java.util.List;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.repository.filtros.FiltroProcesso;

public interface ProcessoRepositoryCustom {
	List<Processo> consultarProcessos(FiltroProcesso filtro);
	List<Processo> consultarProcessosPorPessoa(long pessoa);
	List<Processo> consultaProcessoPorIndice(String indice, long empresa, long usuario);
	List<Processo> consultarProcessosParados(long empresa, long dias, FiltroProcesso filtro);
}
