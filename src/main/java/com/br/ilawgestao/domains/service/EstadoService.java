package com.br.ilawgestao.domains.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.models.Estado;
import com.br.ilawgestao.domains.repository.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repository;
	
	public List<Estado> listarEstados() {
		return repository.findAll();
	}
}
