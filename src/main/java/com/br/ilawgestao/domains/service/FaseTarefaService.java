package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.FaseTarefaDto;import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.FaseTarefa;
import com.br.ilawgestao.domains.repository.EmpresaRepository;
import com.br.ilawgestao.domains.repository.FaseTarefaRepository;

@Service
public class FaseTarefaService {
	
	@Autowired
	private FaseTarefaRepository faseRepository;
		
	@Autowired
	private EmpresaRepository empresaRepository;
	
	public FaseTarefaDto cadastrar(FaseTarefaDto dto) {
		Optional<Empresa> empresa = empresaRepository.findById(dto.getEmpresa().getCodigo());
		if(!empresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		FaseTarefa fase = FaseTarefa.builder()
				.nome(dto.getNome())
				.empresa(dto.getEmpresa())
				.build();
		
		return FaseTarefaDto.build(faseRepository.save(fase));
	}
	
	public List<FaseTarefaDto> listar(long empresa) {
		Optional<Empresa> empresaConsulta = empresaRepository.findById(empresa);
		if(!empresaConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		List<FaseTarefa> entitys = faseRepository.findByEmpresaCodigoOrderByNomeAsc(empresa);
		List<FaseTarefaDto> dtos = new ArrayList<FaseTarefaDto>();
		for(FaseTarefa fase : entitys) {
			dtos.add(FaseTarefaDto.build(fase));
		}
		
		return dtos;
	}
	
	public FaseTarefaDto consultar(long codigo) {
		Optional<FaseTarefa> entity = faseRepository.findById(codigo);
		if(!entity.isPresent()) {
			throw new EntidadeNaoEncontradaException("Fase de Tarefa não encontrada");
		}
		
		return FaseTarefaDto.build(entity.get());
	}
	
	public FaseTarefaDto alterar(FaseTarefaDto dto) {
		FaseTarefa fase = FaseTarefa.builder()
				.codigo(dto.getCodigo())
				.nome(dto.getNome())
				.empresa(dto.getEmpresa())
				.build();
		return FaseTarefaDto.build(faseRepository.save(fase));
	}
	
	public void excluir(long codigo) {
		faseRepository.deleteById(codigo);
	}
}
