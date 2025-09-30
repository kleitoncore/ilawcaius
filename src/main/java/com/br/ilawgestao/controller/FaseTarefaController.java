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
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.FaseTarefaDto;
import com.br.ilawgestao.domains.service.FaseTarefaService;

@RestController
@RequestMapping("/faseTarefa")
public class FaseTarefaController {
	
	@Autowired
	private FaseTarefaService service;
	
	@PostMapping
	public ResponseEntity<FaseTarefaDto> cadastrar(@RequestBody FaseTarefaDto faseDto) {
		FaseTarefaDto dto = service.cadastrar(faseDto);
		return new ResponseEntity<FaseTarefaDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{empresa}")
	public ResponseEntity<List<FaseTarefaDto>> listar(@PathVariable long empresa) {
		List<FaseTarefaDto> dtos = service.listar(empresa);
		return new ResponseEntity<List<FaseTarefaDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/porCodigo/{codigo}")
	public ResponseEntity<FaseTarefaDto> consultar(@PathVariable long codigo) {
		FaseTarefaDto dto = service.consultar(codigo);
		return new ResponseEntity<FaseTarefaDto>(dto, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<FaseTarefaDto> alterar(@RequestBody FaseTarefaDto faseDto) {
		FaseTarefaDto dto = service.alterar(faseDto);
		return new ResponseEntity<FaseTarefaDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluir(@PathVariable long codigo) {
		service.excluir(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
