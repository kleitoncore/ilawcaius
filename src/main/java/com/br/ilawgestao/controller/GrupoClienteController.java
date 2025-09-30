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
import com.br.ilawgestao.domains.models.GrupoCliente;
import com.br.ilawgestao.domains.service.GrupoClienteService;

@RestController
@RequestMapping("/gruposClientes")
public class GrupoClienteController {
	
	@Autowired
	private GrupoClienteService service;
	
	@PostMapping
	public GrupoCliente cadastrarGrupoCliente(@RequestBody GrupoCliente grupoCliente) {
		return service.cadastrarGrupoCliente(grupoCliente);
	}
	
	@GetMapping
	public List<GrupoCliente> listarGruposCliente(@RequestParam long empresa) {
		return service.listarGruposCliente(empresa);
	}
	
	@GetMapping("/{codigo}")
	public GrupoCliente consultarGrupoCliente(@PathVariable long codigo) {
		return service.consultarGrupoCliente(codigo);
	}
	
	@PutMapping("/{codigo}")
	public GrupoCliente alterarGrupoCliente(@PathVariable long codigo, @RequestBody GrupoCliente grupo) {
		return service.alterarGrupoCliente(codigo, grupo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirGrupoCliente(@PathVariable long codigo) {
		service.excluirGrupoCliente(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
