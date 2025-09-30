package com.br.ilawgestao.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtividadesPontosDto {
    private String atividade;
    private int pontos;
}
