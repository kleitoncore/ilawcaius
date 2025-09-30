package com.br.ilawgestao.controller;

import java.util.List;

import com.br.ilawgestao.domains.repository.filtros.FiltroTituloAtividade;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.TituloAtividadeDto;
import com.br.ilawgestao.domains.service.TituloAtividadeService;

@RestController
@RequestMapping("/tituloAtividade")
public class TituloAtividadeController {
	
	@Autowired
	private TituloAtividadeService service;
	
	@PostMapping
	public ResponseEntity<TituloAtividadeDto> cadastrar(@RequestBody TituloAtividadeDto tituloDto) {
		TituloAtividadeDto dto = service.cadastrar(tituloDto);
		return new ResponseEntity<TituloAtividadeDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/all/{empresa}")
	public ResponseEntity<List<TituloAtividadeDto>> listar(@PathVariable long empresa) {
		List<TituloAtividadeDto> dtos = service.listar(empresa);
		return new ResponseEntity<List<TituloAtividadeDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<TituloAtividadeDto> consultar(@PathVariable long codigo) {
		TituloAtividadeDto dto = service.consultar(codigo);
		return new ResponseEntity<TituloAtividadeDto>(dto, HttpStatus.OK);
	}
	

	@GetMapping("/nome")
	public ResponseEntity<TituloAtividadeDto> consultarPorNome(@RequestParam String nome, @RequestParam long empresa) {
		TituloAtividadeDto dto = service.consultarPorNome(nome, empresa);
		return new ResponseEntity<TituloAtividadeDto>(dto, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<TituloAtividadeDto> alterar(@RequestBody TituloAtividadeDto tituloDto) {
		TituloAtividadeDto dto = service.alterar(tituloDto);
		return new ResponseEntity<TituloAtividadeDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluir(@PathVariable long codigo) {
		service.excluir(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/titulos-por-fase/{fase}")
	public ResponseEntity<List<TituloAtividadeDto>> consultarTitulosPorFase(@PathVariable long fase) {
		List<TituloAtividadeDto> dtos = service.consultarTitulosPorFase(fase);
		return new ResponseEntity<List<TituloAtividadeDto>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/filtrar-titulos")
	public ResponseEntity<List<TituloAtividadeDto>> filtrarTitulos(@RequestParam long empresa,
																   @RequestParam long fase,
																   @RequestParam String titulo,
																   @RequestParam String status,
																   @RequestParam long classificacao,
																   @RequestParam long ordenacao) {
		FiltroTituloAtividade filtro = new FiltroTituloAtividade();
		filtro.setEmpresa(empresa);
		filtro.setFase(fase);
		filtro.setTitulo(titulo);
		filtro.setStatus(status);
		filtro.setClassificacao(classificacao);
		filtro.setOrdenacao(ordenacao);
		List<TituloAtividadeDto> dtos = service.filtrarTitulosAtividades(filtro);
		return new ResponseEntity<List<TituloAtividadeDto>>(dtos, HttpStatus.OK);
	}
}
