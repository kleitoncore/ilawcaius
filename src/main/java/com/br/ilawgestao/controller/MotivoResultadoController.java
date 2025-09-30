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

import com.br.ilawgestao.domains.dto.MotivoResultadoDto;
import com.br.ilawgestao.domains.service.MotivoResultadoService;

@RestController
@RequestMapping("/motivoResultado")
public class MotivoResultadoController {
	
	@Autowired
	private MotivoResultadoService service;
	
	@PostMapping
	public ResponseEntity<MotivoResultadoDto> cadastrar(@RequestBody MotivoResultadoDto motivoDto) {
		MotivoResultadoDto dto = service.cadastrar(motivoDto);
		return new ResponseEntity<MotivoResultadoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{empresa}")
	public ResponseEntity<List<MotivoResultadoDto>> listar(@PathVariable long empresa) {
		List<MotivoResultadoDto> dtos = service.listar(empresa);
		return new ResponseEntity<List<MotivoResultadoDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/porCodigo/{codigo}")
	public ResponseEntity<MotivoResultadoDto> consultar(@PathVariable long codigo) {
		MotivoResultadoDto dto = service.consultar(codigo);
		return new ResponseEntity<MotivoResultadoDto>(dto, HttpStatus.OK);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<MotivoResultadoDto> alterar(@PathVariable long codigo, @RequestBody MotivoResultadoDto motivoDto) {
		MotivoResultadoDto dto = service.alterar(codigo, motivoDto);
		return new ResponseEntity<MotivoResultadoDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluir(@PathVariable long codigo) {
		service.excluir(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
