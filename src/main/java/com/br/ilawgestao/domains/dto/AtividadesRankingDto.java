package com.br.ilawgestao.domains.dto;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigInteger;

@Data
public class AtividadesRankingDto {
    private int codigo;
    private String atividade;
    private String fase;
    private int pontuacao;
    private BigInteger atividades;
    @Transient
    private String cor;
}
