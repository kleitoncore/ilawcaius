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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.ProjecaoUsuarioDto;
import com.br.ilawgestao.domains.repository.filtros.FiltroProjecaoUsuario;
import com.br.ilawgestao.domains.service.ProjecaoUsuarioService;

@RestController
@RequestMapping("/projecaoUsuario")
public class ProjecaoUsuarioController {
	
	@Autowired
	private ProjecaoUsuarioService service;
	
	@PostMapping
	public ResponseEntity<ProjecaoUsuarioDto> cadastrar(@RequestBody ProjecaoUsuarioDto projecaoUsuario) {
		ProjecaoUsuarioDto dto = service.cadastrar(projecaoUsuario);
		return new ResponseEntity<ProjecaoUsuarioDto>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping("/consulta")
	public ResponseEntity<List<ProjecaoUsuarioDto>> listar(@RequestParam long empresa,
			                                               @RequestParam String usuario,
			                                               @RequestParam(defaultValue = "0") long ano,
			                                               @RequestParam(defaultValue = "0") long mes) {
		
		FiltroProjecaoUsuario filtro = new FiltroProjecaoUsuario();
		filtro.setEmpresa(empresa);
		filtro.setUsuario(usuario);
		filtro.setAno(ano);
		filtro.setMes(mes);
		
		List<ProjecaoUsuarioDto> dtos = service.listar(filtro);
		return new ResponseEntity<List<ProjecaoUsuarioDto>>(dtos, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<ProjecaoUsuarioDto> alterar(@RequestBody ProjecaoUsuarioDto projecao) {
		ProjecaoUsuarioDto dto = service.alterar(projecao);
		return new ResponseEntity<ProjecaoUsuarioDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluir(@PathVariable long codigo) {
		service.excluir(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/por-codigo/{codigo}")
	public ResponseEntity<ProjecaoUsuarioDto> consultarPorCodigo(@PathVariable long codigo) {
		ProjecaoUsuarioDto dto = service.consultar(codigo);
		return new ResponseEntity<ProjecaoUsuarioDto>(dto, HttpStatus.OK);
	}
}
