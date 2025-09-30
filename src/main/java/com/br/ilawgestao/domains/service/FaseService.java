package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.FaseDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Fase;
import com.br.ilawgestao.domains.repository.FaseRepository;

@Service
public class FaseService {
	
	@Autowired
	private FaseRepository faseRepository;
	
	public FaseDto cadastrar(FaseDto faseDto) {
		Fase fase = Fase.builder()
				.nome(faseDto.getNome())
				.cor("#" + faseDto.getCor())
				.empresa(faseDto.getEmpresa())
				.altera("S")
				.build();
		FaseDto dto = FaseDto.build(faseRepository.save(fase));
		return dto;
	}
	
	public List<FaseDto> listar(long empresa) {
		List<Fase> fases = faseRepository.findByEmpresaCodigoOrderByNomeAsc(empresa);
		List<FaseDto> dtos = new ArrayList<FaseDto>();
		if(fases != null) {
			for(Fase fase :fases) {
				dtos.add(FaseDto.build(fase));
			}
		}
		return dtos;
	}
	
	public FaseDto consultar(long codigo) {
		return FaseDto.build(faseRepository.findById(codigo).get());
	}
	
	public FaseDto alterar(long codigo, FaseDto faseDto) {
		FaseDto consulta = this.consultar(codigo);
		if(consulta == null) {
			new EntidadeNaoEncontradaException("Fase não encontrada");
		}
		
		BeanUtils.copyProperties(faseDto, consulta,"codigo","empresa","altera");
		Fase fase = Fase.builder()
				.codigo(consulta.getCodigo())
				.nome(consulta.getNome())
				.cor("#" + consulta.getCor())
				.altera(consulta.getAltera())
				.empresa(consulta.getEmpresa())
				.build();
		return FaseDto.build(faseRepository.save(fase));
	}
	
	public void excluir(long codigo) {
		Optional<Fase> fase = faseRepository.findById(codigo);
		if(!fase.isPresent()) {
			throw new EntidadeNaoEncontradaException("Fase Processual não encontrada");
		}

		if(fase.get().getAltera().equals("N")) {
			throw new EntidadeEmUsoException("Fase Processual não pode ser excluída");
		}
		faseRepository.deleteById(codigo);
	}
}
