package com.br.ilawgestao.controller;

import java.util.ArrayList;
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

import com.br.ilawgestao.domains.dto.TokenDto;
import com.br.ilawgestao.domains.dto.UsuarioDto;
import com.br.ilawgestao.domains.dto.UsuarioShortDto;
import com.br.ilawgestao.domains.models.LogUsuario;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.service.UsuarioService;
import com.br.ilawgestao.domains.service.interfaces.JwtService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping
	public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody UsuarioDto usuarioDTO, HttpServletResponse response) {
		Usuario usuario = service.cadastrarUsuario(usuarioDTO.transformaParaObjeto());
		publish.publishEvent(new RecursoCriadoEvent(this, response, usuario.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity<?> autenticar(@RequestBody UsuarioDto usuarioDto) {
		Usuario usuarioAutenticado = service.autenticarUsuario(usuarioDto);
		String token = jwtService.gerarToken(usuarioAutenticado);
		TokenDto tokenDto = new TokenDto(usuarioAutenticado.getNome(), token);
		return ResponseEntity.ok(tokenDto);
	}
	
	@GetMapping
	public List<Usuario> listarUsuario(@RequestParam long empresa, @RequestParam(required = false, defaultValue = "%") String nome) {
		return service.listarUsuarios(empresa, nome); 
	}
	
	@GetMapping("/{codigo}")
	public Usuario consultarUsuario(@PathVariable long codigo) {
		return service.consultarUsuario(codigo);
	}
	
	@PutMapping("/{codigo}")
	public Usuario alterarUsuario(@PathVariable long codigo, @RequestBody Usuario usuario) {
		return service.alterarUsuario(codigo, usuario);
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/log")
	public ResponseEntity incluirLogusuario(@RequestBody LogUsuario log) {
		service.inserirLogUsuario(log);
		return new ResponseEntity(HttpStatus.NO_CONTENT); 
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirUsuario(@PathVariable long codigo) {
		service.excluirUsuario(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/usuariosSemGrupo")
	public List<Usuario> listarUsuariosSemGrupo(@RequestParam long grupo, long empresa) {
		List<Usuario> usuarios = service.listarUsuarioSemGrupo(grupo, empresa);
		return usuarios;
	}
	
	@GetMapping("/usuariosSemGrupoPessoas")
	public List<Usuario> listarusuariosSemGrupoPessoas(@RequestParam long grupo, long empresa) {
		List<Usuario> usuarios = service.listarUsuariosSemGruposPessoas(grupo, empresa); 
		return usuarios;
	}
	
	@GetMapping("/empresa/{empresa}")
	public List<Usuario> listarUsuariosEmpresa(@PathVariable long empresa) {
		return service.listarUsuariosPorEmpresa(empresa);
	}
	
	@GetMapping("/empresaFiltrado/{usuario}")
	public List<Usuario> listarUsuariosEmpresaFiltrado(@PathVariable long usuario) {
		Usuario usu = service.consultarUsuario(usuario);
		List<Usuario> lista = new ArrayList<Usuario>();
		lista.add(usu);
		return lista;
	}
	
	@PutMapping("/alterarSenha")
	public Usuario alterarSenha(@RequestParam long codigo, @RequestParam String senha, @RequestParam String novaSenha, 
			@RequestParam String confirmacao, @RequestBody Usuario usuario) {
		return service.alterarSenha(codigo, senha, novaSenha, confirmacao, usuario);
	}
	
	@GetMapping("/shorts/{empresa}")
	public ResponseEntity<List<UsuarioShortDto>> listarShort(@PathVariable long empresa) {
		List<UsuarioShortDto> dtos = service.listarShort(empresa);
		return new ResponseEntity<List<UsuarioShortDto>>(dtos, HttpStatus.OK);
	}
}
