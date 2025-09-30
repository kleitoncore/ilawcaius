package com.br.ilawgestao.domains.dto;

import java.util.List;

import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtividadeCadastroDTO {
	private Atividade atividade;
	private long recorrencia;
	private List<Usuario> responsaveis;
	private List<Usuario> interessados;
	private List<Usuario> responsaveisSubTarefas;
	private List<Usuario> interessadosSubTarefas;
}
