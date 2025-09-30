package com.br.ilawgestao.domains.repository.filtros;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroMovimentacaoProcessualRelatorio {
	
	private String grupo;
	private long empresa;
	private String dtMovimentoInicial;
	private String dtMovimentacaoFinal;
	private String dtCarregamento;
	private String statusProcessual;
	private String numeroPrcesso;
	private String numeroPasta;
	private String movimentacao;
	private String snOculto;
	private String classificacao;
	private String ordenacao;
}
