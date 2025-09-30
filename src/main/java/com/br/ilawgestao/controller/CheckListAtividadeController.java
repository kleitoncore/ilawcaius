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

import com.br.ilawgestao.domains.dto.CheckListAtividadeDto;
import com.br.ilawgestao.domains.service.CheckListAtividadeService;

@RestController
@RequestMapping("/checkListAtividade")
public class CheckListAtividadeController {
	
	@Autowired
	private CheckListAtividadeService service;
	
	@PostMapping
	public ResponseEntity<CheckListAtividadeDto> cadastrar(@RequestBody CheckListAtividadeDto checkList) {
		CheckListAtividadeDto dto = service.cadastrar(checkList);
		return new ResponseEntity<CheckListAtividadeDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/listar/{atividade}")
	public ResponseEntity<List<CheckListAtividadeDto>> listar(@PathVariable long atividade) {
		List<CheckListAtividadeDto> dtos = service.listar(atividade);
		return new ResponseEntity<List<CheckListAtividadeDto>>(dtos, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<CheckListAtividadeDto> alterar(@RequestBody CheckListAtividadeDto checkList) {
		CheckListAtividadeDto dto = service.alterar(checkList);
		return new ResponseEntity<CheckListAtividadeDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/delete/{codigo}")
	public ResponseEntity excluir(@PathVariable long codigo) {
		service.excluir(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
