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
import com.br.ilawgestao.domains.models.TipoAcao;
import com.br.ilawgestao.domains.repository.TipoAcaoRepository;

@Service
public class TipoAcaoService {
	
	@Autowired
	private TipoAcaoRepository tipoAcaoRepository;
	
	public TipoAcao cadastrarTipoAcao(TipoAcao tipoAcao) {
		Optional<TipoAcao> tipoAcaoConsulta = tipoAcaoRepository.findByNomeAndEmpresaCodigo(tipoAcao.getNome(), 
				tipoAcao.getEmpresa().getCodigo());
		if(tipoAcaoConsulta.isPresent()) {
			throw new EntidadeJaCadastradaException("Já existe um Tipo de Ação cadastrado com este nome");
		}
		
		return tipoAcaoRepository.save(tipoAcao);
	}
	
	public List<TipoAcao> listarTipoAcao(long empresa) {
		List<TipoAcao> lista = new ArrayList<TipoAcao>();
		List<TipoAcao> entitys = tipoAcaoRepository.findByEmpresaCodigoOrderByNomeAsc(empresa);
		if(entitys != null) {
			for(TipoAcao tipo : entitys) {
				TipoAcao ta = new TipoAcao();
				if(!tipo.getNome().equals("Agravo de Instrumento") && !tipo.getNome().equals("Processo Apenso")) {
					ta.setCodigo(tipo.getCodigo());
					ta.setNome(tipo.getNome());
					ta.setEmpresa(tipo.getEmpresa());
					lista.add(ta);
				}
			}
		}
		
		return lista;
	}
	
	public TipoAcao consultarTipoAcao(long codigo) {
		Optional<TipoAcao> tipoAcaoConsulta = tipoAcaoRepository.findById(codigo);
		if(!tipoAcaoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Ação não encontrado");
		}
		
		return tipoAcaoConsulta.get();
	}
	
	public TipoAcao alterarTipoAcao(long codigo, TipoAcao tipoAcao) {
		TipoAcao tipoAcaoConsulta = consultarTipoAcao(codigo);
		Optional<TipoAcao> tipoAcaoNome = tipoAcaoRepository.findByNomeAndEmpresaCodigo(tipoAcao.getNome(), tipoAcao.getEmpresa().getCodigo());
		if(tipoAcaoNome.isPresent() && tipoAcaoNome.get().getCodigo() != tipoAcaoConsulta.getCodigo()) {
			throw new EntidadeJaCadastradaException("Tipo de Ação já cadastrado com este nome");
		}
		
		BeanUtils.copyProperties(tipoAcao, tipoAcaoConsulta, "codigo");
		return tipoAcaoRepository.save(tipoAcaoConsulta);
	}
	
	public void excluirTipoAcao(long codigo) {
		TipoAcao tipoAcaoConsulta = consultarTipoAcao(codigo);
		try {
			tipoAcaoRepository.delete(tipoAcaoConsulta);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException("Tipo de Ação não pode ser excluído, já está em uso");
		}
	}
}
