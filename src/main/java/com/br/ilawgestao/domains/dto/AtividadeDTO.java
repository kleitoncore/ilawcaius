package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.*;

import lombok.Getter;

@Getter
public class AtividadeDTO {
	
	public Atividade transformaParaObjeto() {
		return new Atividade(codigo, titulo, processo, subGrupo, dtLimite, dtFatal, status, 
				descricao, tipo, usuario, dtRegistro, dtConcluido, statusCalculado, 
				grupoPai, hora,responsaveis, importante, urgente, privado, pessoa, dataLida, pontuacao,tituloAtividade, lancamentoFinanceiro);
	}
	
	private long codigo;
	private String titulo;
	private Processo processo;
	private GrupoTrabalho subGrupo;
	private String dtLimite;
	private String dtFatal;
	private StatusAtividade status;
	private String descricao;
	private String tipo;	
	private Usuario usuario;
	private String dtRegistro;
	private String dtConcluido;
	private String statusCalculado;
	private String grupoPai;
	private String hora;
	private String responsaveis;
	private String importante;
	private String urgente;
	private String privado;
	private Pessoa pessoa;
	private String dataLida;
	private long pontuacao;
	private TituloAtividade tituloAtividade;
	private Lancamento lancamentoFinanceiro;
}
