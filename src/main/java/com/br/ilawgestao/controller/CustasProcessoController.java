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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.br.ilawgestao.domains.models.CustasProcesso;
import com.br.ilawgestao.domains.service.CustasProcessoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/custasProcesso")
public class CustasProcessoController {
	
	@Autowired
	private CustasProcessoService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<CustasProcesso> cadastrarCustasProcesso(@RequestBody CustasProcesso custas, HttpServletResponse response) {
		CustasProcesso custasSalva = service.cadastrarCustasProcesso(custas);
		publish.publishEvent(new RecursoCriadoEvent(this, response, custasSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(custasSalva);
	}
	
	@GetMapping("/{codigo}")
	public List<CustasProcesso> consultarCustasProcesso(@PathVariable long codigo) {
		return service.consultarCustasProcesso(codigo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirCustasProcesso(@PathVariable long codigo) {
		service.excluirCustasProcesso(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
