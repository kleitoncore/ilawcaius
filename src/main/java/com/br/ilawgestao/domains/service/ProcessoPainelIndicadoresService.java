package com.br.ilawgestao.domains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.repository.ProcessoRepository;

@Service
public class ProcessoPainelIndicadoresService {
	
	@Autowired
	ProcessoRepository processoRepository;
	
	public int totalProcessos(long empresa) {
		return processoRepository.totalProcessos(empresa);
	}
	public int totalAtivos(long empresa) {
		return processoRepository.totalAtivos(empresa);
	}
	public int parados(long empresa) {
		return processoRepository.parados(empresa);
	}
	public int arquivados(long empresa) {
		return processoRepository.arquivados(empresa);
	}
	public int excluidos(long empresa) {
		return processoRepository.excluidos(empresa);
	}
	public int processosPush(long empresa) {
		return processoRepository.processoPush(empresa);
	}
}
