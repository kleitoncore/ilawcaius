package com.br.ilawgestao.domains.dto;



import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniAgendaDto {
	private Date data;
	private String mes;
	private String ano;
	private String dia;
	private String diaSemana;
	private BigInteger quantidadeTarefas;
}
