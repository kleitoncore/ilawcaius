package com.br.ilawgestao.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaSexoDto {
    private String sexo;
    private BigInteger quantidade;
}
