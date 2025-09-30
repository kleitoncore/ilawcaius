package com.br.ilawgestao.domains.dto;

import java.util.List;

import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.Tarefa;
import com.br.ilawgestao.domains.models.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtividadeAgrupadaDto {
	private List<TarefaDto> tarefas;
	private long grupoTarefa;
	private String dataLimite;
	private Processo processo;
	private GrupoTrabalho grupo;
	private Usuario usuario;
	private String descricao;
}
