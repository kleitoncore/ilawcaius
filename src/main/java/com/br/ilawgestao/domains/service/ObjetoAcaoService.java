package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.ObjetoAcaoDto;
import com.br.ilawgestao.domains.exception.EntidadeJaCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.ObjetoAcao;
import com.br.ilawgestao.domains.repository.ObjetoAcaoRepository;

@Service
public class ObjetoAcaoService {
	
	@Autowired
	private ObjetoAcaoRepository objetoAcaoRepository;
		
	public ObjetoAcao cadastrarObjetoAcao(ObjetoAcao objeto) {
		Optional<ObjetoAcao> objetoAcaoEncontrado = objetoAcaoRepository.consultarObjetoAcaoPorNome(objeto.getNome(), 
				objeto.getEmpresa().getCodigo());
		
		if(objetoAcaoEncontrado.isPresent()) {
			throw new EntidadeJaCadastradaException("Objeto de Ação com este nome já cadastrado");
		}
		ObjetoAcao objetoSalvo = objetoAcaoRepository.save(objeto);
		if(objeto.getObjetoPai() == 0) {
			ObjetoAcao ob = new ObjetoAcao();
			ob.setCodigo(objetoSalvo.getCodigo());
			ob.setNome(objetoSalvo.getNome());
			ob.setEmpresa(objetoSalvo.getEmpresa());
			ob.setObjetoPai(objetoSalvo.getCodigo());
			this.alterarObjetoAcaoPai(objetoSalvo.getCodigo(), ob);
		}
		
		return objetoSalvo;
	}
	
	public List<ObjetoAcao> listarObjetosAcao2(long empresa) {
		return objetoAcaoRepository.listarObjetos(empresa);
	}
	
	public List<ObjetoAcaoDto> listarObjetosAcao(long empresa) {
		List<ObjetoAcao> objetos = objetoAcaoRepository.listarObjetos(empresa);
		List<ObjetoAcaoDto> dtos = new ArrayList<ObjetoAcaoDto>();
		if(objetos != null && objetos.size() > 0) {
			for(ObjetoAcao oa : objetos) {
				ObjetoAcaoDto dto = new ObjetoAcaoDto();
				dto.setCodigo(oa.getCodigo());
				dto.setNome(oa.getNome());
				dtos.add(dto);
			}
		}
		return dtos;
	}
	
	public List<ObjetoAcaoDto> listarSubObjetos(long codigo) {
		List<ObjetoAcao> subObjetos = objetoAcaoRepository.listaObjetosFilhos(codigo);
		List<ObjetoAcaoDto> dtos = new ArrayList<ObjetoAcaoDto>();
		for(ObjetoAcao so : subObjetos) {
			ObjetoAcaoDto dto = new ObjetoAcaoDto();
			dto.setCodigo(so.getCodigo());
			dto.setNome(so.getNome());
			dtos.add(dto);
		}
		
		return dtos;
	}
	
	/**
	 * Lista Objetos de Ação que ainda não estão no processo selecionado
	 * @param empresa
	 * @param processo
	 * @return
	 */
	public List<ObjetoAcao> listaObjetosAcaoForaDoProcesso(long empresa, long processo) {
		return objetoAcaoRepository.listarObjetosForaDoProcesso(empresa, processo);
	}
	
	/**
	 * Lista Sub-Objetos de Ação que ainda não estão no processo selecionado
	 * @param empresa
	 * @param processo
	 * @return
	 */
	public List<ObjetoAcao> listarSubObjetosAcaoForaDoProcesso(long processo, long objetoPai) {
		return objetoAcaoRepository.listarSubObjetosForaDoProcesso(processo, objetoPai);
	}
	
	public ObjetoAcao consultarObjetoAcaoPorCodigo(long codigo) {
		Optional<ObjetoAcao> objetoEncontrado = objetoAcaoRepository.findById(codigo);
		if(!objetoEncontrado.isPresent()) {
			throw new EntidadeNaoEncontradaException("Objeto de Ação não eoncontrado");
		}
		return objetoEncontrado.get();
	}
	
	public ObjetoAcao alterarObjetoAcao(long codigo, ObjetoAcao objetoAcao) {
		ObjetoAcao objetoAcaoSalvo = consultarObjetoAcaoPorCodigo(codigo);	
		BeanUtils.copyProperties(objetoAcao, objetoAcaoSalvo,"codigo","objetoPai");
		return objetoAcaoRepository.save(objetoAcaoSalvo);
	}
	
	public ObjetoAcao alterarObjetoAcaoPai(long codigo, ObjetoAcao objetoAcao) {
		ObjetoAcao objetoAcaoSalvo = consultarObjetoAcaoPorCodigo(codigo);	
		BeanUtils.copyProperties(objetoAcao, objetoAcaoSalvo,"codigo");
		return objetoAcaoRepository.save(objetoAcaoSalvo);
	}
	
	public void excluirObjetosAcao(long codigo) {
		Optional<ObjetoAcao> objetoAcaoSalvo = objetoAcaoRepository.findById(codigo);
		if(!objetoAcaoSalvo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Objeto de Ação não encontrado");
		}
		objetoAcaoRepository.deleteById(codigo);
	}
	
	public List<ObjetoAcao> consultarObjetosPorCodigos(String codigos) {
		return objetoAcaoRepository.listaObjetosPorCodigos(codigos);
	}
	
	public List<ObjetoAcao> listaObjetosConsultaRelatorio(long empresa) {
		List<ObjetoAcao> objetos = new ArrayList<ObjetoAcao>();
		List<ObjetoAcao> pais = objetoAcaoRepository.listarObjetos(empresa);
		if(pais != null) {
			for(ObjetoAcao obpai : pais) {
				objetos.add(obpai);
				List<ObjetoAcao> filhos = objetoAcaoRepository.listaObjetosFilhos(obpai.getCodigo());
				if(filhos.size() > 0) {
					for(ObjetoAcao obfilho : filhos) {
						ObjetoAcao of = new ObjetoAcao();
						of.setCodigo(obfilho.getCodigo());
						of.setNome(obfilho.getNome() + " [" + obpai.getNome() + "]");
						of.setEmpresa(obfilho.getEmpresa());
						objetos.add(of);
					}
				}
			}
		}
		
		return objetos;
	}
}
