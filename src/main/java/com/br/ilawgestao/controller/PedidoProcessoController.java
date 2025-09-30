package com.br.ilawgestao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.PedidoProcessoDto;
import com.br.ilawgestao.domains.service.PedidoProcessoService;

@RestController
@RequestMapping("/pedidoProcesso")
public class PedidoProcessoController {
	
	@Autowired
	private PedidoProcessoService service;
	
	@PostMapping
	public ResponseEntity<PedidoProcessoDto> cadastrarPedidoProcesso(@RequestBody PedidoProcessoDto pedidoProcessoDto) {
		PedidoProcessoDto dto = service.cadastrarPedidosProcesso(pedidoProcessoDto);
		return new ResponseEntity<PedidoProcessoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/pedidosPorProcesso/{processo}")
	public ResponseEntity<List<PedidoProcessoDto>> listarPedidosPorProcesso(@PathVariable long processo) {
		List<PedidoProcessoDto> dtos = service.listaPedidosProcesso(processo);
		return new ResponseEntity<List<PedidoProcessoDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<PedidoProcessoDto> consultarPedidoProcesso(@PathVariable long codigo) {
		PedidoProcessoDto dto = service.consultarPedidoProcesso(codigo);
		return new ResponseEntity<PedidoProcessoDto>(dto, HttpStatus.OK);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<PedidoProcessoDto> alterarPedidoProcesso(@PathVariable long codigo, @RequestBody PedidoProcessoDto pedidoProcessoDto) {
		PedidoProcessoDto dto = service.alterarPedidoProcesso(codigo, pedidoProcessoDto);
		return new ResponseEntity<PedidoProcessoDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirPedidoProcesso(@PathVariable long codigo) {
		service.excluirPedido(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
