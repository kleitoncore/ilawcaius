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
import com.br.ilawgestao.domains.models.TipoCusta;
import com.br.ilawgestao.domains.service.TipoCustaService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/tiposCusta")
public class TipoCustaController {
	
	@Autowired
	private TipoCustaService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<TipoCusta> cadastrarTipoCusta(@RequestBody TipoCusta tipoCusta, HttpServletResponse response) {
		TipoCusta tipoSalvo = service.cadastrarTipoCusta(tipoCusta);
		publish.publishEvent(new RecursoCriadoEvent(this, response, tipoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(tipoSalvo);
	}
	
	@GetMapping
	public List<TipoCusta> listarTiposCusta(@RequestParam long empresa) {
		return service.listarTiposCustas(empresa);
	}
	
	@GetMapping("/{codigo}")
	public TipoCusta consultarTipoCusta(@PathVariable long codigo) {
		return service.consultarTipoCusta(codigo);
	}
	
	@PutMapping("/{codigo}")
	public TipoCusta alterarTipoCusta(@PathVariable long codigo, @RequestBody TipoCusta tipoCusta) {
		return service.alterarTipoCusta(codigo, tipoCusta);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirTipoCusta(@PathVariable long codigo) {
		service.excluirTipoCusta(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/tiposCustasPorCodigos")
	public ResponseEntity<List<TipoCusta>> consultarTiposCustasPorCodigos(@RequestParam String codigos) {
		List<TipoCusta> lista = service.consultarTiposCustasPorCodigos(codigos);
		return new ResponseEntity<List<TipoCusta>>(lista, HttpStatus.OK);
	}
}
