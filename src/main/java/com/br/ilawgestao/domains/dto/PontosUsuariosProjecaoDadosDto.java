package com.br.ilawgestao.domains.dto;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class PontosUsuariosProjecaoDadosDto {
    private long codigoUsuario;
    private String usuario;
    private BigDecimal pontos;
    private BigDecimal projecao;
    private BigInteger atividadesConcluidas;
    private BigInteger atividadesCriadas;
    private BigDecimal percentual;
    @Transient
    private String percentualTexto;
    @Transient
    private String status;
}
