package com.br.ilawgestao.domains.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.ilawgestao.domains.models.Perfil;
import com.br.ilawgestao.domains.repository.PerfilRepository;

@Service
public class PerfilService {
	
	@Autowired
	private PerfilRepository perfilRespository;
	
	public List<Perfil> listarPerfis() {
		return perfilRespository.findAll();
	}
}
