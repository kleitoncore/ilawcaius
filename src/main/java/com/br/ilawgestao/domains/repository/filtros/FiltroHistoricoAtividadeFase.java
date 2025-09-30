package com.br.ilawgestao.domains.repository.filtros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroHistoricoAtividadeFase {
    private long processo;
    private String fase;
    private String dataLimiteInicial;
    private String dataLimiteFinal;
    private String dataFatalInicial;
    private String dataFatalFinal;
    private String status;
    private String importante;
    private String urgente;
    private long ordem;
    private long classificacao;
}
