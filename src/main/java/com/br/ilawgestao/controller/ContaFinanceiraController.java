package com.br.ilawgestao.controller;

import com.br.ilawgestao.domains.dto.ContaFinanceiraDto;
import com.br.ilawgestao.domains.service.ContaFinanceiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contaFinanceira")
public class ContaFinanceiraController {

    @Autowired
    private ContaFinanceiraService service;

    @PostMapping
    public ResponseEntity<ContaFinanceiraDto> cadastrar(@RequestBody ContaFinanceiraDto conta) {
        ContaFinanceiraDto dto = service.cadastrar(conta);
        return new ResponseEntity<ContaFinanceiraDto>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{empresa}")
    public ResponseEntity<List<ContaFinanceiraDto>> listar(@PathVariable long empresa) {
        List<ContaFinanceiraDto> dtos = service.listar(empresa);
        return new ResponseEntity<List<ContaFinanceiraDto>>(dtos,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ContaFinanceiraDto> alterar(@RequestBody ContaFinanceiraDto conta) {
        ContaFinanceiraDto dto = service.alterar(conta);
        return new ResponseEntity<ContaFinanceiraDto>(dto,HttpStatus.OK);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity excluir(@PathVariable long codigo) {
        service.excluirContaFinanceira(codigo);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/consultar-por-codigo/{codigo}")
    public ResponseEntity<ContaFinanceiraDto> consultar(@PathVariable long codigo) {
        ContaFinanceiraDto dto = service.consultar(codigo);
        return new ResponseEntity<ContaFinanceiraDto>(dto,HttpStatus.OK);
    }

    @GetMapping("/ativos/{empresa}")
    public ResponseEntity<List<ContaFinanceiraDto>> listarAtivos(@PathVariable long empresa) {
        List<ContaFinanceiraDto> dtos = service.listarAtivos(empresa);
        return new ResponseEntity<List<ContaFinanceiraDto>>(dtos,HttpStatus.OK);
    }
}