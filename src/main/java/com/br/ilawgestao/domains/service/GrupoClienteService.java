package com.br.ilawgestao.domains.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeJaCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.GrupoCliente;
import com.br.ilawgestao.domains.repository.GrupoClienteRepository;

@Service
public class GrupoClienteService {
	
	@Autowired
	private GrupoClienteRepository repository;
	
	public List<GrupoCliente> listarGruposCliente(long codigoEmpresa) {
		return repository.consultarGruposClientesPorEmpresa(codigoEmpresa);
	}
	
	public GrupoCliente cadastrarGrupoCliente(GrupoCliente grupoCliente) {
		Optional<GrupoCliente> grupoConsulta = repository.consultarGrupoPorNome(grupoCliente.getNome(), grupoCliente.getEmpresa().getCodigo());
		if(grupoConsulta.isPresent()) {
			throw new EntidadeJaCadastradaException("Já existe um Grupo de Cliente com este nome");
		}
		
		return repository.save(grupoCliente);
	}
	
	public GrupoCliente consultarGrupoCliente(long codigo) {
		Optional<GrupoCliente> grupoConsulta = repository.findById(codigo);
		if(!grupoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Cliente não encontrado");
		}
		
		return grupoConsulta.get();
	}
	
	public GrupoCliente alterarGrupoCliente(long codigo, GrupoCliente grupoCliente) {
		GrupoCliente grupoConsulta = consultarGrupoCliente(codigo);
		Optional<GrupoCliente> grupoConsultaNome = repository.consultarGrupoPorNome(grupoCliente.getNome(), grupoCliente.getEmpresa().getCodigo());
		if(grupoConsultaNome.isPresent() && grupoConsultaNome.get().getCodigo() != grupoConsulta.getCodigo()) {
			throw new EntidadeJaCadastradaException("Grupo de Cliente já cadastrado com este nome");
		}
		BeanUtils.copyProperties(grupoCliente, grupoConsulta,"codigo");
		return repository.save(grupoConsulta);
	}
	
	public void excluirGrupoCliente(long codigo) {
		GrupoCliente grupo = consultarGrupoCliente(codigo);
		try {
			repository.delete(grupo);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException("Grupo de Cliente não pode ser excluído, já está em uso");
		}
	}
}
