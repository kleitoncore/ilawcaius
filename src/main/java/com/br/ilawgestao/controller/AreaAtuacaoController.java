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
import com.br.ilawgestao.domains.models.AreaAtuacao;
import com.br.ilawgestao.domains.service.AreaAtuacaoServicve;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/areaAtuacao")
public class AreaAtuacaoController {
	
	@Autowired
	private AreaAtuacaoServicve service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<AreaAtuacao> cadastrarAreaAtuacao(@RequestBody AreaAtuacao areaAtuacao, HttpServletResponse response) {
		AreaAtuacao areaAtuacaoSalva = service.cadastrarAreaAtuacao(areaAtuacao);
		publish.publishEvent(new RecursoCriadoEvent(this, response, areaAtuacaoSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(areaAtuacaoSalva);
	}
	
	@GetMapping
	public List<AreaAtuacao> listarAreaAtuacao(@RequestParam long empresa) {
		return service.listarAreaAtuacao(empresa);
	}
	
	@GetMapping("/{codigo}")
	public AreaAtuacao consultarAreaAtuacao(@PathVariable long codigo) {
		return service.consultarAreaAtuacao(codigo);
	}
	
	@PutMapping("/{codigo}")
	public AreaAtuacao alterarAreaAtuacao(@PathVariable long codigo, @RequestBody AreaAtuacao areaAtuacao) {
		return service.alterarAreaAtuacao(codigo, areaAtuacao);
	}
	
	@DeleteMapping("/{codigo}")
	public void excluirAreaAtuacao(@PathVariable long codigo) {
		service.excluirAreaAtuacao(codigo);
	}
}
