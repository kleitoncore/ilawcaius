package com.br.ilawgestao.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.br.ilawgestao.domains.models.TipoPagamento;
import com.br.ilawgestao.domains.service.TipoPagamentoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/tiposPagamento")
public class TipoPagamentoController {
	
	@Autowired
	private TipoPagamentoService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<TipoPagamento> cadastrarTipoPagamento(@RequestBody TipoPagamento tipoPagamanto, HttpServletResponse response) {
		TipoPagamento tipoSalvo = service.cadastrarTipoPagamento(tipoPagamanto);
		publish.publishEvent(new RecursoCriadoEvent(this, response, tipoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(tipoSalvo);
	}
	
	@GetMapping
	public List<TipoPagamento> listarTiposPagamentos(@RequestParam long empresa) {
		return service.listarTiposPagamentos(empresa);
	}
	
	@GetMapping("/{codigo}")
	public TipoPagamento consultarTipoPagamento(@PathVariable long codigo) {
		return service.consultarTipoPagamento(codigo);
	}
	
	@PutMapping("/{codigo}")
	public TipoPagamento alterarTipoPagamento(@PathVariable long codigo, @RequestBody TipoPagamento tipoPagamento) {
		return service.alterarTipoPagamento(codigo, tipoPagamento);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirTipoPagamento(@PathVariable long codigo) {
		service.excluirTipoPagamento(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/tiposPagamentosPorCodigos")
	public ResponseEntity<List<TipoPagamento>> consultarTiposPagamento(@RequestParam String codigos) {
		List<TipoPagamento> lista = service.consultarTiposPagamentoPorCodigos(codigos);
		return new ResponseEntity<List<TipoPagamento>>(lista, HttpStatus.OK);
	}
}
