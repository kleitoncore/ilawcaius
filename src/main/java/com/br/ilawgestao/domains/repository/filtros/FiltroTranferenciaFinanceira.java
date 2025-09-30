package com.br.ilawgestao.domains.repository.filtros;

import com.br.ilawgestao.domains.models.ContaFinanceira;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroTranferenciaFinanceira {
    private long codigo;
    private String dataInicial;
    private String dataFinal;
    private long conta;
    private String tipo;
    private long empresa;
}
