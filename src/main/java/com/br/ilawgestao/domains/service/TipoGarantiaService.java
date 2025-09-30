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
import com.br.ilawgestao.domains.models.TipoGarantia;
import com.br.ilawgestao.domains.repository.TipoGarantiaRepository;

@Service
public class TipoGarantiaService {
	
	@Autowired
	private TipoGarantiaRepository repository;
	
	public TipoGarantia cadastrarTipoGarantia(TipoGarantia tipo) {
		Optional<TipoGarantia> tipoConsulta = repository.findByNomeAndEmpresaCodigo(tipo.getNome(), tipo.getEmpresa().getCodigo());
		if(tipoConsulta.isPresent()) {
			throw new EntidadeJaCadastradaException("Já existe um Tipo de Garantia com este nome");
		}
		return repository.save(tipo);
	}
	
	public List<TipoGarantia> listarTiposGarantias(long empresa) {
		return repository.findByEmpresaCodigoOrderByNome(empresa);
	}
	
	public TipoGarantia consultarTipoGarantia(long codigo) {
		Optional<TipoGarantia> tipoGarantia = repository.findById(codigo);
		if(!tipoGarantia.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Garantia não encontrada");
		}
		return tipoGarantia.get();
	}
	
	public TipoGarantia alterarTipoGarantioa(long codigo, TipoGarantia tipo) {
		TipoGarantia tipoConsulta = consultarTipoGarantia(tipo.getCodigo());
		Optional<TipoGarantia> tipoNome = repository.findByNomeAndEmpresaCodigo(tipo.getNome(), tipo.getEmpresa().getCodigo());
		if(tipoNome.isPresent() && tipoNome.get().getCodigo() != tipoConsulta.getCodigo()) {
			throw new EntidadeJaCadastradaException("Já existe um Tipo de Garantia com este nome");
		}
		BeanUtils.copyProperties(tipo, tipoConsulta,"codigo");
		return repository.save(tipoConsulta);
	}
	
	public void excluirTipoGarantia(long codigo) {
		TipoGarantia tipoConsulta = consultarTipoGarantia(codigo);
		try {
			repository.delete(tipoConsulta);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException("Tipo de Garantia não pode ser excluída, já está em uso");
		}
	}
}
