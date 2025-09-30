package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.PedidoProcessoDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Pedido;
import com.br.ilawgestao.domains.models.PedidoProcesso;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.repository.PedidoProcessoRepository;
import com.br.ilawgestao.domains.repository.PedidoRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class PedidoProcessoService {
	
	@Autowired
	private PedidoProcessoRepository pedidoProcessoRepository;
		
	@Autowired
	private ProcessoRepository processorepository;;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	public PedidoProcessoDto cadastrarPedidosProcesso(PedidoProcessoDto dto) {
		Optional<Pedido> pedido = pedidoRepository.findById(dto.getPedido().getCodigo());
		if(!pedido.isPresent()) {
			throw new EntidadeNaoEncontradaException("Pedido não encontrado");
		}
		
		Optional<Processo> processo = processorepository.findById(dto.getProcesso().getCodigo());
		if(!processo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		PedidoProcesso pp = PedidoProcesso.builder()
				.pedido(pedido.get())
				.processo(processo.get())
				.vlPossivel(Double.parseDouble(this.checaValor(dto.getVlPossivel())))
				.vlProvavel(Double.parseDouble(this.checaValor(dto.getVlProvavel())))
				.vlRemoto(Double.parseDouble(this.checaValor(dto.getVlRemoto())))
				.vlCausa(Double.parseDouble(this.checaValor(dto.getVlCausa())))
				.dtRegistro(DatasUtil.getDataAtual())
				.build();
		
		return PedidoProcessoDto.build(pedidoProcessoRepository.save(pp));
	}
	
	private String checaValor(String valor) {
		if(valor == null) {
			return "0";
		}
		
		return valor;
	}
	
	public List<PedidoProcessoDto> listaPedidosProcesso(long processoCodigo) {
		Optional<Processo> processo = processorepository.findById(processoCodigo);
		if(!processo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		List<PedidoProcesso> pedidos = pedidoProcessoRepository.findByProcessoCodigo(processoCodigo);
		List<PedidoProcessoDto> dtos = new ArrayList<PedidoProcessoDto>();
		if(pedidos != null && pedidos.size() > 0) {
			for(PedidoProcesso pp : pedidos) {
				dtos.add(PedidoProcessoDto.build(pp));
			}
		}
		
		return dtos;
	}
	
	public PedidoProcessoDto consultarPedidoProcesso(long codigo) {
		return PedidoProcessoDto.buildSimples(pedidoProcessoRepository.findById(codigo).get());
	}
	
	public PedidoProcessoDto alterarPedidoProcesso(long codigo, PedidoProcessoDto dto) {
		PedidoProcessoDto consulta = this.consultarPedidoProcesso(codigo);
		if(consulta == null) {
			throw new EntidadeNaoEncontradaException("Pedido não encontrado");
		}
		
		dto.setDtRegistro(DatasUtil.getDataAtual());
		
		BeanUtils.copyProperties(dto, consulta,"codigo","processo");
		PedidoProcesso pp = PedidoProcesso.builder()
				.codigo(consulta.getCodigo())
				.processo(consulta.getProcesso())
				.pedido(consulta.getPedido())
				.vlPossivel(Double.parseDouble(this.checaValor(consulta.getVlPossivel())))
				.vlRemoto(Double.parseDouble(this.checaValor(consulta.getVlRemoto())))
				.vlProvavel(Double.parseDouble(this.checaValor(consulta.getVlProvavel())))
				.dtRegistro(consulta.getDtRegistro())
				.build();
		
		return PedidoProcessoDto.build(pedidoProcessoRepository.save(pp));
	}
	
	public void excluirPedido(long codigo) {
		pedidoProcessoRepository.deleteById(codigo);
	}
}
