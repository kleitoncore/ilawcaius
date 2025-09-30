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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.models.UsuarioGrupoTrabalho;
import com.br.ilawgestao.domains.service.UsuarioGrupoTrabalhoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/usuarioGrupoTrabalho")
public class UsuarioGrupoTrabahoController {
	
	@Autowired
	private UsuarioGrupoTrabalhoService usuarioGrupoService;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<UsuarioGrupoTrabalho> cadastrarUsuarioGrupoTrabalho(@RequestBody UsuarioGrupoTrabalho usuarioGrupo, HttpServletResponse response) {
		UsuarioGrupoTrabalho usuarioGrupoSalvo = usuarioGrupoService.cadastrarUsuarioGrupoTrabalho(usuarioGrupo);
		publish.publishEvent(new RecursoCriadoEvent(this, response, usuarioGrupoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGrupoSalvo);
	}
	
	@GetMapping("/usuariosPorGrupo/{codigo}")
	public List<UsuarioGrupoTrabalho> listarUsuariosPorGrupo(@PathVariable long codigo) {
		GrupoTrabalho grupo = new GrupoTrabalho();
		grupo.setCodigo(codigo);
		return usuarioGrupoService.listarUsuariosPorGrupo(grupo);
	}
	
	@GetMapping("/gruposPorUsuario/{codigo}")
	public List<UsuarioGrupoTrabalho> listarGruposPorUsuario(@PathVariable long codigo) {
		Usuario usuario = new Usuario();
		usuario.setCodigo(codigo);
		return usuarioGrupoService.listarGruposPorusuario(usuario);
	}
	
	@PostMapping("/adicionarTodos")
	public void adicioonarTodosOsGrupos(@RequestBody Usuario usuario) {
		usuarioGrupoService.adicionarTodosGruposTrabalho(usuario);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirUsuarioGrupoTrabalho(@PathVariable long codigo) {
		usuarioGrupoService.excluirUsuarioGrupoTrabalho(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/usuariosAtividade/{grupo}")
	public List<Usuario> listarUsuariosAtividades(@PathVariable long grupo) {
		return usuarioGrupoService.listarUsuariosAtividades(grupo);
	}

	@GetMapping("/usuariosAtividadeAtivos/{grupo}")
	public List<Usuario> listarUsuariosAtividadesAtivos(@PathVariable long grupo) {
		return usuarioGrupoService.listarUsuariosAtividadesAtivos(grupo);
	}
}
