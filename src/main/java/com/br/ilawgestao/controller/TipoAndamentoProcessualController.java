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

import com.br.ilawgestao.domains.dto.TipoAndamentoProcessualDto;
import com.br.ilawgestao.domains.service.TipoAndamentoProcessualService;

@RestController
@RequestMapping("/tipoAndamento")
public class TipoAndamentoProcessualController {
	
	@Autowired
	private TipoAndamentoProcessualService service;
	
	@PostMapping
	public ResponseEntity<TipoAndamentoProcessualDto> cadastrar(@RequestBody TipoAndamentoProcessualDto tipoDto) {
		TipoAndamentoProcessualDto dto = service.cadastrar(tipoDto);
		return new ResponseEntity<TipoAndamentoProcessualDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{empresa}")
	public ResponseEntity<List<TipoAndamentoProcessualDto>> listar(@PathVariable long empresa) {
		List<TipoAndamentoProcessualDto> dtos = service.listar(empresa);
		return new ResponseEntity<List<TipoAndamentoProcessualDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/porCodigo/{codigo}")
	public ResponseEntity<TipoAndamentoProcessualDto> consultar(@PathVariable long codigo) {
		TipoAndamentoProcessualDto dto = service.consultar(codigo);
		return new ResponseEntity<TipoAndamentoProcessualDto>(dto, HttpStatus.OK);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<TipoAndamentoProcessualDto> alterar(@PathVariable long codigo, @RequestBody TipoAndamentoProcessualDto tipoDto) {
		TipoAndamentoProcessualDto dto = service.alterar(codigo, tipoDto);
		return new ResponseEntity<TipoAndamentoProcessualDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirTipo(@PathVariable long codigo) {
		service.excluirTipo(codigo);
		return new ResponseEntity(HttpStatus.OK);
	}
}
