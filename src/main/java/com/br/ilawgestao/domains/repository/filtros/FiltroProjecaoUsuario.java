package com.br.ilawgestao.domains.repository.filtros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroProjecaoUsuario {
	private String usuario;
	private long ano;
	private long mes;
	private long empresa;
}
