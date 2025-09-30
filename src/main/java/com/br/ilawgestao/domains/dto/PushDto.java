package com.br.ilawgestao.domains.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushDto {

	private long codigo;
	private long codigoProcesso;
	private String pasta;
	private String nrcnj;
	private String autor;
	private String reu;
	private String situacao;
	private String dtMovimentacao;
	private String movimentacao;
	private long codigoGrupo;
	private String grupo;
	private long codigoEmpresa;
	private String snEnviou;
	private String dtCarregamento;
	private String temHistorico;
	private String snOculto;
}
