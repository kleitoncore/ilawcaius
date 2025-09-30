package com.br.ilawgestao.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.AtendimentoDto;
import com.br.ilawgestao.domains.dto.AtendimentoHisoricoDto;
import com.br.ilawgestao.domains.models.Atendimento;
import com.br.ilawgestao.domains.models.AtendimentoHistorico;
import com.br.ilawgestao.domains.service.AtendimentoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {
	
	@Autowired
	private AtendimentoService atendimentoService;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<Atendimento> cadastrarAtendimento(@RequestBody AtendimentoDto dto, HttpServletResponse response) {
		Atendimento atendimento = atendimentoService.cadastrarPrimeiroAtendimento(dto.transformaParaObjeto());
		publish.publishEvent(new RecursoCriadoEvent(this, response, atendimento.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(atendimento);
	}
	
	@PostMapping("/historico")
	public ResponseEntity<AtendimentoHistorico> cadastrarHistorico(@RequestBody AtendimentoHisoricoDto dto, HttpServletResponse response) {
		AtendimentoHistorico historico = atendimentoService.cadastrarHistorico(dto.transformaParaObjeto());
		publish.publishEvent(new RecursoCriadoEvent(this, response, historico.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(historico);
	}
	
	@GetMapping("/lista")
	public List<Atendimento> listarAtendimentos(@RequestParam long empresa, 
												@RequestParam String dataInicial,
												@RequestParam String dataFinal,
												@RequestParam long pessoa,
												@RequestParam long grupo) {
		return atendimentoService.listarAtendimento(empresa, dataInicial, dataFinal, pessoa, grupo);
	}
	
	@GetMapping("/historico/{atendimento}")
	public List<AtendimentoHistorico> consultarHistoricos(@PathVariable long atendimento) {
		return atendimentoService.consultarAtendimentoHistorico(atendimento);
	}
	
	@GetMapping("/atendimento/{codigo}")
	public Atendimento consultarAtendimento(@PathVariable long codigo) {
		return atendimentoService.consultarAtendimento(codigo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/historico/{codigo}")
	public ResponseEntity excluirHistorico(@PathVariable long codigo) {
		atendimentoService.excluirHistorico(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/atendimentoGrupo/{codigo}")
	public Atendimento alterarAtendimentoGrupo(@PathVariable long codigo, @RequestBody AtendimentoDto dto) {
		return atendimentoService.alterarAtendimentoGrupo(codigo, dto.transformaParaObjeto());
	}
	
	@PutMapping("/atendimentoProcesso/{codigo}")
	public Atendimento alterarAtendimentoProcesso(@PathVariable long codigo, @RequestBody AtendimentoDto dto) {
		return atendimentoService.alterarAtendimentoProcesso(codigo, dto.transformaParaObjeto());
	}
	
	@GetMapping("/porPessoa/{pessoa}")
	public List<Atendimento> listarAtendimentosPorPessoa(@PathVariable long pessoa) {
		return atendimentoService.listarAtendimentosPorPessoa(pessoa);
	}
}
