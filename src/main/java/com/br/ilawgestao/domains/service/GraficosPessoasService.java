package com.br.ilawgestao.domains.service;

import com.br.ilawgestao.domains.dto.FiltroAniversariantesDto;
import com.br.ilawgestao.domains.dto.PessoaDTO;
import com.br.ilawgestao.domains.dto.PessoaProfissaoDto;
import com.br.ilawgestao.domains.dto.PessoasFaixaEtariaDto;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GraficosPessoasService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public int totalPessoas(long empresa) {
        return pessoaRepository.totalPessoas(empresa);
    }

    public int totalAtivos(long empresa) {
        return pessoaRepository.totalPessoasAtivas(empresa);
    }

    public int totalInativo(long empresa) {
        return pessoaRepository.totalInativos(empresa);
    }

    public int totalComProcessos(long empresa) {
        return pessoaRepository.totalComProcessos(empresa);
    }

    public int totalSemProcesso(long empresa) {
        return pessoaRepository.totalSemProcessos(empresa);
    }

    public List<PessoasFaixaEtariaDto> faixaEtaria(long empresa) {
        List<PessoasFaixaEtariaDto> faixas = pessoaRepository.faixaEtaria(empresa);
        List<PessoasFaixaEtariaDto> dtos = new ArrayList<>();
        for(PessoasFaixaEtariaDto faixa : faixas) {
            PessoasFaixaEtariaDto dto = new PessoasFaixaEtariaDto();
            dto.setFaixa(faixa.getFaixa());
            dto.setQuantidade(faixa.getQuantidade());
            dto.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
            dtos.add(dto);
        }
        return dtos;
    }

    public List<PessoaProfissaoDto> profissao(long empresa) {
        List<PessoaProfissaoDto> profissoes = pessoaRepository.profissao(empresa);
        List<PessoaProfissaoDto> dtos =new ArrayList<>();
        for(PessoaProfissaoDto prof : profissoes) {
            PessoaProfissaoDto dto = new PessoaProfissaoDto();
            dto.setProfissao(prof.getProfissao());
            dto.setQuantidade(prof.getQuantidade());
            dto.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
            dtos.add(dto);
        }
        return dtos;
    }

    public int totalCadastradosEsteMes(long empresa) {
        return pessoaRepository.totalCadastradosEsteMes(empresa);
    }

    public List<PessoaDTO> aniversariantes(FiltroAniversariantesDto filtro) {
        List<Pessoa> pessoas = pessoaRepository.aniversariantes(filtro);
        List<PessoaDTO> dtos = new ArrayList<>();
        if(pessoas != null && pessoas.size() > 0) {
            for(Pessoa pessoa : pessoas) {
                dtos.add(PessoaDTO.buildAniversariantes(pessoa));
            }
        }

        return dtos;
    }

    private Color gerarCorAleatoriamente(){
        Random randColor = new Random();
        int r = randColor.nextInt(256);
        int g = randColor.nextInt(256);
        int b = randColor.nextInt(256);
        return new Color(r, g, b);
    }

    private String gerarCorHexadecimal(Color color){
        return '#'+
                this.tratarHexString(Integer.toHexString(color.getRed()))+
                this.tratarHexString(Integer.toHexString(color.getGreen()))+
                this.tratarHexString(Integer.toHexString(color.getBlue()));
    }

    private String tratarHexString(String hexString){
        String hex = null;
        if(hexString.length() == 1){
            hex = '0'+hexString;
        }else{
            hex = hexString;
        }
        return hex;
    }
}
