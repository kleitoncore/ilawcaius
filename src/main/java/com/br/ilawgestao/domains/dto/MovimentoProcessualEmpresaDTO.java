package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.HistoricoProcesso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimentoProcessualEmpresaDTO {
    private long codigoProcesso;
    private String nrPasta;
    private String nrCnj;
    private String autor;
    private String reu;
    private String statusProcessual;
    private Date dtMovimentacao;
    private String movimentacao;
    private long codigoGrupo;
    private String nomeGrupo;
    private long codEmpresa;
    private String snHistorico;
}
