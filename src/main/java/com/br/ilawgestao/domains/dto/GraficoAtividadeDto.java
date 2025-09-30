package com.br.ilawgestao.domains.dto;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraficoAtividadeDto {
	
	private long codigo;
	private String mes;
	private BigInteger total;
	
}
