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
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.service.GrupoTrabalhoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/gruposTrabalho")
public class GrupoTrabalhoController {
	
	@Autowired
	private GrupoTrabalhoService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@GetMapping
	public List<GrupoTrabalho> listarGruposTrabalhos(@RequestParam(required = false, defaultValue = "%") String nome, Empresa empresa) {
		return service.consultarGrupoTrabalhoPorNome(nome, empresa);
	}

	@GetMapping("/ativos/{empresa}")
	public ResponseEntity<List<GrupoTrabalho>> listarGruposAtivos(@PathVariable long empresa) {
		List<GrupoTrabalho> grupos = service.consultarGruposTrabalhosAtivos(empresa);
		return new ResponseEntity<List<GrupoTrabalho>>(grupos, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<GrupoTrabalho> cadastrarGrupoTrabalho(@RequestBody GrupoTrabalho grupo, HttpServletResponse response) {
		GrupoTrabalho grupoTrabalhoSalvo = service.cadastrarGrupoTrabalho(grupo);
		publish.publishEvent(new RecursoCriadoEvent(this, response, grupo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(grupoTrabalhoSalvo);
	}
	
	@PutMapping("/{codigo}")
	public GrupoTrabalho alterarGrupoTrabalho(@PathVariable long codigo, @RequestBody GrupoTrabalho grupoTrabalho) {
		return service.alterarGrupoTrabalho(codigo, grupoTrabalho);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirGrupoTrabalho(@PathVariable long codigo) {
		service.excluirGrupoTrabalho(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/subGrupo/{codigo}")
	public ResponseEntity excluirSubGrupo(@PathVariable long codigo) {
		service.excluirSubGrupo(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/{codigo}")
	public GrupoTrabalho consultarGrupoTrabalho(@PathVariable long codigo) {
		return service.consultarGrupoTrabalhoPorCodigo(codigo);
	}
	
	@GetMapping("/subGrupos/{grupoPai}")
	public List<GrupoTrabalho> listarSubGrupos(@PathVariable long grupoPai) {
		List<GrupoTrabalho> lista = service.listarSubGrupos(grupoPai);
		return lista;
	}
	
	@GetMapping("/gruposSemUsuarios")
	public List<GrupoTrabalho> listarGruposSemUsuarios(@RequestParam long empresa, @RequestParam long usuario) {
		return service.listarGruposSemUsuario(empresa, usuario);
	}
	
	@PostMapping("/subGrupo")
	public ResponseEntity<GrupoTrabalho> cadastrarSubGrupoTrabalho(@RequestBody GrupoTrabalho subGrupo, HttpServletResponse response) {
		GrupoTrabalho subGrupoTrabalhoSalvo = service.cadastrarSubGrupoTrabalho(subGrupo);
		publish.publishEvent(new RecursoCriadoEvent(this, response, subGrupo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(subGrupoTrabalhoSalvo);
	}
	
	@GetMapping("/gruposUsuario/{usuario}")
	public List<GrupoTrabalho> listarGruposUsuario(@PathVariable long usuario) {
		return service.listarGruposUsuario(usuario);
	}
	
	@GetMapping("/gruposUsuario2/{usuario}")
	public List<GrupoTrabalho> listarGruposUsuario2(@PathVariable long usuario) {
		return service.listarGruposUsuario2(usuario);
	}
	
	@GetMapping("/gruposPorCodigos")
	public ResponseEntity<List<GrupoTrabalho>> consultarGruposPorCodigos(@RequestParam String codigos) {
		List<GrupoTrabalho> lista = service.consultarGrupsTrabalhoPorCodigos(codigos);
		return new ResponseEntity<List<GrupoTrabalho>>(lista, HttpStatus.OK);
	}
}
