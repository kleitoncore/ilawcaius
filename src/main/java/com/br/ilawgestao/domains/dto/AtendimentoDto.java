package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Atendimento;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.Processo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtendimentoDto {
	
	public Atendimento transformaParaObjeto() {
		return new Atendimento(codigo, pessoa, processo, grupo, assunto, dtAtendimento, empresa);
	}
	
	private long codigo;
	private Pessoa pessoa;
	private Processo processo;
	private GrupoTrabalho grupo;
	private String assunto;
	private String dtAtendimento;
	private Empresa empresa;
	
}
