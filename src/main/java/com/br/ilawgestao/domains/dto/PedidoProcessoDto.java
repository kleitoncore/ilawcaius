package com.br.ilawgestao.domains.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.br.ilawgestao.domains.models.Pedido;
import com.br.ilawgestao.domains.models.PedidoProcesso;
import com.br.ilawgestao.domains.models.Processo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoProcessoDto {
	private Long codigo;
	private Pedido pedido;
	private Processo processo;
	private String vlRemoto;
	private String vlPossivel;
	private String vlProvavel;
	private String vlCausa;
	private String dtRegistro;
	
	public static PedidoProcessoDto build(PedidoProcesso pedidoProcesso) {
		PedidoProcessoDto dto = new PedidoProcessoDto();
		dto.setCodigo(pedidoProcesso.getCodigo());
		dto.setPedido(pedidoProcesso.getPedido());
		dto.setProcesso(pedidoProcesso.getProcesso());
		Locale ptBr = new Locale("pt", "BR");
		NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
		
		BigDecimal valorPossivel = new BigDecimal(pedidoProcesso.getVlPossivel());
		String formatoValorPossivel = nf.format(valorPossivel);
		dto.setVlPossivel(formatoValorPossivel);
		
		BigDecimal valorRemoto = new BigDecimal(pedidoProcesso.getVlRemoto());
		String formatoValorRemoto = nf.format(valorRemoto);
		dto.setVlRemoto(formatoValorRemoto);
		
		BigDecimal valorProvavel = new BigDecimal(pedidoProcesso.getVlProvavel());
		String formatoValorProvavel = nf.format(valorProvavel);
		dto.setVlProvavel(formatoValorProvavel);
		
		BigDecimal valorCausa = new BigDecimal(pedidoProcesso.getVlCausa());
		String formatoValorCausa = nf.format(valorCausa);
		dto.setVlCausa(formatoValorCausa);
		
		dto.setDtRegistro(pedidoProcesso.getDtRegistro());
		return dto;
	}
	
	public static PedidoProcessoDto buildSimples(PedidoProcesso pedidoProcesso) {
		PedidoProcessoDto dto = new PedidoProcessoDto();
		dto.setCodigo(pedidoProcesso.getCodigo());
		dto.setPedido(pedidoProcesso.getPedido());
		dto.setProcesso(pedidoProcesso.getProcesso());
		dto.setVlPossivel(String.valueOf(pedidoProcesso.getVlPossivel()));
		dto.setVlRemoto(String.valueOf(pedidoProcesso.getVlRemoto()));
		dto.setVlProvavel(String.valueOf(pedidoProcesso.getVlProvavel()));		
		dto.setDtRegistro(pedidoProcesso.getDtRegistro());
		return dto;
	}
}
