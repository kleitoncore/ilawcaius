package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.dto.MovimentoProcessualEmpresaDTO;
import com.br.ilawgestao.domains.models.HistoricoProcesso;
import com.br.ilawgestao.domains.models.MovimentoProcessualRelatorio;
import com.br.ilawgestao.domains.repository.filtros.FiltroMovimentacaoProcessualRelatorio;

public interface MovimentoProcessualRelatorioRepositoryCustom {
	
	List<MovimentoProcessualRelatorio> consultarMovimentoProcessual(FiltroMovimentacaoProcessualRelatorio filtro);
	HistoricoProcesso consultaUltimoHistorico(long processo);
	List<MovimentoProcessualEmpresaDTO> processosMovimentosPorGrupoEmpresa(String dataMovvimentacao);
}
