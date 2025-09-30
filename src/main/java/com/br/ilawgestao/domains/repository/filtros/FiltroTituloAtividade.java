package com.br.ilawgestao.domains.repository.filtros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroTituloAtividade {
    private long fase;
    private String titulo;
    private String status;
    private long classificacao;
    private long ordenacao;
    private long empresa;
}
