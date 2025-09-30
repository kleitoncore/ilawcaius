package com.br.ilawgestao.domains.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.service.interfaces.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService {
	
	@Value("${jwt.expiracao}")
	private String expiracao;
	
	@Value("${jwt.chave-assinatura}")
	private String chaveAssinatura;

	@Override
	public String gerarToken(Usuario usuario) {
		long exp = Long.valueOf(expiracao);
		LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
		Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
		Date data = Date.from(instant);
		
		String token = Jwts.builder()
					.setExpiration(data)
					.setSubject(usuario.getEmail())
					.claim("usuarioId", usuario.getCodigo())
					.claim("nome", usuario.getNome())
					.claim("perfil", usuario.getPerfil())
					.claim("empresa", usuario.getEmpresa())
					.claim("dataHoraExpiracao", dataHoraExpiracao.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
					.signWith(SignatureAlgorithm.HS512, chaveAssinatura)
					.compact();
		
		return token;
	}

	@Override
	public Claims obterClaims(String token) throws ExpiredJwtException {
		return Jwts.parser()
				.setSigningKey(chaveAssinatura)
				.parseClaimsJws(token)
				.getBody();
	}

	@Override
	public boolean isTokenValido(String token) {
		try {
			Claims claims = this.obterClaims(token);
			Date dataExp = claims.getExpiration();
			LocalDateTime dataExpiracao = dataExp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			boolean dataHoraAtualIsAfeterDataHoraExpiracao = LocalDateTime.now().isAfter(dataExpiracao);
			return !dataHoraAtualIsAfeterDataHoraExpiracao;
		} catch (ExpiredJwtException e) {
			return false;
		}
	}

	@Override
	public String loginUsuario(String token) {
		Claims claims = this.obterClaims(token);
		return claims.getSubject();
	}

}
