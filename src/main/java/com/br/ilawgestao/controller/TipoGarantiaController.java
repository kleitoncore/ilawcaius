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

import com.br.ilawgestao.domains.models.TipoGarantia;
import com.br.ilawgestao.domains.service.TipoGarantiaService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/tiposGarantias")
public class TipoGarantiaController {
	
	@Autowired
	private TipoGarantiaService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<TipoGarantia> cadastrarTipoGarantia(@RequestBody TipoGarantia tipoGarantia, HttpServletResponse response) {
		TipoGarantia tipoSalvo = service.cadastrarTipoGarantia(tipoGarantia);
		publish.publishEvent(new RecursoCriadoEvent(this, response, tipoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(tipoSalvo);
	}
	
	@GetMapping
	public List<TipoGarantia> listarTiposGarantias(@RequestParam long empresa) {
		return service.listarTiposGarantias(empresa);
	}
	
	@GetMapping("/{codigo}")
	public TipoGarantia consultarTipoGarantia(@PathVariable long codigo) {
		return service.consultarTipoGarantia(codigo);
	}
	
	@PutMapping("/{codigo}")
	public TipoGarantia alterarTipoGarantia(@PathVariable long codigo, @RequestBody TipoGarantia tipoGarantia) {
		return service.alterarTipoGarantioa(codigo, tipoGarantia);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirTipoGarantia(@PathVariable long codigo) {
		service.excluirTipoGarantia(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
