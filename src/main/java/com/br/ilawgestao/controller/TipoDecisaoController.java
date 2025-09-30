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
import com.br.ilawgestao.domains.models.TipoDecisao;
import com.br.ilawgestao.domains.service.TipoDecisaoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/tiposDecisao")
public class TipoDecisaoController {
	
	@Autowired
	private TipoDecisaoService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<TipoDecisao> cadastrarTipoDecisao(@RequestBody TipoDecisao tipo, HttpServletResponse response) {
		TipoDecisao tipoSalvo = service.cadastrarTipoDecisao(tipo);
		publish.publishEvent(new RecursoCriadoEvent(this, response, tipoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(tipoSalvo);
	}
	
	@GetMapping
	public List<TipoDecisao> listarTiposDecisao(@RequestParam long empresa) {
		return service.listarTiposDecisao(empresa);
	}
	
	@GetMapping("/{codigo}")
	public TipoDecisao consultarTipoDecisao(@PathVariable long codigo) {
		return service.consultarTipoDecisao(codigo);
	}
	
	@PutMapping("/{codigo}")
	public TipoDecisao alterarTipoDecisao(@PathVariable long codigo, @RequestBody TipoDecisao tipo) {
		return service.alterarTipoDecisao(codigo, tipo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirTipoDecisao(@PathVariable long codigo) {
		service.excluirTipoDecisao(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
