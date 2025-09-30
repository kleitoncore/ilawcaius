package com.br.ilawgestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.GraficoPontuacaoUsuarioDto;
import com.br.ilawgestao.domains.service.PontuacaoUsuarioService;

@RestController
@RequestMapping("/pontuacaoUsuario")
public class PontuacaoUsuarioController {
	
	@Autowired
	private PontuacaoUsuarioService service;
	
	@GetMapping("/pontos-usuario")
	public ResponseEntity<GraficoPontuacaoUsuarioDto> gerarGraficoPontuacaoUsuario(@RequestParam long ano, @RequestParam long mesAtual,
			@RequestParam long mesAnterior, @RequestParam long usuario, @RequestParam long empresa) {
		GraficoPontuacaoUsuarioDto dto = service.gerarGraficoPontuacaoUsuario(ano, mesAtual, mesAnterior, usuario, empresa);
		return new ResponseEntity<GraficoPontuacaoUsuarioDto>(dto, HttpStatus.OK);
		
	}
}
