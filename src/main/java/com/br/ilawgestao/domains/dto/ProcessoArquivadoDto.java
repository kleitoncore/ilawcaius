package com.br.ilawgestao.domains.dto;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessoArquivadoDto {
	
	private long codigo;
	private String mes;
	private BigInteger total;
	
}
