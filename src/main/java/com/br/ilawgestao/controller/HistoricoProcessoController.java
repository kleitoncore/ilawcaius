package com.br.ilawgestao.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.HistoricoProcessoDto;
import com.br.ilawgestao.domains.service.HistoricoProcessoService;

@RestController
@RequestMapping("/historicoProcesso")
public class HistoricoProcessoController {
	
	@Autowired
	private HistoricoProcessoService historicoService;
		
	@PostMapping
	public ResponseEntity<HistoricoProcessoDto> incluirHistorico(@RequestBody HistoricoProcessoDto historicoDto, HttpServletResponse response) {
		HistoricoProcessoDto dto = historicoService.incluirHistorico(historicoDto);
		return new ResponseEntity<HistoricoProcessoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{processo}")
	public ResponseEntity<List<HistoricoProcessoDto>> listarHistoricoPorProcesso(@PathVariable long processo) {
		List<HistoricoProcessoDto> dtos = historicoService.listarHistoricoPorProcesso(processo);
		return new ResponseEntity<List<HistoricoProcessoDto>>(dtos, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirHistorico(@PathVariable long codigo) {
		historicoService.excluirHistorico(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
