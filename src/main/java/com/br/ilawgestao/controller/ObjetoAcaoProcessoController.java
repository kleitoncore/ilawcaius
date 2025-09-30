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
import com.br.ilawgestao.domains.models.ObjetoAcaoProcesso;
import com.br.ilawgestao.domains.service.ObjetoAcaoProcessoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/objetoAcaoProcesso")
public class ObjetoAcaoProcessoController {
	
	@Autowired
	private ObjetoAcaoProcessoService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<ObjetoAcaoProcesso> cadastrarObjetoProcesso(@RequestBody ObjetoAcaoProcesso objetoProcesso, HttpServletResponse response) {
		ObjetoAcaoProcesso objetoSalvo = service.cadastrarObjetoProcesso(objetoProcesso);
		publish.publishEvent(new RecursoCriadoEvent(this, response, objetoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(objetoSalvo);
	}
	
	@GetMapping("/{processo}")
	public List<ObjetoAcaoProcesso> consultarObjetosPorProcesso(@PathVariable long processo) {
		return service.consultarObjetosPorProcesso(processo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirObjeto(@PathVariable long codigo) {
		service.excluirObjetoProcesso(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
