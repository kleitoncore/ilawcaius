package com.br.ilawgestao.controller;

import com.br.ilawgestao.domains.dto.FiltroAniversariantesDto;
import com.br.ilawgestao.domains.dto.PessoaDTO;
import com.br.ilawgestao.domains.dto.PessoaProfissaoDto;
import com.br.ilawgestao.domains.dto.PessoasFaixaEtariaDto;
import com.br.ilawgestao.domains.service.GraficosPessoasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grafico-pessoas")
public class GraficoPessoasController {

    @Autowired
    private GraficosPessoasService service;

    @GetMapping("/total-pessoas/{empresa}")
    public ResponseEntity<Integer> totalProcessos(@PathVariable long empresa) {
        Integer total = service.totalPessoas(empresa);
        return new ResponseEntity<Integer>(total, HttpStatus.OK);
    }

    @GetMapping("/total-ativas/{empresa}")
    public ResponseEntity<Integer> totalAtivas(@PathVariable long empresa) {
        Integer ativas = service.totalAtivos(empresa);
        return new ResponseEntity<Integer>(ativas, HttpStatus.OK);
    }

    @GetMapping("/total-inativas/{empresa}")
    public ResponseEntity<Integer> totalInativas(@PathVariable long empresa) {
        Integer inativas = service.totalInativo(empresa);
        return new ResponseEntity<Integer>(inativas, HttpStatus.OK);
    }

    @GetMapping("/total-com-processos/{empresa}")
    public ResponseEntity<Integer> totalComProcessos(@PathVariable long empresa) {
        Integer comProcessos = service.totalComProcessos(empresa);
        return new ResponseEntity<Integer>(comProcessos, HttpStatus.OK);
    }

    @GetMapping("/total-sem-processos/{empresa}")
    public ResponseEntity<Integer> totalSemProcessos(@PathVariable long empresa) {
        Integer semProcessos = service.totalSemProcesso(empresa);
        return new ResponseEntity<Integer>(semProcessos, HttpStatus.OK);
    }

    @GetMapping("/total-cadastrados-este-mes/{empresa}")
    public ResponseEntity<Integer> totalCadastradosEsteMes(@PathVariable long empresa) {
        Integer esteMes = service.totalCadastradosEsteMes(empresa);
        return new ResponseEntity<Integer>(esteMes, HttpStatus.OK);
    }

    @GetMapping("/faixa-etaria/{empresa}")
    public ResponseEntity<List<PessoasFaixaEtariaDto>> faixaEtaria(@PathVariable long empresa) {
        List<PessoasFaixaEtariaDto> faixas = service.faixaEtaria(empresa);
        return new ResponseEntity<List<PessoasFaixaEtariaDto>>(faixas, HttpStatus.OK);
    }

    @GetMapping("/profissao/{empresa}")
    public ResponseEntity<List<PessoaProfissaoDto>> profissao(@PathVariable long empresa) {
        List<PessoaProfissaoDto> dtos = service.profissao(empresa);
        return new ResponseEntity<List<PessoaProfissaoDto>>(dtos, HttpStatus.OK);
    }

    @GetMapping("/aniversariantes")
    public ResponseEntity<List<PessoaDTO>> aniversariantes(@RequestParam long mes,
                                                           @RequestParam long idade,
                                                           @RequestParam long empresa) {
        FiltroAniversariantesDto filtro = new FiltroAniversariantesDto();
        filtro.setMes(mes);
        filtro.setIdade(idade);
        filtro.setEmpresa(empresa);
        List<PessoaDTO> dtos = service.aniversariantes(filtro);
        return new ResponseEntity<List<PessoaDTO>>(dtos, HttpStatus.OK);
    }
}
