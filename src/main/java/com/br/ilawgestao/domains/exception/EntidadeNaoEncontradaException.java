package com.br.ilawgestao.domains.exception;

public class EntidadeNaoEncontradaException extends NegocioException {

	private static final long serialVersionUID = 8171017885181666187L;
	
	public EntidadeNaoEncontradaException(String message) {
		super(message);
	}
}
