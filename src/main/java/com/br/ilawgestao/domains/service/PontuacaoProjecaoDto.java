package com.br.ilawgestao.domains.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PontuacaoProjecaoDto {
	private int dias;
	private int pontos;
}
