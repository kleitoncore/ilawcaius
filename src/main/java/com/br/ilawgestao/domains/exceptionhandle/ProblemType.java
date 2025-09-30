package com.br.ilawgestao.domains.exceptionhandle;

import lombok.Getter;

@Getter
public enum ProblemType {

	EMAIL_EM_USO("/erro-negocio", "Violação de regra de negócio"),
	ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada","Entidade não encontrada"),
	ENTIDADE_JA_CADASTRADA("entidade-ja-cadastrada","Entidade já cadastrada com esse nome"),
	ENTIDADE_EM_USO("/entidade-em-uso","Entidade em uso"),
	USUARIO_OU_SENHA_NÃO_CONFEREM("/usuario-ou-senha-nao-conferem", "Usuário ou Senha não Conferem"),
	USUARIO_SEM_PERMISSAO("/Usuario-Sem-Permissao", "Usuário sem Permissão"),
	ENTIDADE_NAO_CADASTRADA("/nenhuma-entidade-cadastrada","Nenhuma entidade foi cadastrada"),
	SALDO_INSUFICIENTE("/saldo-insuficiente","Saldo Insuficiente");
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title) {
		this.uri = "https://goten1.com.br/ilawgestao" + path;
		this.title = title;
	}
	
}
