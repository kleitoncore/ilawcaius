package com.br.ilawgestao.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.models.CentralAtividade;
import com.br.ilawgestao.domains.service.CentralAtividadeService;

@RestController
@RequestMapping("/central")
public class CentralAtividadeController {
	
	@Autowired
	private CentralAtividadeService centralService;
	
	@GetMapping("/notificacoesQuantidade/{usuario}")
	public int notificacoes(@PathVariable long usuario) {
		return centralService.notificacoes(usuario);
	}
	
	@GetMapping("/consulta")
	public List<CentralAtividade> consultarCentral(@RequestParam long usuario, @RequestParam String dataInicial, @RequestParam String dataFinal) {
		return centralService.consultarCentral(usuario, dataInicial, dataFinal);
	}
	
	@PutMapping("/lerNotificacao/{codigo}")
	public CentralAtividade lerNotificacao(@PathVariable long codigo, @RequestBody CentralAtividade central) {
		return centralService.lerNotificacao(codigo, central);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirNotificacoes")
	public ResponseEntity excluirNotificacoes(@RequestParam long[] codigos) {
		centralService.excluirNotificacoes(codigos);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
