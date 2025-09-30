package com.br.ilawgestao.domains.dto;

import java.util.List;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.Tarefa;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.models.UsuarioTarefa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarefaDto {
	private long codigo;
	private String titulo;
	private long prazo;
	private String dataRegistro;
	private Usuario usuario;
	private Empresa empresa;
	private String snAtivo;
	private long pontuacao;
	private String tempo;
	private List<UsuarioTarefa> responsaveis;
	private Usuario responsavel;

	public static TarefaDto build1(Tarefa entity, List<UsuarioTarefa> responsaveis) {
		TarefaDto dto = new TarefaDto();
		dto.setCodigo(entity.getCodigo());
		dto.setTitulo(entity.getTitulo());
		dto.setPrazo(entity.getPrazo());
		dto.setDataRegistro(entity.getDataRegistro());
		dto.setUsuario(entity.getUsuario());
		dto.setEmpresa(entity.getEmpresa());
		if(entity.getSnAtivo().equals("S")) {
			dto.setSnAtivo("ATIVO");
		} else {
			dto.setSnAtivo("INATIVO");
		}
		dto.setPontuacao(entity.getPontuacao());
		if(entity.getTempo().equals("P")) {
			dto.setTempo("Progressiva");
		} else {
			dto.setTempo("Regressiva");
		}
		dto.setResponsaveis(responsaveis);
		return dto;
	}

	public static TarefaDto build(Tarefa entity, Usuario responsavel) {
		TarefaDto dto = new TarefaDto();
		dto.setCodigo(entity.getCodigo());
		dto.setTitulo(entity.getTitulo());
		dto.setPrazo(entity.getPrazo());
		dto.setDataRegistro(entity.getDataRegistro());
		dto.setUsuario(entity.getUsuario());
		dto.setEmpresa(entity.getEmpresa());
		if(entity.getSnAtivo().equals("S")) {
			dto.setSnAtivo("ATIVO");
		} else {
			dto.setSnAtivo("INATIVO");
		}
		dto.setPontuacao(entity.getPontuacao());
		if(entity.getTempo().equals("P")) {
			dto.setTempo("Progressiva");
		} else {
			dto.setTempo("Regressiva");
		}
		dto.setResponsavel(responsavel);
		return dto;
	}
}
