package com.br.ilawgestao.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.repository.EmpresaRepository;
import com.br.ilawgestao.domains.service.EmpresaService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping(value = "/empresas")
public class EmpresaController {
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@PostMapping
	public ResponseEntity<Empresa> cadastrarEmpresa(@RequestBody Empresa empresa, HttpServletResponse response) {
		Empresa empresaSalva = empresaService.cadastrarEmpresa(empresa);
		publish.publishEvent(new RecursoCriadoEvent(this, response, empresaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Empresa> consultarEmpresaPorCodigo(@PathVariable long codigo) {
		Optional<Empresa> empresa = empresaRepository.findById(codigo);
		return empresa.isPresent() ? ResponseEntity.ok(empresa.get()) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Empresa> alterarEmpresa(@PathVariable long codigo, @RequestBody Empresa empresa) {
		Empresa empresaSalva = empresaService.alterarEmpresa(codigo, empresa);
		return ResponseEntity.ok(empresaSalva);
	}
	
	@GetMapping
	public Page<Empresa> listarEmpresas(Pageable pageble) {
		return  empresaService.listarEmpresas(pageble);
	}
	
	@GetMapping("/por-nome")
	public List<Empresa> consultarEmpresaPorNome(String nome) {
		return empresaService.consultarEmpresaPorNome(nome);
	}
	
	@GetMapping("/por-email")
	public List<Empresa> consultarEmpresaPorEmail(String email) {
		return empresaService.consultarEmpresaPorEmail(email);
	}
	
	@GetMapping("/status/{status}")
	public List<Empresa> consultarEmpresPorStatus(@PathVariable int status) {
		return empresaService.consultarEmpresaPorStatus(status);
	}
}
