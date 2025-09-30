package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.MovimentoPush;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimentoPushDto {
     private long codMovimento;
     private String cnj;
     private String processo;
     private String dsMovimento;
     private String dtMovimentacao;
     private long codEmpresa;

     public static MovimentoPushDto build(MovimentoPush movimento) {
         MovimentoPushDto dto = new MovimentoPushDto();
         dto.setCodMovimento(movimento.getCodMovimento());
         dto.setCnj(movimento.getCnj());
         dto.setProcesso(movimento.getProcesso());
         dto.setDsMovimento(movimento.getDsMovimento());
         dto.setCodEmpresa(movimento.getCodEmpresa());
         return dto;
     }
}
