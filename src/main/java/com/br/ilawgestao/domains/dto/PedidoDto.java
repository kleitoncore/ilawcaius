package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.Pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {
	
	private Long codigo;
	private String nome;
	private Empresa empresa;
	
	public static PedidoDto build(Pedido pedido) {
		PedidoDto dto = new PedidoDto();
		dto.setCodigo(pedido.getCodigo());
		dto.setNome(pedido.getNome());
		dto.setEmpresa(pedido.getEmpresa());
		return dto;
	}
}
