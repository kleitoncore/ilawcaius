package com.br.ilawgestao.domains.dto;

import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaCalendarioHomeDto {
	
	private Date dataCompromisso;
	private BigInteger atividades;
	
}
