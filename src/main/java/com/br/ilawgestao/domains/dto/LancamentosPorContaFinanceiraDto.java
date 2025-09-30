package com.br.ilawgestao.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LancamentosPorContaFinanceiraDto {
    private String conta;
    private double valor;
    @Transient
    private String cor;
}
