package com.br.ilawgestao.domains.repository.filtros;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroAgenda {
	
	private String grupoTrabalho;
	private String usuarios;
	private String status;
	private String tipos;
	private String mes;
	private String ano;
	private long usuario;
	
}
