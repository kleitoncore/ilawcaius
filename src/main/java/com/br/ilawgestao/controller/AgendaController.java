package com.br.ilawgestao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.AgendaCalendarDto;
import com.br.ilawgestao.domains.repository.filtros.FiltroAgenda;
import com.br.ilawgestao.domains.service.AgendaService;

@RestController
@RequestMapping("/agenda")
public class AgendaController {
	
	@Autowired
	private AgendaService agendaService;
	
	@GetMapping("/all")
	public List<AgendaCalendarDto> listarAgenda(@RequestParam long usuario, @RequestParam String usuarios, @RequestParam String grupos,
			@RequestParam String status, @RequestParam String tipos) {
		
		FiltroAgenda filtro = new FiltroAgenda();
		
		filtro.setUsuario(usuario);
		filtro.setUsuarios(usuarios);
		filtro.setGrupoTrabalho(grupos);
		filtro.setStatus(status);
		filtro.setTipos(tipos);
		
		return agendaService.listarAgenda(filtro);
	}
	
	@GetMapping("/home")
	public List<AgendaCalendarDto> listarAgendaHome(@RequestParam long usuario, @RequestParam String usuarios, @RequestParam String grupos,
			@RequestParam String status, @RequestParam String tipos) {
		
		FiltroAgenda filtro = new FiltroAgenda();
		
		filtro.setUsuario(usuario);
		filtro.setUsuarios(usuarios);
		filtro.setGrupoTrabalho(grupos);
		filtro.setStatus(status);
		filtro.setTipos(tipos);
		
		return agendaService.listarAgendaHome(filtro);
	}
	
	@GetMapping("/homeResumo")
	public List<AgendaCalendarDto> listarAgendaHomeResumo(@RequestParam long usuario, @RequestParam String usuarios, @RequestParam String grupos,
			@RequestParam String status, @RequestParam String tipos) {
		
		FiltroAgenda filtro = new FiltroAgenda();
		
		filtro.setUsuario(usuario);
		filtro.setUsuarios(usuarios);
		filtro.setGrupoTrabalho(grupos);
		filtro.setStatus(status);
		filtro.setTipos(tipos);
		
		return agendaService.listarAgendaHomeResumo(filtro);
	}
}
