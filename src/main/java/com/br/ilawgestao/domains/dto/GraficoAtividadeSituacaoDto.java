package com.br.ilawgestao.domains.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraficoAtividadeSituacaoDto {
	
	private BigDecimal atrasados;
	private BigDecimal concluidosAtrasados;
	private BigDecimal concluidosPrazo;
	
}
