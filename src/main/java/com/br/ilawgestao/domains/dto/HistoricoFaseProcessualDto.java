package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.Fase;
import com.br.ilawgestao.domains.models.HistoricoFaseProcessual;
import com.br.ilawgestao.domains.models.Processo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoFaseProcessualDto {
     private long codigo;
     private Processo processo;
     private Atividade atividade;
     private Fase fase;

     public static HistoricoFaseProcessualDto build(HistoricoFaseProcessual historico) {
         HistoricoFaseProcessualDto dto = new HistoricoFaseProcessualDto();
         dto.setCodigo(historico.getCodigo());
         dto.setProcesso(historico.getProcesso());
         dto.setAtividade(historico.getAtividade());
         dto.setFase(historico.getFase());
         return dto;
     }
}
