package com.br.ilawgestao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.ExecucaoCheckListDto;
import com.br.ilawgestao.domains.dto.ExecucaoCheckListaCadastroDto;
import com.br.ilawgestao.domains.service.ExecucaoCheckListService;

@RestController
@RequestMapping("/execucaoCheckList")
public class ExecucaoCheckListController {
	
	@Autowired
	private ExecucaoCheckListService service;
	
	@PostMapping
	public ResponseEntity<ExecucaoCheckListaCadastroDto> cadastrar(@RequestBody ExecucaoCheckListaCadastroDto exec) {
		ExecucaoCheckListaCadastroDto dto = service.cadastrar(exec);
		return new ResponseEntity<ExecucaoCheckListaCadastroDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{atividade}")
	public ResponseEntity<List<ExecucaoCheckListDto>> consultarCheckList(@PathVariable long atividade) {
		List<ExecucaoCheckListDto> dtos = service.consultarCheckList(atividade);
		return new ResponseEntity<List<ExecucaoCheckListDto>>(dtos, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<ExecucaoCheckListDto> alterar(@RequestBody ExecucaoCheckListDto exec) {
		ExecucaoCheckListDto dto = service.selecionarCheckList(exec);
		return new ResponseEntity<ExecucaoCheckListDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/por-codigo/{codigo}")
	public ResponseEntity<ExecucaoCheckListDto> consultar(@PathVariable long codigo) {
		return new ResponseEntity<ExecucaoCheckListDto>(service.consultarExecucao(codigo), HttpStatus.OK);
	}
}
