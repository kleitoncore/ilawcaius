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
import com.br.ilawgestao.domains.models.PagamentoProcesso;
import com.br.ilawgestao.domains.service.PagamentoProcessoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/pagamentoProcesso")
public class PagamentoProcessoController {
	
	@Autowired
	private PagamentoProcessoService service;
	
	@Autowired
	private ApplicationEventPublisher publish; 
	
	@PostMapping
	public ResponseEntity<PagamentoProcesso> cadastrarPagamento(@RequestBody PagamentoProcesso pagamento, HttpServletResponse response) {
		PagamentoProcesso pagamentoSalvo = service.cadastrarPagamentoProcesso(pagamento);
		publish.publishEvent(new RecursoCriadoEvent(this, response, pagamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoSalvo);
	}
	
	@GetMapping("/{processo}")
	public List<PagamentoProcesso> consultarPagamentos(@PathVariable long processo) {
		return service.consultarPagamentos(processo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirPagamento(@PathVariable long codigo) {
		service.excluirPagamento(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
