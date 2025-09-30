package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.RitoDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Rito;
import com.br.ilawgestao.domains.repository.RitoRepository;

@Service
public class RitoService {
	
	@Autowired
	private RitoRepository ritoRepository;
	
	public RitoDto cadastrar(RitoDto ritoDto) {
		Rito rito = Rito.builder()
				.nome(ritoDto.getNome())
				.empresa(ritoDto.getEmpresa())
				.build();
		RitoDto dto = RitoDto.build(ritoRepository.save(rito));
		return dto;
	}
	
	public List<RitoDto> listar(long empresa) {
		List<Rito> ritos = ritoRepository.findByEmpresaCodigoOrderByNomeAsc(empresa);
		List<RitoDto> dtos = new ArrayList<RitoDto>();
		if(ritos != null) {
			for(Rito rito :ritos) {
				dtos.add(RitoDto.build(rito));
			}
		}
		return dtos;
	}
	
	public RitoDto consultar(long codigo) {
		return RitoDto.build(ritoRepository.findById(codigo).get());
	}
	
	public RitoDto alterar(long codigo, RitoDto ritoDto) {
		RitoDto consulta = this.consultar(codigo);
		if(consulta == null) {
			new EntidadeNaoEncontradaException("Rito não encontrado");
		}
		
		BeanUtils.copyProperties(ritoDto, consulta,"codigo","empresa");
		Rito rito = Rito.builder()
				.codigo(consulta.getCodigo())
				.nome(consulta.getNome())
				.empresa(consulta.getEmpresa())
				.build();
		return RitoDto.build(ritoRepository.save(rito));
	}
	
	public void excluir(long codigo) {
		ritoRepository.deleteById(codigo);
	}
}
