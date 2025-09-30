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

import com.br.ilawgestao.domains.dto.PedidoDto;
import com.br.ilawgestao.domains.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoService pedidoService;
	
	@GetMapping("/{empresa}")
	public ResponseEntity<List<PedidoDto>> listarPedidos(@PathVariable long empresa) {
		List<PedidoDto> dtos = pedidoService.listarPedidos(empresa);
		return new ResponseEntity<List<PedidoDto>>(dtos, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<PedidoDto> cadastrarPedido(@RequestBody PedidoDto pedidoDto) {
		PedidoDto dto = pedidoService.cadastrarPedido(pedidoDto);
		return new ResponseEntity<PedidoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/porCodigo/{codigo}")
	public ResponseEntity<PedidoDto> consultarPedidoPorCodigo(@PathVariable long codigo) {
		PedidoDto dto = pedidoService.consultarPedido(codigo);
		return new ResponseEntity<PedidoDto>(dto, HttpStatus.OK);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<PedidoDto> alterarPedido(@PathVariable long codigo, @RequestBody PedidoDto pedidoDto) {
		PedidoDto dto = pedidoService.alterarPedido(codigo, pedidoDto);
		return new ResponseEntity<PedidoDto>(dto, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirPedido(@PathVariable long codigo) {
		pedidoService.excluirPedido(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
