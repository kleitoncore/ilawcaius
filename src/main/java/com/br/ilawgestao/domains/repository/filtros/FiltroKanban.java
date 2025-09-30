package com.br.ilawgestao.domains.repository.filtros;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroKanban {
	
	private String grupo;
	private long empresa;
	private String usuario;
	private String dataLimiteInicial;
	private String dataLimiteFinal;
	private String status;
	private long limite;
}
