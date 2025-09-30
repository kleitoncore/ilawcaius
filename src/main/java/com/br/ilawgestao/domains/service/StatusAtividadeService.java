package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.br.ilawgestao.domains.dto.StatusAtividadeDto;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.StatusAtividade;
import com.br.ilawgestao.domains.repository.StatusAtividadeRepository;

@Service
public class StatusAtividadeService {
	
	@Autowired
	private StatusAtividadeRepository statusRepository;
	
	public StatusAtividadeDto cadastrarStatus(StatusAtividadeDto dto) {
		StatusAtividade status = StatusAtividade.builder()
				.status(dto.getStatus())
				.cor("#" + dto.getCor())
				.empresa(dto.getEmpresa())
				.altera("S")
				.build();

		return StatusAtividadeDto.build(statusRepository.save(status));
	}

	public StatusAtividadeDto alterarStatus(StatusAtividadeDto dto) {
		Optional<StatusAtividade> statusConsulta = statusRepository.findById(dto.getCodigo());
		if(!statusConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Status de atividade não encontrada");
		}

		if(!statusConsulta.get().getStatus().equals(dto.getStatus()) && statusConsulta.get().getAltera().equals("N")) {
			throw new EntidadeEmUsoException("Para este Status, você só pode alterar a cor");
		}

		StatusAtividade status = StatusAtividade.builder()
				.codigo(statusConsulta.get().getCodigo())
				.status(dto.getStatus())
				.empresa(statusConsulta.get().getEmpresa())
				.cor("#" + dto.getCor())
				.altera(statusConsulta.get().getAltera())
				.build();
		return StatusAtividadeDto.build(statusRepository.save(status));
	}

	public List<StatusAtividade> listarStatus(long empresa) {
		List<StatusAtividade> lista = statusRepository.findByEmpresaCodigo(empresa);
		return lista;
	}
	
	public StatusAtividade consultarStatusAtividade(long codigo) {
		Optional<StatusAtividade> status = statusRepository.findById(codigo);
		if(!status.isPresent()) {
			throw new EntidadeNaoEncontradaException("Status de Atividade não encontrado");
		}
		
		return status.get();
	}

	public StatusAtividadeDto consultar(long codigo) {
		Optional<StatusAtividade> status = statusRepository.findById(codigo);
		if(!status.isPresent()) {
			throw new EntidadeNaoEncontradaException("Status de Atividade não encontrado");
		}

		return StatusAtividadeDto.build(status.get());
	}

	public void excluirStatus(long codigo) {
		Optional<StatusAtividade> status = statusRepository.findById(codigo);
		if(!status.isPresent()) {
			throw new EntidadeNaoEncontradaException("Status de Atividade não encontrado");
		}

		if(status.get().getAltera().equals("N")) {
			throw new EntidadeEmUsoException("Este tipo de Status de Atividade não pode ser excluído");
		}

		statusRepository.deleteById(codigo);
	}
}
