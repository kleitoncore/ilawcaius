package com.br.ilawgestao.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioAtividadesDto {
	private long codigo;
	private String titulo;
	private String descricao;
	private String dataLimite;
	private String dataFatal;
	private String dataConcluido;
	private String numerocnj;
	private String numeroProcesso;
	private String numeroPasta;
	private String autor;
	private String reu;
	private String responsavel;
	private String interessado;
	private String status;
	private String dataCadastro;
	private String tipoAtividade;
	private String subGrupo;
	private String grupo;
}
