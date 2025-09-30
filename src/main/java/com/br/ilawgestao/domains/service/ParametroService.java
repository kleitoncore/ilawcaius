package com.br.ilawgestao.domains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.models.Parametro;
import com.br.ilawgestao.domains.repository.ParametroRepository;

@Service
public class ParametroService {
	
	@Autowired
	private ParametroRepository parametroRepository;
	
	public Parametro consultarParametro(long codigo) {
		return parametroRepository.findById(codigo).get();
	}
}
