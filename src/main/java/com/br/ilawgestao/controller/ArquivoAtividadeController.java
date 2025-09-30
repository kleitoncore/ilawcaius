package com.br.ilawgestao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.br.ilawgestao.domains.models.ArquivoAtividade;
import com.br.ilawgestao.domains.models.ArquivoAtividadeTemp;
import com.br.ilawgestao.domains.service.ArquivoAtividadeService;

@RestController
@RequestMapping("/arquivoAtividade")
public class ArquivoAtividadeController {
	
	@Autowired
	private ArquivoAtividadeService service;
	
	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/temp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity incluirArquivoTemp(@RequestParam MultipartFile file, MultipartFile usuario) {
		service.incluirArquivoTemp(file, usuario.getOriginalFilename());
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/listarArquivosTemp/{usuario}")
	List<ArquivoAtividadeTemp> listarArquivosTemp(@PathVariable long usuario) {
		return service.listarArquivosTemp(usuario);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirArquivosTemp/{usuario}")
	public ResponseEntity excluirArquivosTemp(@PathVariable long usuario) {
		service.excluirArquivosTemp(usuario);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirArquivoTemp/{codigo}")
	public ResponseEntity excluirArquivoTemp(@PathVariable long codigo) {
		service.excluirArquivoTemp(codigo);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/consultarArquivoTemp/{codigo}")
	public ArquivoAtividadeTemp consultarArquivoTemp(@PathVariable long codigo) {
		return service.consultarArquivoTemp(codigo);
	}
	
	@PutMapping("/adicionarDescricaoTemp/{codigo}")
	public ArquivoAtividadeTemp adicionarDescricaoTemp(@PathVariable long codigo, @RequestBody ArquivoAtividadeTemp arquivo) {
		return service.adicionarDescricaoTemp(codigo, arquivo);
	}
	
	@GetMapping("/downloadTemp/{codigo}")
	public ResponseEntity<ByteArrayResource> baixarArquivoTemp(@PathVariable long codigo) {
		ArquivoAtividadeTemp arquivo = null;
		arquivo = service.consultarArquivoTemp(codigo);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(arquivo.getTipo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, 
						"attachment; filename=\"" + arquivo.getArquivo() + "\"")
				.body(new ByteArrayResource(arquivo.getFile()));
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity incluirArquivoTemp(@RequestParam MultipartFile file, MultipartFile atividade, MultipartFile usuario) {
		service.incluirArquivo(file, atividade.getOriginalFilename(), usuario.getOriginalFilename());
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/listarArquivos/{atividade}")
	public List<ArquivoAtividade> listarArquivos(@PathVariable long atividade) {
		return service.listarArquivos(atividade);
	}
	
	@GetMapping("/consultarArquivo/{codigo}")
	public ArquivoAtividade consultarArquivo(@PathVariable long codigo) {
		return service.consultarArquivo(codigo);
	}
	
	@PutMapping("/adicionarDescricao/{codigo}")
	public ArquivoAtividade adicionarDescricao(@PathVariable long codigo, @RequestBody ArquivoAtividade arquivo) {
		return service.adicionarDescricao(codigo, arquivo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirArquivo/{codigo}")
	public ResponseEntity excluirArquivo(@PathVariable long codigo) {
		service.excluirArquivo(codigo);;
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/download/{codigo}")
	public ResponseEntity<ByteArrayResource> baixarArquivo(@PathVariable long codigo) {
		ArquivoAtividade arquivo = null;
		arquivo = service.consultarArquivo(codigo);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(arquivo.getTipo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, 
						"attachment; filename=\"" + arquivo.getArquivo() + "\"")
				.body(new ByteArrayResource(arquivo.getFile()));
	}
}
