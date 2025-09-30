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

import com.br.ilawgestao.domains.dto.TipoDespesaeceitaDto;
import com.br.ilawgestao.domains.models.TipoDespesaReceita;
import com.br.ilawgestao.domains.service.TipoDespesaReceitaService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/tipoDespesaReceita")
public class TipoDespesareceitaController {
	
	@Autowired
	private TipoDespesaReceitaService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<TipoDespesaReceita> cadastrarTipoDespesaReceita(@RequestBody TipoDespesaeceitaDto tipoDto, HttpServletResponse response) {
		TipoDespesaReceita tipo = service.cadastrarTipoDespesaReceita(tipoDto.tranformeParaObjeto());
		publish.publishEvent(new RecursoCriadoEvent(this, response, tipo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(tipo);
	}
	
	@GetMapping("/{empresa}")
	public List<TipoDespesaReceita> listarTiposDespesasReceitas(@PathVariable long empresa) {
		return service.listarTiposDespesasReceitas(empresa);
	}
	
	@GetMapping("/porCodigo/{codigo}")
	public TipoDespesaReceita consultarTipo(@PathVariable long codigo) {
		return service.consultarTipo(codigo);
	}
	
	@PutMapping("/{codigo}")
	public TipoDespesaReceita alterarTipo(@PathVariable long codigo, @RequestBody TipoDespesaeceitaDto tipoDto) {
		return service.alterarTipoDespesaReceita(codigo, tipoDto.tranformeParaObjeto());
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirTipoDespesareceita(@PathVariable long codigo) {
		service.excluirTipoDespesaReceita(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/despesasReceitasPorTipo")
	public List<TipoDespesaReceita> listarTiposDespesasReceitasPorTipo(@RequestParam long empresa, @RequestParam String tipo) {
		return service.listarTiposDespesasReceitasPorTipo(empresa, tipo);
	}
}
