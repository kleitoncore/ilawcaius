package com.br.ilawgestao.domains.dto;

import java.math.BigInteger;

import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraficoAtividadeGrupoDto {
	
	private String grupo;
	private BigInteger total;
	@Transient
	private String cor;
	
}
