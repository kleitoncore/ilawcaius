package com.br.ilawgestao.domains.repository.filtros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroMiniAgenda {
	private String data;
	private String processo;
	private boolean isPrazoFatal;
	private String dataLimiteInicial;
	private String dataLimiteFinal;
	private String prazoFatalInicial;
	private String prazoFatalFinal;
	private String dataConclusaoInicial;
	private String dataConclusaoFinal;
	private String importante;
	private String urgente;
	private String tipoUsuario;
	private String partes;
	private int classificacao;
	private String ordenacao;
	private long usuario;
	private long empresa;
	private boolean isFinanceiro;
	private String status;
	private String fase;
}
