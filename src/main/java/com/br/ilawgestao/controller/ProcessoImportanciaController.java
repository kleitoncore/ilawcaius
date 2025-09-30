package com.br.ilawgestao.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.models.ProcessoImportancia;
import com.br.ilawgestao.domains.service.ProcessoImportanciaService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/processoImportancia")
public class ProcessoImportanciaController {
	
	@Autowired
	private ProcessoImportanciaService service;
	
	@Autowired
	private ApplicationEventPublisher publish;

	@PostMapping
	public ResponseEntity<ProcessoImportancia> incluirProcessoImportancia(@RequestBody ProcessoImportancia processoImportancia, 
			HttpServletResponse response) {
		ProcessoImportancia processoImportanciaSalvo = service.incluirProcesso(processoImportancia);
		publish.publishEvent(new RecursoCriadoEvent(this, response, processoImportanciaSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(processoImportanciaSalvo);
	}
	
	@GetMapping
	public ProcessoImportancia consultarProcessoImportancia(@RequestParam long usuario, long processo) {
		return service.consultarProcessoImportancia(usuario, processo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirProcessoImportancia(@PathVariable long codigo) {
		service.excluirProcessoImportancia(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
