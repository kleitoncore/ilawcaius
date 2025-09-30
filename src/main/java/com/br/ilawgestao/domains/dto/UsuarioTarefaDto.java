package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.AgrupamentoTarefa;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.models.UsuarioTarefa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioTarefaDto {
	private long codigo;
	private Usuario usuario;
	private String tipo;
	private AgrupamentoTarefa agrupamento;
	
	public static UsuarioTarefaDto build(UsuarioTarefa entity) {
		UsuarioTarefaDto dto = new UsuarioTarefaDto();
		dto.setCodigo(entity.getCodigo());
		dto.setUsuario(entity.getUsuario());
		dto.setTipo(entity.getTipo());
		dto.setAgrupamento(entity.getAgrupamento());
		return dto;
	}
}
