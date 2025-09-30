package com.br.ilawgestao.domains.dto;



import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PontuacaoUsuarioDto {
	private long dia;
	private BigDecimal pontos;
}
