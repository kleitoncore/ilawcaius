package com.br.ilawgestao.controller;

import com.br.ilawgestao.domains.dto.ConfiguracaoProcessoParadoDto;
import com.br.ilawgestao.domains.service.ProcessosParadadosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuracao-processo-parado")
public class ConfiguracaoProcessoParadoController {

    @Autowired
    private ProcessosParadadosService service;

    @GetMapping("/{empresa}")
    public ResponseEntity<ConfiguracaoProcessoParadoDto> consultar(@PathVariable long empresa) {
        ConfiguracaoProcessoParadoDto dto = service.consultar(empresa);
        return new ResponseEntity<ConfiguracaoProcessoParadoDto>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ConfiguracaoProcessoParadoDto> alterar(@RequestBody ConfiguracaoProcessoParadoDto conf) {
        ConfiguracaoProcessoParadoDto dto = service.altrerar(conf);
        return new ResponseEntity<ConfiguracaoProcessoParadoDto>(dto, HttpStatus.OK);
    }
}
