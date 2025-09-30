package com.br.ilawgestao.domains.dto;

import java.util.List;

import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.StatusAtividade;
import com.br.ilawgestao.domains.models.Usuario;

import lombok.Data;

@Data
public class AlterarAtividadeMassaDto {
	private List<Atividade> atividades;
	private String dataLimite;
	private StatusAtividade status;
	private String historico;
	private Usuario usuario;
}
