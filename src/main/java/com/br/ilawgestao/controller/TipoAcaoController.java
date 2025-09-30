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
import com.br.ilawgestao.domains.models.TipoAcao;
import com.br.ilawgestao.domains.service.TipoAcaoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/tipoAcao")
public class TipoAcaoController {
	
	@Autowired
	private TipoAcaoService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<TipoAcao> cadastrarTipoAcao(@RequestBody TipoAcao tipoAcao, HttpServletResponse response) {
		TipoAcao tipoAcaoSalvo = service.cadastrarTipoAcao(tipoAcao);
		publish.publishEvent(new RecursoCriadoEvent(this, response, tipoAcaoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(tipoAcaoSalvo);
	}
	
	@GetMapping
	public List<TipoAcao> listarTipoAcao(@RequestParam long empresa) {
		return service.listarTipoAcao(empresa);
	}
	
	@GetMapping("/{codigo}")
	public TipoAcao consultarTipoAcao(@PathVariable long codigo) {
		return service.consultarTipoAcao(codigo);
	}
	
	@PutMapping("/{codigo}")
	public TipoAcao alterarTipoAcao(@PathVariable long codigo, @RequestBody TipoAcao tipoAcao) {
		return service.alterarTipoAcao(codigo, tipoAcao);
	}
	
	@DeleteMapping("/{codigo}")
	public void excluirTipoAcao(@PathVariable long codigo) {
		service.excluirTipoAcao(codigo);
	}
}
