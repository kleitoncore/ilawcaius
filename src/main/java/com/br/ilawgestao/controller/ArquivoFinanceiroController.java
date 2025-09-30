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

import com.br.ilawgestao.domains.models.ArquivoFinanceiro;
import com.br.ilawgestao.domains.models.ArquivoFinanceiroTemp;
import com.br.ilawgestao.domains.service.ArquivoFinanceiroService;

@RestController
@RequestMapping("/arquivoFinanceiro")
public class ArquivoFinanceiroController {
	
	@Autowired
	private ArquivoFinanceiroService service;
	
	@SuppressWarnings("rawtypes")
	@PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity upLoadFile(@RequestParam MultipartFile file, MultipartFile lancamento, MultipartFile usuario) {
		service.incluirArquivo(file, lancamento.getOriginalFilename(), usuario.getOriginalFilename());
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/temp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity uploadFileTemp(@RequestParam MultipartFile file, MultipartFile usuario) {
		service.incluirArquivoTemp(file, usuario.getOriginalFilename());
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/listarTemp/{usuario}")
	public List<ArquivoFinanceiroTemp> listarArquivosTemp(@PathVariable long usuario) {
		return service.listarTemp(usuario);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirArquivoTemp/{codigo}")
	public ResponseEntity excluirArquivoTemp(@PathVariable long codigo) {
		service.excluirArquivoTemp(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirArquivosTemp/{usuario}")
	public ResponseEntity excluirArquivosTemp(@PathVariable long usuario) {
		service.excluirArquivosTemp(usuario);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/consultarArquivoTemp/{codigo}")
	public ArquivoFinanceiroTemp consultarArquivoTemp(@PathVariable long codigo) {
		return service.consultarArquivoTemp(codigo);
	}
	
	@GetMapping("/downloadTemp/{codigo}")
	public ResponseEntity<ByteArrayResource> baixarArquivoTemp(@PathVariable long codigo) {
		ArquivoFinanceiroTemp arquivo = null;
		arquivo = service.consultarArquivoTemp(codigo);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(arquivo.getTipo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, 
						"attachment; filename=\"" + arquivo.getArquivo() + "\"")
				.body(new ByteArrayResource(arquivo.getFile()));
	}
	
	@GetMapping("/arquivos/{lancamento}")
	public List<ArquivoFinanceiro> listarArquivos(@PathVariable long lancamento) {
		return service.listarArquivosFinanceiro(lancamento);
	}
	
	@GetMapping("/consultarArquivo/{codigo}")
	public ArquivoFinanceiro consultarArquivo(@PathVariable long codigo) {
		return service.consultarArquivo(codigo);
	}
	
	@GetMapping("/download/{codigo}")
	public ResponseEntity<ByteArrayResource> baixarArquivo(@PathVariable long codigo) {
		ArquivoFinanceiro arquivo = null;
		arquivo = service.consultarArquivo(codigo);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(arquivo.getTipo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, 
						"attachment; filename=\"" + arquivo.getArquivo() + "\"")
				.body(new ByteArrayResource(arquivo.getFile()));
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirArquivo/{codigo}")
	public ResponseEntity excluirArquivo(@PathVariable long codigo) {
		service.excluirArquivo(codigo);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@PutMapping("/adicionarDescricaoTemp/{codigo}")
	public ArquivoFinanceiroTemp adicionarDescricaoArquivoTemp(@PathVariable long codigo, @RequestBody ArquivoFinanceiroTemp arquivo) {
		return service.adicionarDescricaoTemp(codigo, arquivo);
	}
	
	@PutMapping("/adicionarDescricao/{codigo}")
	public ArquivoFinanceiro adicionarDescricaoArquivo(@PathVariable long codigo, @RequestBody ArquivoFinanceiro arquivo) {
		return service.adicionarDescricao(codigo, arquivo);
	}
}
