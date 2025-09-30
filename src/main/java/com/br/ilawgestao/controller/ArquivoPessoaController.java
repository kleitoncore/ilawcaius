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

import com.br.ilawgestao.domains.models.ArquivoPessoa;
import com.br.ilawgestao.domains.models.ArquivoPessoaTemp;
import com.br.ilawgestao.domains.service.ArquivoPessoaService;

@RestController
@RequestMapping("/arquivoPessoa")
public class ArquivoPessoaController {
	
	@Autowired
	private ArquivoPessoaService service;
	
	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/incluirArquivoTemp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity incluirArquivoTemp(@RequestParam MultipartFile file, MultipartFile usuario) {
		service.incluirArquivoTemp(file, usuario.getOriginalFilename());
		return new ResponseEntity(HttpStatus.OK);
	} 
	
	@GetMapping("/listarArquivosTemp/{usuario}")
	public List<ArquivoPessoaTemp> listarArquivosTemp(@PathVariable long usuario) {
		return service.listarArquivosTemp(usuario);
	}
	
	@GetMapping("/consultarArquivoTemp/{codigo}")
	public ArquivoPessoaTemp cosultarArquivoTemp(@PathVariable long codigo) {
		return service.consultarArquivoTemp(codigo);
	}
	
	@PutMapping("/adicionarDescricaoArquivoTemp/{codigo}")
	public ArquivoPessoaTemp adicionarDescricaoArquivoTemp(@PathVariable long codigo, @RequestBody ArquivoPessoaTemp arquivo) {
		return service.adicionarDescricaoTemp(codigo, arquivo);
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
	
	@GetMapping("/downloadTemp/{codigo}")
	public ResponseEntity<ByteArrayResource> baixarArquivoTemp(@PathVariable long codigo) {
		ArquivoPessoaTemp arquivo = null;
		arquivo = service.consultarArquivoTemp(codigo);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(arquivo.getTipo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, 
						"attachment; filename=\"" + arquivo.getArquivo() + "\"")
				.body(new ByteArrayResource(arquivo.getFile()));
	}
	
	//Definitivos
	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/incluirArquivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity incluirArquivo(@RequestParam MultipartFile file, MultipartFile pessoa, MultipartFile usuario) {
		service.incluirArquivo(file, pessoa.getOriginalFilename(), usuario.getOriginalFilename());
		return new ResponseEntity(HttpStatus.OK);
	} 
	
	@GetMapping("/listarArquivos/{pessoa}")
	public List<ArquivoPessoa> listarArquivos(@PathVariable long pessoa) {
		return service.listarArquivos(pessoa);
	}
	
	@GetMapping("/consultarArquivo/{codigo}")
	public ArquivoPessoa consultarArquivo(@PathVariable long codigo) {
		return service.consultarArquivo(codigo);
	}
	
	@PutMapping("/adicionarDescricao/{codigo}")
	public ArquivoPessoa adicionarDescricao(@PathVariable long codigo, @RequestBody ArquivoPessoa arquivo) {
		return service.adicionarDescricao(codigo, arquivo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirArquivo/{codigo}")
	public ResponseEntity excluirArquivo(@PathVariable long codigo) {
		service.excluirArquivo(codigo);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/download/{codigo}")
	public ResponseEntity<ByteArrayResource> baixarArquivo(@PathVariable long codigo) {
		ArquivoPessoa arquivo = null;
		arquivo = service.consultarArquivo(codigo);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(arquivo.getTipo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, 
						"attachment; filename=\"" + arquivo.getArquivo() + "\"")
				.body(new ByteArrayResource(arquivo.getFile()));
	}
 }