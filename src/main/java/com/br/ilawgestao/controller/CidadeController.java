package com.br.ilawgestao.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.br.ilawgestao.domains.models.Cidade;
import com.br.ilawgestao.domains.service.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired
	private CidadeService service;
	
	@GetMapping("/{estado}")
	public List<Cidade> listarCidades(@PathVariable long estado) {
		return service.listarCidadesPorEstado(estado);
	}
}
