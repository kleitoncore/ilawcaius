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

import com.br.ilawgestao.domains.dto.AgrupamentoTarefaDto;
import com.br.ilawgestao.domains.dto.GrupoTarefaDto;
import com.br.ilawgestao.domains.dto.TarefaDto;
import com.br.ilawgestao.domains.dto.UsuarioTarefaDto;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.service.TarefaService;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {
	
	@Autowired
	private TarefaService service;
	
	@PostMapping
	public ResponseEntity<TarefaDto> cadastrar(@RequestBody TarefaDto tarefaDto) {
		TarefaDto dto = service.cadastrar(tarefaDto);
		return new ResponseEntity<TarefaDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/{empresa}")
	public ResponseEntity<List<TarefaDto>> listar(@PathVariable long empresa) {
		List<TarefaDto> dtos = service.listar(empresa);
		return new ResponseEntity<List<TarefaDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/porCodigo/{codigo}")
	public ResponseEntity<TarefaDto> consultar(@PathVariable long codigo) {
		TarefaDto dto = service.consultar(codigo);
		return new ResponseEntity<TarefaDto>(dto, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<TarefaDto> alterar(@RequestBody TarefaDto tarefaDto) {
		TarefaDto dto = service.alterarTarefa(tarefaDto);
		return new ResponseEntity<TarefaDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluir(@PathVariable long codigo) {
		service.excluirTarefa(codigo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/usuariosNaoCadastrados")
	public ResponseEntity<List<Usuario>> listarUsuariosNaoCadastrados(@RequestParam long tarefa, @RequestParam long empresa) {
		List<Usuario> usuarios = service.listarUsuariosNaoCadastrados(tarefa, empresa);
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
	}
	
	@PostMapping("/adicionarTarefaGrupo")
	public ResponseEntity<AgrupamentoTarefaDto> adicionarTarefaAoGrupo(@RequestBody AgrupamentoTarefaDto agrupaDto) {
		AgrupamentoTarefaDto dto = service.adicionarTerefaAoGrupo(agrupaDto);
		return new ResponseEntity<AgrupamentoTarefaDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/gruposPorTarefa/{tarefa}")
	public ResponseEntity<List<GrupoTarefaDto>> listarGruposPorTarefa(@PathVariable long tarefa) {
		List<GrupoTarefaDto> dtos = service.listarGruposPorTarefa(tarefa);
		return new ResponseEntity<List<GrupoTarefaDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/gruposSemTarefa")
	public ResponseEntity<List<GrupoTarefaDto>> listarGruposSemTarefa(@RequestParam long tarefa, @RequestParam long empresa) {
		List<GrupoTarefaDto> dtos = service.listarGruposSemTarefa(tarefa,empresa);
		return new ResponseEntity<List<GrupoTarefaDto>>(dtos, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirAgrupamento")
	public ResponseEntity excluirAgrupamento(@RequestParam long tarefa, @RequestParam long grupo) {
		service.excluirAgrupamento(tarefa, grupo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/tarefasSemGrupo")
	public ResponseEntity<List<TarefaDto>> listarTarefasSemGrupo(@RequestParam long grupo, @RequestParam long empresa) {
		List<TarefaDto> dtos = service.listarTarefasSemGrupo(grupo, empresa);
		return new ResponseEntity<List<TarefaDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/consultarAgrupamento")
	public ResponseEntity<AgrupamentoTarefaDto> consultarAgrupamento(@RequestParam long tarefa, @RequestParam long grupo) {
		AgrupamentoTarefaDto dto = service.consultarAgrupamento(tarefa, grupo);
		return new ResponseEntity<AgrupamentoTarefaDto>(dto, HttpStatus.OK);
	}
	
	@PostMapping("/adicionarUsuarioTarefa")
	public ResponseEntity<UsuarioTarefaDto> adicionarUsuarioTarefa(@RequestBody UsuarioTarefaDto usuarioTarefaDto) {
		UsuarioTarefaDto dto = service.adicionarUsuarioTarefa(usuarioTarefaDto);
		return new ResponseEntity<UsuarioTarefaDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/usuariosPorTarefa/{tarefa}")
	public ResponseEntity<List<UsuarioTarefaDto>> listarUsuariosTarefa(@PathVariable long tarefa) {
		List<UsuarioTarefaDto> dtos = service.listarusuariosTarefas(tarefa);
		return new ResponseEntity<List<UsuarioTarefaDto>>(dtos, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirUsuario/{codigo}")
	public ResponseEntity excluirUsuarioTarefa(@PathVariable long codigo) {
		service.excluirUsuarioTarefa(codigo);
		return new ResponseEntity(HttpStatus.OK);
	}

	@GetMapping("/tarefasPorGrupo")
	public ResponseEntity<List<TarefaDto>> listarTarefasPorGrupo(@RequestParam long grupoTrabalho,
																 @RequestParam long grupoAtividade) {
		List<TarefaDto> dtos = service.listarTarefasPorGrupo(grupoTrabalho,grupoAtividade);
		return new ResponseEntity<List<TarefaDto>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/tarefas-por-grupo-atividade-e-grupo-trabalho")
	public ResponseEntity<List<TarefaDto>> listarTarefasPorGrupoAtividadeAndGrupoTrabalho(@RequestParam long grupoAtividade,
																						  @RequestParam long grupoTrabalho) {
		List<TarefaDto> dtos = service.listarTarefasPorGrupoAtividadeEGrupoTrabalho(grupoAtividade,grupoTrabalho);
		return new ResponseEntity<List<TarefaDto>>(dtos, HttpStatus.OK);
	}
}
