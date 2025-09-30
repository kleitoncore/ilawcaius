package com.br.ilawgestao.domains.repository.filtros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroRelatorioProcesso {
	
	private String pessoa;
	private String grupoTrabalho;
	private long tipoAcao;
	private long areaAtuacao;
	private long tipoDecisao;
	private String dataInicialDecisao;
	private String dataFinalDecisao;
	private String dataInicialDistribuicao;
	private String dataFinalDistribuicao;
	private long statusProcessual;
	private boolean excetoProcessosArquivados;
	private String dataInicialSentenca;
	private String dataFinalSentenca;
	private String objetoAcao;
	private String garantia;
	private String operadorLogicoGarantias;
	private double valorGarantia;
	private String pagamentos;
	private String operadorLogicoPagamento;
	private double valorPagamento;
	private String dataInicialPagamento;
	private String dataFinalPagamento;
	private String custas;
	private String operadorLogicoCustas;
	private double valorCustas;
	private String dataInicialCadastro;
	private String dataFinalCadastro;
	private String dataInicialAlteracao;
	private String dataFinalAlteracao;
	private String dataArquivamentoInicial;
	private String dataArquivamentoFinal;
	private String importanteEmpresa;
	private String importanteParaMim;
	private String estrategico;
	private String pedidos;
	private long empresa;
	private long usuario;
	private String uf;
	private long rito;
	private long fase;
	private long motivoResultado;
	private long responsavel;
	private long banca;
}
