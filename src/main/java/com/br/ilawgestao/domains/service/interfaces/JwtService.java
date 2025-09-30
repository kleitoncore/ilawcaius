package com.br.ilawgestao.domains.service.interfaces;

import com.br.ilawgestao.domains.models.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {
	
	String gerarToken(Usuario usuario);
	Claims obterClaims(String token) throws ExpiredJwtException;
	boolean isTokenValido(String token);
	String loginUsuario(String token);
}
