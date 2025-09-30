package com.br.ilawgestao.domains.repository.filtros;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroPush {
	
	private List<Long> gruposTrabalho;
	private String dtMovimentacaoInicial;
	private String dtMovimentacaoFinal;
	private List<String> statusProcessual;
	private String numeroProcesso;
	private String pasta;
	private String movimentacao;
	private String movimentoOculto;
	private long empresa;
	private String classificacao;
	private String ordenacao;
}
