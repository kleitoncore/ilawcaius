package com.br.ilawgestao.controller;

import java.util.List;

import com.br.ilawgestao.domains.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.models.Lancamento;
import com.br.ilawgestao.domains.models.Mes;
import com.br.ilawgestao.domains.service.LancamentoService;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {
	
	@Autowired
	private LancamentoService lancamentoService;
		
	@GetMapping("/meses")
	public List<Mes> listarMeses() {
		return lancamentoService.listarMeses();
	}
	
	@PostMapping
	public ResponseEntity<LancamentoDto> cadastrarLancamento(@RequestBody LancamentoDto lancamentoDto) {
		LancamentoDto dto = lancamentoService.cadastrarLancamento(lancamentoDto);
		return new ResponseEntity<LancamentoDto>(dto, HttpStatus.OK);
	}

	@PutMapping("/alterar")
	public ResponseEntity<LancamentoDto> alterarLancamento(@RequestBody LancamentoDto lancamento) {
		LancamentoDto dto = lancamentoService.alterarLancamento(lancamento);
		return new ResponseEntity<LancamentoDto>(dto,HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/parcelada")
	public ResponseEntity cadastrarLancamentoParcelada(@RequestBody LancamentoDto dto, @RequestParam long parcelas, @RequestParam String recorrencia) {
		lancamentoService.cadastrarLancamentoParcelada(dto, parcelas, recorrencia);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<LancamentoDto> consultarLancamento(@PathVariable long codigo) {
		LancamentoDto dto = lancamentoService.consultarLancamento(codigo);
		return new ResponseEntity<LancamentoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/lancamentosDespesaDatas")
	public List<LancamentoPorTipoDto> lancamentosTipoDespesaDatas(@RequestParam long empresa) {
		return lancamentoService.lancamentosPoTipoDespesaData(empresa);
	}
	
	@GetMapping("/lancamentosReceitaDatas")
	public List<LancamentoPorTipoDto> lancamentosTipoReceitaDatas(@RequestParam long empresa) {
		return lancamentoService.lancamentosPoTipoReceitaData(empresa);
	}
	
	@GetMapping("/lancamentosPorTipoPeriodo")
	public List<Lancamento> lancamentosPorTipoPeriodo(@RequestParam long empresa, @RequestParam String dataInicial, String dataFinal, String tipo) {
		return lancamentoService.lancamentosPorTipoPeriodo(empresa, dataInicial, dataFinal, tipo);
	}
	
	@GetMapping("/lancamentosPorTipoDescricaoPeriodo")
	public List<Lancamento> lancamentosPorTipoDescricaoPeriodo(@RequestParam long empresa, @RequestParam String dataInicial, String dataFinal,
			@RequestParam long tipoCodigo, @RequestParam String tipo, @RequestParam String situacao) {
		return lancamentoService.lancamentosPorTipoDescricaoPeriodo(empresa, dataInicial, dataFinal, tipoCodigo, tipo, situacao);
	}
	
	@GetMapping("/lancamentosGrafico")
	public List<LancamentoPorTipoGraficoDto> lancamentosGrafico(@RequestParam long empresa, @RequestParam String dataInicial,
			String dataFinal, String tipo) {
		return lancamentoService.lancamentoGrafico(empresa, dataInicial, dataFinal, tipo);
	}
	
	@PutMapping("/pagarOuCancelarLancamento/{codigo}")
	public Lancamento pagarOuCancelarLancamento(@PathVariable long codigo, @RequestBody LancamentoDto lancamento) {
		return lancamentoService.pagarOuCancelarLancamento(codigo, lancamento);
	}
	
	@PutMapping("/pagarOuCancelarLancamentoIndividual/{codigo}")
	public ResponseEntity<LancamentoDto> pagarOuCancelarLancamentoIndividual(@PathVariable long codigo, @RequestBody LancamentoDto lancamento) {
		LancamentoDto dto = lancamentoService.pagarOuCancelarLancamentoIndividual(codigo,lancamento);
		return new ResponseEntity<LancamentoDto>(dto,HttpStatus.OK);
	}
	
	@PutMapping("/reabrirLancamento/{codigo}")
	public Lancamento reabrirLancamento(@PathVariable long codigo, @RequestBody LancamentoDto lancamento) {
		return lancamentoService.reabrirLancamento(codigo, lancamento);
	}
	
	@GetMapping("/relatorioDetalhado")
	public ResponseEntity<List<LancamentoDto>> relatorioDetalhado(@RequestParam long empresa, 
											   @RequestParam String dataInicial, 
											   @RequestParam String dataFinal, 
											   @RequestParam String tipo, 
											   @RequestParam String categoria, 
											   @RequestParam String situacao,
											   @RequestParam String codigo,
											   @RequestParam String processo,
											   @RequestParam String descricao,
											   @RequestParam String classificacao, 
											   @RequestParam String ordem) {
		List<LancamentoDto> dtos = lancamentoService.relatorioDetalhado(empresa, dataInicial, dataFinal, Long.parseLong(tipo), 
				categoria, situacao, Long.parseLong(codigo), Long.parseLong(processo), descricao, classificacao, ordem);
		return new ResponseEntity<List<LancamentoDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/relatorioResumido")
	public List<RelatorioFinanceiroResumidoDto> relatorioResumido(@RequestParam long empresa, 
																  @RequestParam String dataInicial, 
																  @RequestParam String dataFinal, 
																  @RequestParam String situacao, 
																  @RequestParam String relatorio) {
		return lancamentoService.relatorioFinanceiroResumido(empresa, dataInicial, dataFinal, situacao, relatorio);
	}
	
	@GetMapping("/vencendoAmanha/{empresa}")
	public double vencendoAmanha(@PathVariable long empresa) {
		return lancamentoService.vencendoAmanha(empresa);
	}
	
	@GetMapping("/vencendoHoje/{empresa}")
	public double vencendoHoje(@PathVariable long empresa) {
		return lancamentoService.vencendoHoje(empresa);
	}
	
	@GetMapping("/pendente/{empresa}")
	public double pendente(@PathVariable long empresa) {
		return lancamentoService.pendentes(empresa);
	}
	
	@GetMapping("/detalharPendentes")
	public List<Lancamento> detalharPendentes(@RequestParam long empresa, @RequestParam(required = false) String tipoCodigo,
			@RequestParam(required = false) String tipo) {
		if(tipoCodigo == null) {
			tipoCodigo = "0";
		}
		return lancamentoService.detalharPendentes(empresa, Long.parseLong(tipoCodigo), tipo);
	}
	
	@GetMapping("/detalharVencendoHoje")
	public List<Lancamento> detalharVncendoHoje(@RequestParam long empresa, @RequestParam(required = false) String tipoCodigo,
			@RequestParam(required = false) String tipo) {
		
		if(tipoCodigo == "") {
			tipoCodigo = "0";
		}
		return lancamentoService.detalharVencendoHoje(empresa, Long.parseLong(tipoCodigo), tipo);
	}

	@GetMapping("/lancamentos-por-conta")
	public ResponseEntity<List<LancamentosPorContaFinanceiraDto>> lancamentosPorConta(@RequestParam long empresa,
																					  @RequestParam String dataInicial,
																					  @RequestParam String dataFinal,
																					  @RequestParam String tipo) {
		List<LancamentosPorContaFinanceiraDto> dtos = lancamentoService.lancamentosPorConta(dataInicial,dataFinal,empresa,tipo);
		return new ResponseEntity<List<LancamentosPorContaFinanceiraDto>>(dtos, HttpStatus.OK);
	}
}
