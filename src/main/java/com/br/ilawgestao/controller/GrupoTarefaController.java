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

import com.br.ilawgestao.domains.dto.GrupoTarefaDto;
import com.br.ilawgestao.domains.service.GrupoTarefaService;

@RestController
@RequestMapping("/grupoTarefa")
public class GrupoTarefaController {
	
	@Autowired
	private GrupoTarefaService service;
	
	@PostMapping
	public ResponseEntity<GrupoTarefaDto> cadastrar(@RequestBody GrupoTarefaDto grupoDto) {
		GrupoTarefaDto dto = service.cadastrar(grupoDto);
		return new ResponseEntity<GrupoTarefaDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{empresa}")
	public ResponseEntity<List<GrupoTarefaDto>> listar(@PathVariable long empresa) {
		List<GrupoTarefaDto> dtos = service.listar(empresa);
		return new ResponseEntity<List<GrupoTarefaDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/porCodigo/{codigo}")
	public ResponseEntity<GrupoTarefaDto> consultar(@PathVariable long codigo) {
		GrupoTarefaDto dto = service.consultar(codigo);
		return new ResponseEntity<GrupoTarefaDto>(dto, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<GrupoTarefaDto> alterar(@RequestBody GrupoTarefaDto grupoDto) {
		GrupoTarefaDto dto = service.alterar(grupoDto);
		return new ResponseEntity<GrupoTarefaDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluir(@PathVariable long codigo) {
		service.excluir(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}	
