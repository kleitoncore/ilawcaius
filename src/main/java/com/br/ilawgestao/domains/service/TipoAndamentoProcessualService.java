package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.TipoAndamentoProcessualDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.TipoAndamentoProcessual;
import com.br.ilawgestao.domains.repository.EmpresaRepository;
import com.br.ilawgestao.domains.repository.TipoAndamentoProcessualRepository;

@Service
public class TipoAndamentoProcessualService {
	
	@Autowired
	private TipoAndamentoProcessualRepository repository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	public TipoAndamentoProcessualDto cadastrar(TipoAndamentoProcessualDto dto) {
		Optional<Empresa> empresa = empresaRepository.findById(dto.getEmpresa().getCodigo());
		if(!empresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		TipoAndamentoProcessual tipo = TipoAndamentoProcessual.builder()
				.nome(dto.getNome())
				.empresa(dto.getEmpresa())
				.build();
		return TipoAndamentoProcessualDto.build(repository.save(tipo));
	}
	
	public List<TipoAndamentoProcessualDto> listar(long empresa) {
		List<TipoAndamentoProcessual> tipos = repository.findByEmpresaCodigoOrderByNomeAsc(empresa);
		List<TipoAndamentoProcessualDto> dtos = new ArrayList<TipoAndamentoProcessualDto>();
		if(tipos != null) {
			for(TipoAndamentoProcessual tipo : tipos) {
				dtos.add(TipoAndamentoProcessualDto.build(tipo));
			}
		}
		
		return dtos;
	}
	
	public TipoAndamentoProcessualDto consultar(long codigo) {
		return TipoAndamentoProcessualDto.build(repository.findById(codigo).get());
	}
	
	public TipoAndamentoProcessualDto alterar(long codigo, TipoAndamentoProcessualDto dto) {
		TipoAndamentoProcessualDto consulta = this.consultar(codigo);
		if(consulta == null) {
			throw new EntidadeNaoEncontradaException("Tipo de Andamento Processual não encontrado");
		}
		
		BeanUtils.copyProperties(dto, consulta, "codigo", "empresa");
		TipoAndamentoProcessual tipo = TipoAndamentoProcessual.builder()
				.codigo(consulta.getCodigo())
				.nome(consulta.getNome())
				.empresa(consulta.getEmpresa())
				.build();
		return TipoAndamentoProcessualDto.build(repository.save(tipo));
	}
	
	public void excluirTipo(long codigo) {
		repository.deleteById(codigo);
	}
}
