package com.br.ilawgestao.domains.dto;

import java.math.BigInteger;

import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraficoProcessoDto {
	
	private String nome;
	private BigInteger quantidade;
	@Transient
	private String cor;
	
}
