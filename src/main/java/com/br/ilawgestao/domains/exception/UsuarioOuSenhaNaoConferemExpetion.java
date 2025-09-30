package com.br.ilawgestao.domains.exception;

public class UsuarioOuSenhaNaoConferemExpetion extends NegocioException {

	private static final long serialVersionUID = 5943330090712318622L;
	
	public UsuarioOuSenhaNaoConferemExpetion(String mensagem) {
		super(mensagem);
	}
}
