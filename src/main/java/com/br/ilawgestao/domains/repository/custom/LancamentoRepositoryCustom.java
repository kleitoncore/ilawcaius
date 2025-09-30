package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.models.Lancamento;

public interface LancamentoRepositoryCustom {
	
	List<LancamentoPorTipoDto> lancamantoPorTipoDespesaData(String dataInicial, String dataFinal, long empresa);
	List<LancamentoPorTipoDto> lancamantoPorTipoReceitaData(String dataInicial, String dataFinal, long empresa);
	List<Lancamento> lancamentosPorTipoPeriodo(long empresa, String dataInicial, String dataFinal, String tipo);
	List<Lancamento> lancamentosPorTipoDescricaoPeriodo(long empresa, String dataInicial, String dataFinal, long tipoCodigo, String tipo, String situacao);
	List<Lancamento> lancamentosPendente(long empresa, long tipoCodigo, String tipo);
	List<LancamentoPorTipoGraficoDto> lancamentoGrafico(long empresa, String dataInicial, String dataFinal, String tipo);
	List<Lancamento> relatorioDetalhado(long empresa, String dataInicial, String dataFinal, long tipo, 
			String categoria, String situacao, long codigo, long processo, String descricao,
			String classificacao, String orderm);
	List<RelatorioFinanceiroResumidoDto> relatorioResumidoCategoria(long empresa, String dataInicial, String dataFinal, String situacao);
	List<RelatorioFinanceiroResumidoDto> relatorioResumidoTipo(long empresa, String dataInicial, String dataFinal, String situacao);
	FinanceiroVencendoDto vencendoAmanha(long empresa);
	FinanceiroVencendoDto vencendoHoje(long empresa);
	List<Lancamento> lancamentosVencendoHoje(long empresa, long tipoCodigo, String tipo);
	FinanceiroVencendoDto pendentes(long empresa);
	List<LancamentosPorContaFinanceiraDto> lancamentosPagosPorContaFinanceira(String dataInicial, String dataFinal, long empresa, String tipo);
}
