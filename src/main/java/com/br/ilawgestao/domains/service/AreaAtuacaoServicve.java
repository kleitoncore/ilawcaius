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
import com.br.ilawgestao.domains.models.AreaAtuacao;
import com.br.ilawgestao.domains.repository.AreaAtuacaoRepository;


@Service
public class AreaAtuacaoServicve {
	
	@Autowired
	private AreaAtuacaoRepository areaAtuacaoRepository;
	
	public AreaAtuacao cadastrarAreaAtuacao(AreaAtuacao areaAtuacao) {
		Optional<AreaAtuacao> areaAtuacaoConsulta = areaAtuacaoRepository.findByNomeAndEmpresaCodigo(areaAtuacao.getNome(), 
				areaAtuacao.getEmpresa().getCodigo());
		if(areaAtuacaoConsulta.isPresent()) {
			throw new EntidadeJaCadastradaException("Area de Atuação já cadastrada com este nome");
		}
		return areaAtuacaoRepository.save(areaAtuacao);
	}
	
	public List<AreaAtuacao> listarAreaAtuacao(long empresa) {
		List<AreaAtuacao> lista = new ArrayList<AreaAtuacao>();
		List<AreaAtuacao> entitys = areaAtuacaoRepository.findByEmpresaCodigoOrderByNomeAsc(empresa);
		if(entitys != null) {
			for(AreaAtuacao area : entitys) {
				AreaAtuacao are = new AreaAtuacao();
				if(!area.getNome().equals("Agravo de Instrumento") && !area.getNome().equals("Processo Apenso")) {
					are.setCodigo(area.getCodigo());
					are.setNome(area.getNome());
					are.setEmpresa(area.getEmpresa());
					lista.add(are);
				}
			}
		}
		
		return lista;
	}
	
	public AreaAtuacao consultarAreaAtuacao(long codigo) {
		Optional<AreaAtuacao> areaAtuacao = areaAtuacaoRepository.findById(codigo);
		if(!areaAtuacao.isPresent()) {
			throw new EntidadeNaoEncontradaException("Area de Atuação não encontrada");
		}
		
		return areaAtuacao.get(); 
	}
	
	public AreaAtuacao alterarAreaAtuacao(long codigo, AreaAtuacao areaAtuacao) {
		AreaAtuacao areaAtuacaoConsulta = consultarAreaAtuacao(codigo);
		Optional<AreaAtuacao> areaAtuacaoNome = areaAtuacaoRepository.findByNomeAndEmpresaCodigo(areaAtuacao.getNome(), areaAtuacao.getEmpresa().getCodigo());
		if(areaAtuacaoNome.isPresent() && areaAtuacaoNome.get().getCodigo() != areaAtuacaoConsulta.getCodigo()) {
			throw new EntidadeJaCadastradaException("Já existe uma Area de Atuação cadastrada com este nome");
		}
		
		BeanUtils.copyProperties(areaAtuacao, areaAtuacaoConsulta,"codigo");
		return areaAtuacaoRepository.save(areaAtuacaoConsulta);
	}
	
	public void excluirAreaAtuacao(long codigo) {
		AreaAtuacao areaAtuacao = consultarAreaAtuacao(codigo);
		try {
			areaAtuacaoRepository.delete(areaAtuacao);			
		} catch (DataIntegrityViolationException e ) {
			throw new EntidadeEmUsoException("Area de Atuação não póde ser excluída, já está em uso");
		}
	}
}
