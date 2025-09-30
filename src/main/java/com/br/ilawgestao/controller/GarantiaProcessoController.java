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
import com.br.ilawgestao.domains.models.GarantiaProcesso;
import com.br.ilawgestao.domains.service.GarantiaProcessoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/garantiaProcesso")
public class GarantiaProcessoController {
	
	@Autowired
	private GarantiaProcessoService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<GarantiaProcesso> cadastrarGarantiaProcesso(@RequestBody GarantiaProcesso garantia, HttpServletResponse response) {
		GarantiaProcesso garantiaSalva = service.cadastrarGarantiaProcesso(garantia);
		publish.publishEvent(new RecursoCriadoEvent(this, response, garantiaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(garantiaSalva);
	}
	
	@GetMapping("/{processo}")
	public List<GarantiaProcesso> consultarGarantias(@PathVariable long processo) {
		return service.consultarGarantiasProcesso(processo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity escluirGarantia(@PathVariable long codigo) {
		service.excluirGarantia(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
