package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.dto.ProcessoArquivadoDto;
import com.br.ilawgestao.domains.models.ProcessoArquivado;

public interface ProcessoArquivadoCustom {
	
	List<ProcessoArquivado> processosArquivadosPorDatas(String dataInicial, String dataFinal, long empresa);
	List<ProcessoArquivadoDto> processosArquivadosMes(long empresa, String dataInicial, String dataFinal);
	
}
