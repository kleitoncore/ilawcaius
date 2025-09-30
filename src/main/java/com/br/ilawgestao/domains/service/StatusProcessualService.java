package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeJaCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.StatusProcessual;
import com.br.ilawgestao.domains.repository.StatusProcessualRepository;

@Service
public class StatusProcessualService {
	
	@Autowired
	private StatusProcessualRepository statusProcessualRepository;
	
	public StatusProcessual cadastrarStatusProcessual(StatusProcessual statusProcessual) {
		Optional<StatusProcessual> statusConsulta = statusProcessualRepository.findByDescricaoAndEmpresaCodigo(statusProcessual.getDescricao(),
				statusProcessual.getEmpresa().getCodigo());
		if(statusConsulta.isPresent()) {
			throw new EntidadeJaCadastradaException("Status Processual já cadastrado com este nome");
		}
		return statusProcessualRepository.save(statusProcessual);
	}
	
	public List<StatusProcessual> listarStatusProcessuais(long codigo) {
		List<StatusProcessual> lista = new ArrayList<StatusProcessual>();
		List<StatusProcessual> entitys = statusProcessualRepository.findByEmpresaCodigoOrderByDescricaoAsc(codigo);
		if(entitys != null) {
			for(StatusProcessual status : entitys) {
				StatusProcessual sp = new StatusProcessual();
				if(!status.getDescricao().equals("Agravo de Instrumento") && !status.getDescricao().equals("Processo Apenso")) {
					sp.setCodigo(status.getCodigo());
					sp.setDescricao(status.getDescricao());
					sp.setEmpresa(status.getEmpresa());
					lista.add(sp);
				}
			}
		}
		
		return lista;
	}
	
	public StatusProcessual consultarStatusProcessual(long codigo) {
		Optional<StatusProcessual> status = statusProcessualRepository.findById(codigo);
		if(!status.isPresent()) {
			throw new EntidadeNaoEncontradaException("Status Processual não encontrado");
		}
		
		return status.get(); 
	}
	
	public StatusProcessual alterarStatusProcessual(long codigo, StatusProcessual statusProcessual) {
		StatusProcessual statusConsulta = consultarStatusProcessual(codigo);
		Optional<StatusProcessual> statusNome = statusProcessualRepository.findByDescricaoAndEmpresaCodigo(statusProcessual.getDescricao(), 
				statusProcessual.getEmpresa().getCodigo());
		if(statusNome.isPresent() && statusConsulta.getCodigo() != statusNome.get().getCodigo()) {
			throw new EntidadeJaCadastradaException("Status Processual já cadastrado com este nome");
		}
		BeanUtils.copyProperties(statusProcessual, statusConsulta,"codigo");
		return statusProcessualRepository.save(statusConsulta);
	}
	
	public void excluirStatusProcessual(long codigo) {
		StatusProcessual statusConsulta = consultarStatusProcessual(codigo);
		try {
			statusProcessualRepository.delete(statusConsulta);
		} catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException("Status Processual não pode ser excluído, já está em uso");
		}
	}
}
