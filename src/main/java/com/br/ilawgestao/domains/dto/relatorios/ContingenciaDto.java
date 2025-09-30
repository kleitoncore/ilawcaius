package com.br.ilawgestao.domains.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContingenciaDto {
	
	private double provavel;
	private double possivel;
	private double remoto;
	private double causa;
	
}
