package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.ControleArquivoPush;
import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControleArquivoPushDTO {
    private long codigo;
    private String arquivo;
    private String dataRegistro;
    private String status;

    public static ControleArquivoPushDTO build(ControleArquivoPush push) {
        ControleArquivoPushDTO dto = new ControleArquivoPushDTO();
        dto.setCodigo(push.getCodigo());
        dto.setArquivo(push.getArquivo());
        dto.setDataRegistro(DatasUtil.formatarDataTela(push.getDataRegistro()));
        dto.setStatus(push.getStatus());
        return dto;
    }
}
