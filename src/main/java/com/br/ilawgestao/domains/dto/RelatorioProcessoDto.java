package com.br.ilawgestao.domains.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatorioProcessoDto {
	private long codigo;
	private String numeroProcesso;
	private String numeroCnj;
	private String pasta;
	private Date dataDistribuicao;
	private Date dataUltimaDecisao;
	private Date dataUltimaMovimentacao;
	private Date dataSentenca;
	private Date dataCadastro;
	private String comarca;
	private String pedidos;
	private double valorProvavel;
	private double valorPossivel;
	private double valorRemoto;
	private double valorCausa;
	private String snPush;
	private String snEmail;
	private String snHistorico;
	private String ultimaAtividade;
	private Date dataUltimaAtividade;
	private String ultimoHistorico;
	private Date dataUltimoHistorico;
	private String objetos;
	private String autores;
	private String reus;
	private String advogados;
	private String grupoTrabalho;
	private String statusProcessual;
	private String tipoAcao;
	private String tipoDecisao;
	private String areaAtuacao;
	private String pagamentos;
	private double valorPagamentos;
	private String custas;
	private double valorCustas;
	private String uf;
	private String fase;
	private String rito;
	private String motivoResultado;
	private Date dataArquivamento;
	private String importanteEmpresa;
	private String importanteParaMim;
	private String estrategico;
	private String responsavel;
	private String observacao;
	private String banca;
}
