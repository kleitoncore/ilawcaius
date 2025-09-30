package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.TipoDespesaReceita;

import lombok.Getter;

@Getter
public class TipoDespesaeceitaDto {
	
	public TipoDespesaReceita tranformeParaObjeto() {
		return new TipoDespesaReceita(codigo, nome, tipo, empresa, cor, tipoCalculado);
	}
	
	private long codigo;
	private String nome;
	private String tipo;
	private Empresa empresa;
	private String cor;
	private String tipoCalculado;
}
