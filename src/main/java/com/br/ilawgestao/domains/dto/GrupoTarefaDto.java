package com.br.ilawgestao.domains.dto;


import java.util.List;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTarefa;
import com.br.ilawgestao.domains.models.Tarefa;
import com.br.ilawgestao.domains.models.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoTarefaDto {
	
	private long codigo;
	private String nome;
	private Empresa empresa;
	private String dataRegistro;
	private Usuario usuario;
	private String snAtivo;
	private List<Tarefa> tarefas;
	
	public static GrupoTarefaDto build(GrupoTarefa entity, List<Tarefa> tarefas) {
		GrupoTarefaDto dto = new GrupoTarefaDto();
		dto.setCodigo(entity.getCodigo());
		dto.setNome(entity.getNome());
		dto.setEmpresa(entity.getEmpresa());
		dto.setUsuario(entity.getUsuario());
		if(entity.getSnAtivo().equals("S")) {
			dto.setSnAtivo("ATIVO");
		} else {
			dto.setSnAtivo("INATIVO");
		}
		dto.setTarefas(tarefas);
		return dto;
	}
}
