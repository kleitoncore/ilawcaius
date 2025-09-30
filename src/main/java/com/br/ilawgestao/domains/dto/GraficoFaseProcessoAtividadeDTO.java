package com.br.ilawgestao.domains.dto;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigInteger;

@Data
public class GraficoFaseProcessoAtividadeDTO {
    private String fase;
    private BigInteger atividades;
    @Transient
    private String cor;
}
