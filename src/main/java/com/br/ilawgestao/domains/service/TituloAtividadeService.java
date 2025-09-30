package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.br.ilawgestao.domains.models.Fase;
import com.br.ilawgestao.domains.repository.FaseRepository;
import com.br.ilawgestao.domains.repository.filtros.FiltroTituloAtividade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.TituloAtividadeDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.TituloAtividade;
import com.br.ilawgestao.domains.models.TituloAtividadeFase;
import com.br.ilawgestao.domains.repository.TituloAtividadeFaseRepository;
import com.br.ilawgestao.domains.repository.TituloAtividadeRepository;

@Service
public class TituloAtividadeService {
	
	@Autowired
	private TituloAtividadeRepository tituloRepository;

	@Autowired
	private FaseRepository faseRepository;
	
	public TituloAtividadeDto cadastrar(TituloAtividadeDto dto) {	
		TituloAtividade titulo = TituloAtividade.builder()
				.titulo(dto.getTitulo())
				.empresa(dto.getEmpresa())
				.pontos(dto.getPontos())
				.fase(dto.getFase())
				.status("A")
				.build();
		TituloAtividade tituloSalvo = tituloRepository.save(titulo);
		
		return TituloAtividadeDto.buildConsulta(tituloSalvo);
	}
	
	public List<TituloAtividadeDto> listar(long empresa) {
		List<TituloAtividade> entitys = tituloRepository.findByEmpresaCodigoOrderByTitulo(empresa);
		List<TituloAtividadeDto> dtos = new ArrayList<TituloAtividadeDto>();
		if(entitys != null && entitys.size() > 0) {
			for(TituloAtividade titulo : entitys) {
				dtos.add(TituloAtividadeDto.buildConsulta(titulo));
			}
		}

		return dtos;
	}
	
	public TituloAtividadeDto consultar(long codigo) {
		Optional<TituloAtividade> titulo = tituloRepository.findById(codigo);
		return TituloAtividadeDto.buildConsulta(titulo.get());
	}
	
	public TituloAtividadeDto alterar(TituloAtividadeDto dto) {
		TituloAtividadeDto titulo = this.consultar(dto.getCodigo());
		TituloAtividade tituloAtividade = TituloAtividade.builder()
				.codigo(titulo.getCodigo())
				.titulo(dto.getTitulo())
				.empresa(titulo.getEmpresa())
				.pontos(dto.getPontos())
				.fase(dto.getFase())
				.status(dto.getStatus())
				.build();
		return TituloAtividadeDto.build(tituloRepository.save(tituloAtividade));
	}
	
	public TituloAtividadeDto consultarPorNome(String nome, long empresa) {
		Optional<TituloAtividade> entity = tituloRepository.findByTituloAndEmpresaCodigo(nome, empresa);
		if(!entity.isPresent()) {
			throw new EntidadeNaoEncontradaException("Título de Atividade não encontrado");
		}
		
		return TituloAtividadeDto.build(entity.get());
	}

	public List<TituloAtividadeDto> consultarTitulosPorFase(long fase) {
		Optional<Fase> faseConsulta = faseRepository.findById(fase);
		if(!faseConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Fase de Processo não encontrado");
		}

		List<TituloAtividade> titulos = tituloRepository.findByFaseCodigo(fase);
		List<TituloAtividadeDto> dtos = new ArrayList<TituloAtividadeDto>();
		if(titulos != null && titulos.size() > 0) {
			for(TituloAtividade tit : titulos) {
				dtos.add(TituloAtividadeDto.buildConsulta(tit));
			}
		}

		return dtos;
	}

	public List<TituloAtividadeDto> filtrarTitulosAtividades(FiltroTituloAtividade filtro) {
		List<TituloAtividade> entitys = tituloRepository.consultarTituloAtividade(filtro);
		List<TituloAtividadeDto> dtos = new ArrayList<TituloAtividadeDto>();
		if(entitys != null && entitys.size() > 0) {
			for(TituloAtividade titulo : entitys) {
				dtos.add(TituloAtividadeDto.buildConsulta(titulo));
			}
		}

		return dtos;
	}
	
	public void excluir(long codigo) {
		tituloRepository.deleteById(codigo);
	}
}
