package com.br.ilawgestao.controller;

import com.br.ilawgestao.domains.dto.HistoricoPessoaDto;
import com.br.ilawgestao.domains.dto.HistoricoProcessoDto;
import com.br.ilawgestao.domains.service.HistoricoPessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historicoPessoa")
public class HistoricoPessoaController {
    @Autowired
    private HistoricoPessoaService service;

    @PostMapping
    public ResponseEntity<HistoricoPessoaDto> cadatrarHistorico(@RequestBody HistoricoPessoaDto historico) {
        HistoricoPessoaDto dto = service.cadastrarHistorico(historico);
        return new ResponseEntity<HistoricoPessoaDto>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/por-datas")
    public ResponseEntity<List<HistoricoPessoaDto>> consultarHistorico(@RequestParam String dataInicial, @RequestParam String dataFinal) {
        List<HistoricoPessoaDto> dtos = service.consultarHistoricoPessoa(dataInicial,dataFinal);
        return new ResponseEntity<List<HistoricoPessoaDto>>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity excluirHistorico(@PathVariable long codigo) {
        service.excluirHistorico(codigo);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/por-pessoa/{pessoa}")
    public ResponseEntity<List<HistoricoPessoaDto>> listarTodos(@PathVariable long pessoa) {
        List<HistoricoPessoaDto> dtos = service.listarTodos(pessoa);
        return new ResponseEntity<List<HistoricoPessoaDto>>(dtos, HttpStatus.OK);
    }
}
