package com.br.ilawgestao.domains.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class PontuacaoProjecaoDatasDto {
    private int dia;
    private BigDecimal pontos;
}
