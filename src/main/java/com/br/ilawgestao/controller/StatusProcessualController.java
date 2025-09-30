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
import com.br.ilawgestao.domains.models.StatusProcessual;
import com.br.ilawgestao.domains.service.StatusProcessualService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/statusProcessual")
public class StatusProcessualController {
	
	@Autowired
	private StatusProcessualService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<StatusProcessual> cadastrarStatusProcessual(@RequestBody StatusProcessual status, HttpServletResponse response) {
		StatusProcessual statusSalvo = service.cadastrarStatusProcessual(status);
		publish.publishEvent(new RecursoCriadoEvent(this, response, statusSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(statusSalvo);
	}
	
	@GetMapping
	public List<StatusProcessual> listarStatusProcessual(@RequestParam long empresa) {
		return service.listarStatusProcessuais(empresa);
	}
	
	@GetMapping("/{codigo}")
	public StatusProcessual consultarStatusProcessual(@PathVariable long codigo) {
		return service.consultarStatusProcessual(codigo);
	}
	
	@PutMapping("/{codigo}")
	public StatusProcessual alterarStatusProcessual(@PathVariable long codigo, @RequestBody StatusProcessual status) {
		return service.alterarStatusProcessual(codigo, status);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirStatusProcessual(@PathVariable long codigo) {
		service.excluirStatusProcessual(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
