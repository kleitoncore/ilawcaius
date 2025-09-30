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

import com.br.ilawgestao.domains.dto.FaseDto;
import com.br.ilawgestao.domains.service.FaseService;

@RestController
@RequestMapping("/fase")
public class FaseController {
	
	@Autowired
	private FaseService service;
	
	@PostMapping
	public ResponseEntity<FaseDto> cadastrar(@RequestBody FaseDto faseDto) {
		FaseDto dto = service.cadastrar(faseDto);
		return new ResponseEntity<FaseDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{empresa}")
	public ResponseEntity<List<FaseDto>> listar(@PathVariable long empresa) {
		List<FaseDto> dtos = service.listar(empresa);
		return new ResponseEntity<List<FaseDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/porCodigo/{codigo}")
	public ResponseEntity<FaseDto> consultar(@PathVariable long codigo) {
		return new ResponseEntity<FaseDto>(service.consultar(codigo), HttpStatus.OK);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<FaseDto> alterar(@PathVariable long codigo, @RequestBody FaseDto faseDto) {
		FaseDto dto = service.alterar(codigo,faseDto);
		return new ResponseEntity<FaseDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluir(@PathVariable long codigo) {
		service.excluir(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
