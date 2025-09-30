package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.PedidoDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.Pedido;
import com.br.ilawgestao.domains.repository.EmpresaRepository;
import com.br.ilawgestao.domains.repository.PedidoRepository;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	public PedidoDto cadastrarPedido(PedidoDto dto) {
		Optional<Empresa> empresa = empresaRepository.findById(dto.getEmpresa().getCodigo());
		if(!empresa.isPresent()) {
			new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		Pedido pedido = Pedido.builder()
				.nome(dto.getNome())
				.empresa(empresa.get()).build();
		
		return PedidoDto.build(pedidoRepository.save(pedido));
	}
	
	public List<PedidoDto> listarPedidos(long empresa) {
		List<PedidoDto> dtos = new ArrayList<PedidoDto>();
		List<Pedido> pedidos = pedidoRepository.findPedidoByEmpresaCodigo(empresa);
		if(pedidos != null && pedidos.size() > 0) {
			for(Pedido pedido : pedidos) {
				dtos.add(PedidoDto.build(pedido));
			}
		}
		
		return dtos;
	}
	
	public PedidoDto consultarPedido(long codigo) {
		Optional<Pedido> pedidoConsulta = pedidoRepository.findById(codigo);
		if(!pedidoConsulta.isPresent()) {
			new EntidadeNaoEncontradaException("Pedido não encontrado");
		}
		
		PedidoDto dto = PedidoDto.build(pedidoConsulta.get());
		return dto;
	}
	
	public PedidoDto alterarPedido(long codigo, PedidoDto dto) {
		PedidoDto pedido = this.consultarPedido(codigo);
		BeanUtils.copyProperties(dto, pedido,"codigo","empresa");
		
		Pedido p = Pedido.builder()
				.codigo(pedido.getCodigo())
				.nome(pedido.getNome())
				.empresa(pedido.getEmpresa()).build();
		
		return PedidoDto.build(pedidoRepository.save(p));
	}
	
	public void excluirPedido(long codigo) {
		this.consultarPedido(codigo);
		pedidoRepository.deleteById(codigo);
	}
}
