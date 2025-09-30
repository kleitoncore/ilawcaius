package com.br.ilawgestao.domains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.models.LogUsuario;
import com.br.ilawgestao.domains.repository.LogUsuarioRepository;

@Service
public class LogUsuarioService {
	
	@Autowired
	private LogUsuarioRepository logUsuarioRepository;
	
	public void cadastrarLogusuario(LogUsuario log) {
		logUsuarioRepository.save(log);
	}
}
