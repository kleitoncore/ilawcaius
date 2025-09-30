package com.br.ilawgestao.controller;

import java.util.List;

import com.br.ilawgestao.domains.dto.StatusAtividadeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.br.ilawgestao.domains.models.StatusAtividade;
import com.br.ilawgestao.domains.service.StatusAtividadeService;

@RestController
@RequestMapping("/statusAtividade")
public class StatusAtividadeController {
	
	@Autowired
	private StatusAtividadeService statusService;

	@PostMapping
	public ResponseEntity<StatusAtividadeDto> cadastrar(@RequestBody StatusAtividadeDto status) {
		StatusAtividadeDto dto = statusService.cadastrarStatus(status);
		return new ResponseEntity<StatusAtividadeDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/all/{empresa}")
	public List<StatusAtividade> listarStatus(@PathVariable long empresa) {
		return statusService.listarStatus(empresa);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<StatusAtividadeDto> consultar(@PathVariable long codigo) {
		return new ResponseEntity<StatusAtividadeDto>(statusService.consultar(codigo),HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<StatusAtividadeDto> alterar(@RequestBody StatusAtividadeDto status) {
		return new ResponseEntity<StatusAtividadeDto>(statusService.alterarStatus(status),HttpStatus.OK);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity excluir(@PathVariable long codigo) {
		statusService.excluirStatus(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
