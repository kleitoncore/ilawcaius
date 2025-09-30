package com.br.ilawgestao.domains.dto;

import java.math.BigInteger;

import javax.persistence.Transient;

import lombok.Data;

@Data
public class GraficoAtividadeFaseDto {
	private String fase;
	private BigInteger total;
	@Transient
	private String cor;
}
