package com.br.ilawgestao.controller;

import com.br.ilawgestao.domains.dto.ArquivoTransferenciaDto;
import com.br.ilawgestao.domains.dto.TransferenciaFinanceiraDto;
import com.br.ilawgestao.domains.repository.filtros.FiltroTranferenciaFinanceira;
import com.br.ilawgestao.domains.service.TransferenciaFinanceiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/transferencia-financeira")
public class TransferenciaFinanceiraController {

    @Autowired
    private TransferenciaFinanceiraService service;

    @PostMapping("/transferir")
    public ResponseEntity<TransferenciaFinanceiraDto> transferir(@RequestBody TransferenciaFinanceiraDto trans) {
        TransferenciaFinanceiraDto dto = service.cadastrarTransferenciaFinanceira(trans);
        return new ResponseEntity<TransferenciaFinanceiraDto>(dto,HttpStatus.OK);
    }

    @GetMapping("/consultar-transferencias")
    public ResponseEntity<List<TransferenciaFinanceiraDto>> consultarTransferencias(@RequestParam String dataInicial,
                                                                                    @RequestParam String dataFinal,
                                                                                    @RequestParam long conta,
                                                                                    @RequestParam String tipo,
                                                                                    @RequestParam long empresa,
                                                                                    @RequestParam long codigo) {

        FiltroTranferenciaFinanceira filtro = new FiltroTranferenciaFinanceira();
        filtro.setDataInicial(dataInicial);
        filtro.setDataFinal(dataFinal);
        filtro.setConta(conta);
        filtro.setTipo(tipo);
        filtro.setEmpresa(empresa);
        filtro.setCodigo(codigo);

        List<TransferenciaFinanceiraDto> dtos = service.consultarTransferencias(filtro);
        return new ResponseEntity<List<TransferenciaFinanceiraDto>>(dtos, HttpStatus.OK);
    }

    @PostMapping(value = "/arquivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArquivoTransferenciaDto> incluirArquivo(@RequestParam MultipartFile file) {
        ArquivoTransferenciaDto dto = service.processarArquivo(file);
        return new ResponseEntity<ArquivoTransferenciaDto>(dto, HttpStatus.OK);
    }
}
