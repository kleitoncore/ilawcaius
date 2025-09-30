package com.br.ilawgestao.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtividadesGeralHomeDto {
    private long codigo;
    private String status;
    private String atividade;
    private String fase;
    private String subGrupo;
    private String grupo;
    private String dataLimite;
    private String prazoFatal;
    private String dataConcluido;
    private BigInteger codigoProcesso;
    private String processo;
    private String partes;
    private String corStatus;
    private String corfase;
    private String importante;
    private String urgente;

    public static AtividadesGeralHomeDto build(AtividadesGeralHomeDto ativ) {
        AtividadesGeralHomeDto dto = new AtividadesGeralHomeDto();
        dto.setCodigo(ativ.getCodigo());
        dto.setStatus(ativ.getStatus());
        dto.setImportante(ativ.getImportante());
        dto.setUrgente(ativ.getUrgente());
        dto.setAtividade(ativ.getAtividade());
        dto.setFase(ativ.getFase());
        dto.setSubGrupo(ativ.getSubGrupo());
        dto.setGrupo(ativ.getGrupo());
        dto.setDataLimite(ativ.getDataLimite());
        dto.setPrazoFatal(ativ.getPrazoFatal());
        dto.setDataConcluido(ativ.getDataConcluido());
        String processoMascarado = "";
        if(ativ.getProcesso() != null) {
            if (ativ.getProcesso().length() == 20) {
                processoMascarado = ativ.getProcesso().substring(0, 7);
                processoMascarado = processoMascarado + "-" + ativ.getProcesso().substring(7, 9);
                processoMascarado = processoMascarado + "." + ativ.getProcesso().substring(9, 13);
                processoMascarado = processoMascarado + "." + ativ.getProcesso().substring(13, 14);
                processoMascarado = processoMascarado + "." + ativ.getProcesso().substring(14, 16);
                processoMascarado = processoMascarado + "." + ativ.getProcesso().substring(16, 20);
            } else {
                processoMascarado = ativ.getProcesso();
            }
        } else {
            processoMascarado = "";
        }
        dto.setProcesso(processoMascarado);
        dto.setCodigoProcesso(ativ.getCodigoProcesso());
        dto.setPartes(ativ.getPartes());
        dto.setCorStatus(ativ.getCorStatus());
        dto.setCorfase(ativ.getCorfase());
        return dto;
    }
}
