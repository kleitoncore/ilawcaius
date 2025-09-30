package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.CheckListAtividadeDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.CheckListAtividade;
import com.br.ilawgestao.domains.models.TituloAtividade;
import com.br.ilawgestao.domains.repository.CheckListRepository;
import com.br.ilawgestao.domains.repository.TituloAtividadeRepository;

@Service
public class CheckListAtividadeService {
	
	@Autowired
	private CheckListRepository checkListRepository;
	
	@Autowired
	private TituloAtividadeRepository tituloAtividadeRepository;
	
	public CheckListAtividadeDto cadastrar(CheckListAtividadeDto dto) {
		Optional<TituloAtividade> atividade = tituloAtividadeRepository.findById(dto.getAtividade().getCodigo());
		if(!atividade.isPresent()) {
			throw new EntidadeNaoEncontradaException("Título de atividade não encontrado");
		}
		
		CheckListAtividade checkList = CheckListAtividade.builder()
				.atividade(atividade.get())
				.ordem(dto.getOrdem())
				.tarefa(dto.getTarefa())
				.descricao(dto.getDescricao())
				.build();
		
		return CheckListAtividadeDto.build(checkListRepository.save(checkList));
	}
	
	public List<CheckListAtividadeDto> listar(long codigo) {
		Optional<TituloAtividade> atividade = tituloAtividadeRepository.findById(codigo);
		if(!atividade.isPresent()) {
			throw new EntidadeNaoEncontradaException("Título de atividade não encontrado");
		}
		
		List<CheckListAtividade> lista = checkListRepository.findByAtividadeCodigoOrderByOrdem(atividade.get().getCodigo());
		List<CheckListAtividadeDto> dtos = new ArrayList<CheckListAtividadeDto>();
		if(lista != null && lista.size() > 0) {
			for(CheckListAtividade ch : lista) {
				dtos.add(CheckListAtividadeDto.build(ch));
			}
		}
		
		return dtos;
	}
	
	public CheckListAtividadeDto alterar(CheckListAtividadeDto dto) {
		CheckListAtividade checkList = CheckListAtividade.builder()
				.codigo(dto.getCodigo())
				.atividade(dto.getAtividade())
				.ordem(dto.getOrdem())
				.tarefa(dto.getTarefa())
				.descricao(dto.getDescricao())
				.build();
		
		return CheckListAtividadeDto.build(checkListRepository.save(checkList));
	}
	
	public void excluir(long codigo) {
		checkListRepository.deleteById(codigo);
	}
}
