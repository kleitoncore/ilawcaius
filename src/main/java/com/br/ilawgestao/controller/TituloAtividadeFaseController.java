package com.br.ilawgestao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.FaseTarefaDto;
import com.br.ilawgestao.domains.dto.TituloAtividadeDto;
import com.br.ilawgestao.domains.dto.TituloAtividadeFaseDto;
import com.br.ilawgestao.domains.service.TituloAtividadeFaseService;

@RestController
@RequestMapping("/tituloAtividadeFase")
public class TituloAtividadeFaseController {
	
	@Autowired
	private TituloAtividadeFaseService service;
	
	@PostMapping
	public ResponseEntity<TituloAtividadeFaseDto> cadastrar(@RequestBody TituloAtividadeFaseDto tituloFaseDto) {
		TituloAtividadeFaseDto dto = service.cadastrar(tituloFaseDto);
		return new ResponseEntity<TituloAtividadeFaseDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{titulo}")
	public ResponseEntity<List<FaseTarefaDto>> listarFasePorTitulo(@PathVariable long titulo) {
		List<FaseTarefaDto> dtos = service.fasesPorTitulos(titulo);
		return new ResponseEntity<List<FaseTarefaDto>>(dtos, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluir")
	public ResponseEntity excluir(@RequestParam long titulo, @RequestParam long fase) {
		service.excluir(titulo,fase);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/fasesNotInTitulo")
	public ResponseEntity<List<FaseTarefaDto>> listarNotintTitulo(@RequestParam long titulo, @RequestParam long empresa) {
		List<FaseTarefaDto> dtos = service.listarFasesNotInTitulo(titulo,empresa);
		return new ResponseEntity<List<FaseTarefaDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/titulosPorFase/{fase}")
	public ResponseEntity<List<TituloAtividadeDto>> listarTitulosPorFase(@PathVariable long fase) {
		List<TituloAtividadeDto> dtos = service.listarTitulosPorFase(fase);
		return new ResponseEntity<List<TituloAtividadeDto>>(dtos, HttpStatus.OK);
	}
}
