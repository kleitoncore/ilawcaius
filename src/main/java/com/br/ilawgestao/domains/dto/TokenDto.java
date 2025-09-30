package com.br.ilawgestao.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDto {
	
	private String nome;
	private String token;
}
