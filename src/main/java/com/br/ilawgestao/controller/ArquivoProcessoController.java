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

import com.br.ilawgestao.domains.models.ArquivoProcesso;
import com.br.ilawgestao.domains.service.ArquivoProcessoService;

@RestController
@RequestMapping("/arquivoProcesso")
public class ArquivoProcessoController {
	
	@Autowired
	private ArquivoProcessoService service;
				
	@SuppressWarnings("rawtypes")
	@PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity incluirArquivo(@RequestParam MultipartFile file, MultipartFile processo, MultipartFile usuario) {
		service.incluirArquivo(file, processo.getOriginalFilename(), usuario.getOriginalFilename());
		return new ResponseEntity(HttpStatus.OK);
	} 
	
	@GetMapping("/consultarArquivos/{processo}")
	public List<ArquivoProcesso> listarArquivos(@PathVariable long processo) {
		return service.listarArquivos(processo);
	}
	
	@GetMapping("/consultarArquivo/{codigo}")
	public ArquivoProcesso consultarArquivo(@PathVariable long codigo) {
		return service.consultarArquivo(codigo);
	}
	
	@PutMapping("/adicionarDescricao/{codigo}")
	public ArquivoProcesso adicionarDescricao(@PathVariable long codigo, @RequestBody ArquivoProcesso arquivo) {
		return service.adicionarDescricao(codigo, arquivo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirArquivo/{codigo}")
	public ResponseEntity excluirArquivo(@PathVariable long codigo) {
		service.excluirArquivo(codigo);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/download/{codigo}")
	public ResponseEntity<ByteArrayResource> baixarArquivoTemp(@PathVariable long codigo) {
		ArquivoProcesso arquivo = null;
		arquivo = service.consultarArquivo(codigo);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(arquivo.getTipo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, 
						"attachment; filename=\"" + arquivo.getNome() + "\"")
				.body(new ByteArrayResource(arquivo.getFile()));
	}
}
