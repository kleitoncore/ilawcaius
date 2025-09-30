package com.br.ilawgestao.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.models.GrupoCliente;
import com.br.ilawgestao.domains.models.GrupoClienteUsuario;
import com.br.ilawgestao.domains.service.GrupoClienteUsuarioService;

@RestController
@RequestMapping("/gruposClientesUsuarios")
public class GrupoClienteUsuarioController {
	
	@Autowired
	private GrupoClienteUsuarioService service;
	
	@PostMapping
	public GrupoClienteUsuario cadastrarGrupoClienteusuario(@RequestBody GrupoClienteUsuario grupoClienteUsuario) {
		return service.cadastrarGrupoClienteUsuario(grupoClienteUsuario);
	}
	
	@GetMapping("/porUsuario")
	public List<GrupoCliente> listarGruposPorusuario(@RequestParam long usuario) {
		return service.listarGruposPorUsuario(usuario);
	}
	
	@GetMapping("/porUsuarioConsulta/{usuario}")
	public List<GrupoCliente> listarGruposConsulta(@PathVariable long usuario) {
		return service.listarGruposConsulta(usuario);
	}
	
	@GetMapping("/porGrupo")
	public List<GrupoClienteUsuario> listarUsuariosPorGrupo(@RequestParam long grupo) {
		return service.listarusuarioPorGrupo(grupo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirGrupoClienteUsuario(@PathVariable long codigo) {
		service.excluirGrupoClienteusuario(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
