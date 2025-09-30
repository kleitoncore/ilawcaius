package com.br.ilawgestao.domains.exception;

public class UsuarioSemPermissaoException extends NegocioException {

	private static final long serialVersionUID = -3717366297139313279L;
	
	public UsuarioSemPermissaoException(String message) {
		super(message);
	}
}
