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
import com.br.ilawgestao.domains.models.TipoDecisao;
import com.br.ilawgestao.domains.repository.TipoDecisaoRepository;

@Service
public class TipoDecisaoService {
	
	@Autowired
	private TipoDecisaoRepository repository;
	
	public TipoDecisao cadastrarTipoDecisao(TipoDecisao tipo) {
		Optional<TipoDecisao> tipoConsulta = repository.findByNomeAndEmpresaCodigo(tipo.getNome(), tipo.getEmpresa().getCodigo());
		if(tipoConsulta.isPresent()) {
			throw new EntidadeJaCadastradaException("Tipo de Decisão já cadastrada com este nome");
		}
		return repository.save(tipo);
	}
	
	public List<TipoDecisao> listarTiposDecisao(long empresa) {
		return repository.findByEmpresaCodigoOrderByNome(empresa);
	}
	
	public TipoDecisao consultarTipoDecisao(long codigo) {
		Optional<TipoDecisao> tipo = repository.findById(codigo);
		if(!tipo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Decisão não encontrado");
		}
		return tipo.get();
	}
	
	public TipoDecisao alterarTipoDecisao(long codigo, TipoDecisao tipo) {
		TipoDecisao tipoConsulta = consultarTipoDecisao(codigo);
		Optional<TipoDecisao> tipoNome = repository.findByNomeAndEmpresaCodigo(tipo.getNome(), tipo.getEmpresa().getCodigo());
		if(tipoNome.isPresent() && tipoNome.get().getCodigo() != tipoConsulta.getCodigo()) {
			throw new EntidadeJaCadastradaException("Tipo de Decisão já cadastrada com este nome");
		}
		BeanUtils.copyProperties(tipo, tipoConsulta,"codigo");
		return repository.save(tipoConsulta);
	}
	
	public void excluirTipoDecisao(long codigo) {
		TipoDecisao tipoConsulta = consultarTipoDecisao(codigo);
		try {
			repository.delete(tipoConsulta);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException("Tipo de Decisão não pode ser excluído, já está em uso");
		}
	}
}
