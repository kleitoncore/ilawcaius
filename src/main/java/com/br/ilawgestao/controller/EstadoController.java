package com.br.ilawgestao.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.br.ilawgestao.domains.models.Estado;
import com.br.ilawgestao.domains.service.EstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {
	
	@Autowired
	private EstadoService service;
	
	@GetMapping
	public List<Estado> listarEstados() {
		return service.listarEstados();
	}
}
