package com.br.ilawgestao.domains.repository.filtros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroRelatorioAtividades {
	private String dataLimiteInicial;
	private String dataLimiteFinal;
	private long responsavel;
	private long interessado;
	private long grupoTrabalho;
	private String dataConcluidoInicial;
	private String dataConcluidoFinal;
	private String dataCriacaoInicial;
	private String dataCriacaoFinal;
	private String dataAlteracaoInicial;
	private String dataAlteracaoFinal;
	private String dataFatalInicial;
	private String dataFatalFinal;
	private String status;
	private String tipoAtividade;
	private long empresa;
	private long usuario;
	private int classificacao;
	private int ordenacao;
}
