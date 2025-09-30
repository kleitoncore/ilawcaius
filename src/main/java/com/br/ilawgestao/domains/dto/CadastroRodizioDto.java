package com.br.ilawgestao.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroRodizioDto {
    private TarefaDto atividade;
    private GrupoTarefaDto grupoAtividade;
    private List<RodizioUsuarioAtividadeDto> rodizio;
}
