package com.br.ilawgestao.domains.dto;


import com.br.ilawgestao.domains.models.Atendimento;
import com.br.ilawgestao.domains.models.AtendimentoHistorico;
import com.br.ilawgestao.domains.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtendimentoHisoricoDto {
	
	public AtendimentoHistorico transformaParaObjeto() {
		return new AtendimentoHistorico(codigo, atendimento, usuario, historico, dtAtendimento);
	}
	
	private long codigo;
	private Atendimento atendimento;
	private Usuario usuario;
	private String historico;
	private String dtAtendimento;
}
