package com.br.ilawgestao.domains.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.br.ilawgestao.domains.models.Lancamento;
import com.br.ilawgestao.domains.models.TipoDespesaReceita;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.utils.DatasUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LancamentoDto {
	private long codigo;
	private String dtLancamento;
	private String dtVencimento;
	private double vlLancamento;
	private double vlPago;
	private String dtPago;
	private TipoDespesaReceita tipo;
	private String dsLancamento;
	private String observacao;
	private Usuario usuario;
	private ProcessoDto processo;
	private String status;
	private String situacao;
	private String categoria;
	private String valorLancamentoFormat;
	private String valorPagamentoFormat;
	private String criaAtividade;
	private ContaFinanceiraDto conta;
	
	public static LancamentoDto build(Lancamento lancamento, ProcessoDto processoDto) {
		LancamentoDto dto = new LancamentoDto();
		dto.setCodigo(lancamento.getCodigo());
		dto.setDtLancamento(lancamento.getDtLancamento());
		dto.setDtVencimento(lancamento.getDtVencimento());
		dto.setVlLancamento(lancamento.getVlLancamento());
		dto.setVlPago(lancamento.getVlPago());
		dto.setVlPago(lancamento.getVlPago());
		dto.setDtPago(lancamento.getDtPago());
		dto.setTipo(lancamento.getTipo());
		dto.setDsLancamento(lancamento.getDsLancamento());
		dto.setObservacao(lancamento.getObservacao());
		dto.setUsuario(lancamento.getUsuario());
		dto.setProcesso(processoDto);
		dto.setStatus(lancamento.getStatus());
		dto.setSituacao(lancamento.getSituacao());
		return dto;
	}
	
	public static Lancamento build(LancamentoDto dto) {
		Lancamento lanc = new Lancamento();
		lanc.setCodigo(dto.getCodigo());
		lanc.setDtLancamento(dto.getDtLancamento());
		lanc.setDtVencimento(dto.getDtVencimento());
		lanc.setVlLancamento(dto.getVlLancamento());
		lanc.setVlPago(dto.getVlPago());
		lanc.setVlPago(dto.getVlPago());
		lanc.setDtPago(dto.getDtPago());
		lanc.setTipo(dto.getTipo());
		lanc.setDsLancamento(dto.getDsLancamento());
		lanc.setObservacao(dto.getObservacao());
		lanc.setUsuario(dto.getUsuario());
		lanc.setStatus(dto.getStatus());
		lanc.setSituacao(dto.getSituacao());
		return lanc;
	}

	public static LancamentoDto build(Lancamento lancamento) {
		LancamentoDto dto = new LancamentoDto();
		dto.setCodigo(lancamento.getCodigo());
		dto.setDtLancamento(lancamento.getDtLancamento());
		dto.setDtVencimento(lancamento.getDtVencimento());
		dto.setVlLancamento(lancamento.getVlLancamento());
		dto.setVlPago(lancamento.getVlPago());
		dto.setVlPago(lancamento.getVlPago());
		dto.setDtPago(lancamento.getDtPago());
		dto.setTipo(lancamento.getTipo());
		dto.setDsLancamento(lancamento.getDsLancamento());
		dto.setObservacao(lancamento.getObservacao());
		dto.setUsuario(lancamento.getUsuario());
		dto.setStatus(lancamento.getStatus());
		dto.setSituacao(lancamento.getSituacao());
		return dto;
	}
	
	public static LancamentoDto buildConsulta(Lancamento lancamento, ProcessoDto processoDto, ContaFinanceiraDto conta) {
		LancamentoDto dto = new LancamentoDto();
		dto.setCodigo(lancamento.getCodigo());
		dto.setDtLancamento(DatasUtil.formatarDataTela(lancamento.getDtLancamento()));
		dto.setDtVencimento(DatasUtil.formatarDataTela(lancamento.getDtVencimento()));
		
		Locale ptBr = new Locale("pt", "BR");
		NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
		BigDecimal valorLancamento = new BigDecimal(lancamento.getVlLancamento());
		String formato = nf.format(valorLancamento);
		dto.setValorLancamentoFormat(formato);
		dto.setVlLancamento(lancamento.getVlLancamento());
		
		BigDecimal valorPago = new BigDecimal(lancamento.getVlPago());
		String formatoValorPago = nf.format(valorPago);
		dto.setValorPagamentoFormat(formatoValorPago);
		dto.setVlPago(lancamento.getVlPago());
		
		if(lancamento.getDtPago() != null) {
			dto.setDtPago(DatasUtil.formatarDataTela(lancamento.getDtPago()));
		} else {
			dto.setDtPago(null);
		}
		
		dto.setTipo(lancamento.getTipo());
		dto.setDsLancamento(lancamento.getDsLancamento());
		dto.setObservacao(lancamento.getObservacao());
		dto.setUsuario(lancamento.getUsuario());
		if(lancamento.getProcesso() != null) {
			dto.setProcesso(processoDto);
		} else {
			dto.setProcesso(null);
		}
		if(lancamento.getSituacao().equals("P")) {
			dto.setSituacao("Pago");
		} else if(lancamento.getSituacao().equals("A")) {
			dto.setSituacao("Ativo");
		} else {
			dto.setSituacao("Cancelada");
		}
		if(lancamento.getTipo().getTipo().equals("D")) {
			dto.setCategoria("Despesa");
		} else {
			dto.setCategoria("Receita");
		}
		dto.setConta(conta);

		return dto;
	}
}
