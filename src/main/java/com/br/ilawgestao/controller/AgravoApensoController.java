package com.br.ilawgestao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.br.ilawgestao.domains.dto.AgravoApensoDto;
import com.br.ilawgestao.domains.dto.ProcessoDto;
import com.br.ilawgestao.domains.service.AgravoApensoService;

@RestController
@RequestMapping("/agravoApenso")
public class AgravoApensoController {
	
	@Autowired
	private AgravoApensoService service;
	
	@PostMapping
	public ResponseEntity<AgravoApensoDto> cadastrar(@RequestBody AgravoApensoDto agravoApensoDto) {
		AgravoApensoDto dto = service.cadastrar(agravoApensoDto);
		return new ResponseEntity<AgravoApensoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{processo}")
	public ResponseEntity<List<AgravoApensoDto>> listar(@PathVariable long processo) {
		List<AgravoApensoDto> dtos = service.listarProcessos(processo);
		return new ResponseEntity<List<AgravoApensoDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/numero")
	public ResponseEntity<ProcessoDto> consultarProcessoPorNumero(@RequestParam String numero, @RequestParam long empresa) {
		ProcessoDto dto = service.consultarProcessoExistens(numero, empresa);
		return new ResponseEntity<ProcessoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/processoExistente/{codigo}")
	public ResponseEntity<ProcessoDto> consultarProcessoExistente(@PathVariable long codigo) {
		ProcessoDto dto = service.consultarProcessoExistente(codigo);
		return new ResponseEntity<ProcessoDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirAgravopenso(@PathVariable long codigo) {
		service.excluirAgravoApenso(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/porProcesso/{processo}")
	public ResponseEntity<AgravoApensoDto> consultarAgravoApenso(@PathVariable long processo) {
		AgravoApensoDto dto = service.consultarAgravoApenso(processo);
		return new ResponseEntity<AgravoApensoDto>(dto, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<AgravoApensoDto> alterarAgravoApenso(@RequestBody AgravoApensoDto agravoApensoDto) {
		AgravoApensoDto dto = service.alterarAgravoApenso(agravoApensoDto);
		return new ResponseEntity<AgravoApensoDto>(dto, HttpStatus.OK);
	}
}
