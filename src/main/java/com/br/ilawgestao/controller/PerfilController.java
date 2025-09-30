package com.br.ilawgestao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.models.Perfil;
import com.br.ilawgestao.domains.service.PerfilService;

@RestController
@RequestMapping("/perfil")
public class PerfilController {
	
	@Autowired
	private PerfilService service;
	
	@GetMapping("/all")
	public List<Perfil> listarPerfis() {
		return service.listarPerfis();
	}
}
