package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.awt.*;
import java.util.Date;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoFaseProcessualViewDto {
    private long codigo;
    private String titulo;
    private String tipo;
    private String fase;
    private String status;
    private Date dataLimite;
    private Date dataFatal;
    private String subGrupo;
    private String responsaveis;
    @Transient
    private String dataLimiteFormat;
    @Transient
    private String dataFatalFormat;
    private String cor;
    private String corStatus;
    private String importante;
    private String urgente;

    public static HistoricoFaseProcessualViewDto build(HistoricoFaseProcessualViewDto dto) {
        HistoricoFaseProcessualViewDto dtot = new HistoricoFaseProcessualViewDto();
        dtot.setCodigo(dto.getCodigo());
        dtot.setTitulo(dto.getTitulo());
        dtot.setSubGrupo(dto.getSubGrupo());
        dtot.setFase(dto.getFase());
        dtot.setStatus(dto.getStatus());
        if(dto.getTipo().equals("A")) {
            dtot.setDataLimiteFormat(DatasUtil.formatarDataHoraTela(dto.getDataLimite()));
        } else {
            dtot.setDataLimiteFormat(DatasUtil.formatarDataTela(dto.getDataLimite()));
        }

        if(dto.getDataFatal() != null) {
            dtot.setDataFatalFormat(DatasUtil.formatarDataTela(dto.getDataFatal()));
        } else {
            dtot.setDataFatal(null);
        }
        dtot.setImportante(dto.getImportante());
        dtot.setUrgente(dto.getUrgente());
        dtot.setResponsaveis(dto.getResponsaveis());
        dtot.setSubGrupo(dto.getSubGrupo());
        dtot.setCor(dto.getCor());
        dtot.setCorStatus(dto.getCorStatus());
        return dtot;
    }
}
