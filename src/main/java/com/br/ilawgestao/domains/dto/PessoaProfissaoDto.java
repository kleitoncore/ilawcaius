package com.br.ilawgestao.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaProfissaoDto {
    private String profissao;
    private BigInteger quantidade;
    @Transient
    private String cor;
}
