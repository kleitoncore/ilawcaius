package com.br.ilawgestao.domains.exception;

public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = -8072937178155982924L;

	public NegocioException(String message, Throwable cause) {
		super(message, cause);
	}

	public NegocioException(String message) {
		super(message);
	}
}
