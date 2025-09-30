package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.service.PontuacaoProjecaoDto;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
public class GraficoPontuacaoTotalDto {
    private BigDecimal pontosTotal;
    private BigDecimal pontos;
    @Transient
    private String mensagem1;
    @Transient
    private String mensagem2;
    @Transient
    private String mensagem3;
    @Transient
    private double percentual;
    @Transient
    private List<PontuacaoProjecaoDto> pontuacaoProjecao;
    @Transient
    private List<PontuacaoUsuarioDto> pontuacaoUsuarios;
    @Transient
    private List<PontosAcumuladosUsuariosDto> acumulado;
}
