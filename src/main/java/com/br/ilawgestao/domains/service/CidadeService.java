package com.br.ilawgestao.domains.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.models.Cidade;
import com.br.ilawgestao.domains.repository.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repository;
	
	public List<Cidade> listarCidadesPorEstado(long estado) {
		return repository.findCidadeByEstadoCodigo(estado);
	}
}
