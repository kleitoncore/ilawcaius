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
import com.br.ilawgestao.domains.models.TipoCusta;
import com.br.ilawgestao.domains.repository.TipoCustaRepository;

@Service
public class TipoCustaService {
	
	@Autowired
	private TipoCustaRepository tipoCustaRepository;
	
	public TipoCusta cadastrarTipoCusta(TipoCusta tipoCusta) {
		Optional<TipoCusta> tipoConsulta = tipoCustaRepository.findByNomeAndEmpresaCodigo(tipoCusta.getNome(), tipoCusta.getEmpresa().getCodigo());
		if(tipoConsulta.isPresent()) {
			throw new EntidadeJaCadastradaException("Tipo de Custa já cadstrada com este nome");
		}
		return tipoCustaRepository.save(tipoCusta);
	}
	
	public List<TipoCusta> listarTiposCustas(long empresa) {
		return tipoCustaRepository.findByEmpresaCodigoOrderByNomeAsc(empresa);
	}
	
	public TipoCusta consultarTipoCusta(long codigo) {
		Optional<TipoCusta> tipoCusta = tipoCustaRepository.findById(codigo);
		if(!tipoCusta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Custa não encontrada");
		}
		
		return tipoCusta.get();
	}
	
	public TipoCusta alterarTipoCusta(long codigo, TipoCusta tipoCusta) {
		TipoCusta tipoConsulta = consultarTipoCusta(codigo);
		Optional<TipoCusta> tipoNome = tipoCustaRepository.findByNomeAndEmpresaCodigo(tipoCusta.getNome(), tipoCusta.getEmpresa().getCodigo());
		if(tipoNome.isPresent() && tipoNome.get().getCodigo() != tipoConsulta.getCodigo()) {
			throw new EntidadeJaCadastradaException("Tipo de Custa já cadastrada com este nome");
		}
		BeanUtils.copyProperties(tipoCusta, tipoConsulta,"codigo");
		return tipoCustaRepository.save(tipoConsulta);
	}
	
	public void excluirTipoCusta(long codigo) {
		TipoCusta tipo = consultarTipoCusta(codigo);
		try {
			tipoCustaRepository.delete(tipo);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException("O Tipo de Custa não pode ser excluído, já está em uso");
		}
	}
	
	public List<TipoCusta> consultarTiposCustasPorCodigos(String codigos) {
		return tipoCustaRepository.listaTiposCustasPorCodigos(codigos);
	}
}
