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

import com.br.ilawgestao.domains.dto.RitoDto;
import com.br.ilawgestao.domains.service.RitoService;

@RestController
@RequestMapping("/rito")
public class RitoController {
	
	@Autowired
	private RitoService service;
	
	@PostMapping
	public ResponseEntity<RitoDto> cadastrar(@RequestBody RitoDto ritoDto) {
		RitoDto dto = service.cadastrar(ritoDto);
		return new ResponseEntity<RitoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{empresa}")
	public ResponseEntity<List<RitoDto>> listar(@PathVariable long empresa) {
		List<RitoDto> dtos = service.listar(empresa);
		return new ResponseEntity<List<RitoDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/porCodigo/{codigo}")
	public ResponseEntity<RitoDto> consultar(@PathVariable long codigo) {
		return new ResponseEntity<RitoDto>(service.consultar(codigo), HttpStatus.OK);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<RitoDto> alterar(@PathVariable long codigo, @RequestBody RitoDto ritoDto) {
		RitoDto dto = service.alterar(codigo,ritoDto);
		return new ResponseEntity<RitoDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluir(@PathVariable long codigo) {
		service.excluir(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
