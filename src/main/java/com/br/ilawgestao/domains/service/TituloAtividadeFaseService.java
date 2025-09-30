package com.br.ilawgestao.domains.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.FaseTarefaDto;
import com.br.ilawgestao.domains.dto.TituloAtividadeDto;
import com.br.ilawgestao.domains.dto.TituloAtividadeFaseDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.FaseTarefa;
import com.br.ilawgestao.domains.models.TituloAtividade;
import com.br.ilawgestao.domains.models.TituloAtividadeFase;
import com.br.ilawgestao.domains.repository.FaseTarefaRepository;
import com.br.ilawgestao.domains.repository.TituloAtividadeFaseRepository;
import com.br.ilawgestao.domains.repository.TituloAtividadeRepository;

@Service
public class TituloAtividadeFaseService {
	
	@Autowired
	private TituloAtividadeRepository tituloRepository;
	
	@Autowired
	private FaseTarefaRepository faseTarefaRepository;
	
	@Autowired
	private TituloAtividadeFaseRepository tituloAtividadeRepository;
	
	public TituloAtividadeFaseDto cadastrar(TituloAtividadeFaseDto dto) {
		Optional<TituloAtividade> titulo = tituloRepository.findById(dto.getTitulo().getCodigo());
		if(!titulo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Título de atividade não encontrado");
		}
		
		Optional<FaseTarefa> fase = faseTarefaRepository.findById(dto.getFase().getCodigo());
		if(!fase.isPresent()) {
			throw new EntidadeNaoEncontradaException("Fase de atividade não encontrada");
		}
		
		TituloAtividadeFase tituloAtividadeFase = TituloAtividadeFase.builder()
				.titulo(titulo.get())
				.fase(fase.get())
				.build();
		
		return TituloAtividadeFaseDto.build(tituloAtividadeRepository.save(tituloAtividadeFase));
	}
	
	public List<FaseTarefaDto> fasesPorTitulos(long titulo) {
		List<TituloAtividadeFase> fases = tituloAtividadeRepository.findByTituloCodigoOrderByFaseNomeAsc(titulo);
		List<FaseTarefaDto> dtos = new ArrayList<FaseTarefaDto>();
		if(fases != null && fases.size() > 0) {
			for(TituloAtividadeFase titFase : fases) {
				dtos.add(FaseTarefaDto.build(titFase.getFase()));
			}
		}
		
		return dtos;
	}
	
	public List<TituloAtividadeDto> listarTitulosPorFase(long codigo) {
		List<TituloAtividadeFase> titulosFases = tituloAtividadeRepository.findByFaseCodigoOrderByTituloTituloAsc(codigo);
		List<TituloAtividadeDto> dtosTitulos = new ArrayList<TituloAtividadeDto>();
		//for(TituloAtividadeFase titFase : titulosFases) {
			//dtosTitulos.add(TituloAtividadeDto.buildConsulta(titFase.getTitulo(),null));
		//}
		
		return dtosTitulos;
	}
	
	public void excluir(long titulo, long fase) {
		Optional<TituloAtividadeFase> titFase = tituloAtividadeRepository.findByTituloCodigoAndFaseCodigo(titulo, fase);
		if(!titFase.isPresent()) {
			throw new EntidadeNaoEncontradaException("Registro não encontrado");
		}
		tituloAtividadeRepository.deleteById(titFase.get().getCodigo());
	}
	
	public List<FaseTarefaDto> listarFasesNotInTitulo(long titulo, long empresa) {
		List<FaseTarefa> fases = faseTarefaRepository.listarFasesSemTitulo(titulo,empresa);
		List<FaseTarefaDto> dtos = new ArrayList<FaseTarefaDto>();
		if(fases != null && fases.size() > 0) {
			for(FaseTarefa fase : fases) {
				dtos.add(FaseTarefaDto.build(fase));
			}
		}
		
		return dtos;
	}
}
