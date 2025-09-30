package com.br.ilawgestao.domains.dto;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraficoAtividadesStatusDto {
	private String status;
	private BigInteger total;
	private String cor;
}
