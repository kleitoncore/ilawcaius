package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.StatusAtividade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusAtividadeDto {
    private long codigo;
    private String status;
    private Empresa empresa;
    private String cor;
    private String cor2;
    private String altera;

    public static StatusAtividadeDto build(StatusAtividade status) {
        StatusAtividadeDto dto = new StatusAtividadeDto();
        dto.setCodigo(status.getCodigo());
        dto.setStatus(status.getStatus());
        dto.setEmpresa(status.getEmpresa());
        dto.setCor(status.getCor());
        if(status.getCor() != null) {
            dto.setCor2(status.getCor().replace("#", ""));
        } else {
            dto.setCor2(null);
        }
        dto.setAltera(status.getAltera());
        return dto;
    }
}
