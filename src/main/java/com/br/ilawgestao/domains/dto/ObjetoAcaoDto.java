package com.br.ilawgestao.domains.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ObjetoAcaoDto {
	private long codigo;
	private String nome;
}
