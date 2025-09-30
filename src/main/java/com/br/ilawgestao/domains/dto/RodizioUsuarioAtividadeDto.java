package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.RodizioUsuarioAtividade;
import com.br.ilawgestao.domains.models.GrupoTarefa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Tarefa;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RodizioUsuarioAtividadeDto {
    private long codigo;
    private GrupoTarefa grupoAtividade;
    private Tarefa atividade;
    private GrupoTrabalho grupoTrabalho;
    private Usuario usuarioAtividade;
    private long posicao;
    private String dataAlteracao;

    public static RodizioUsuarioAtividadeDto build(RodizioUsuarioAtividade rodizio) {
        RodizioUsuarioAtividadeDto dto = new RodizioUsuarioAtividadeDto();
        dto.setCodigo(rodizio.getCodigo());
        dto.setGrupoAtividade(rodizio.getGrupoAtividade());
        dto.setAtividade(rodizio.getAtividade());
        dto.setGrupoTrabalho(rodizio.getGrupoTrabalho());
        dto.setUsuarioAtividade(rodizio.getUsuarioAtividade());
        dto.setPosicao(rodizio.getPosicao());
        dto.setDataAlteracao(rodizio.getDataAlteracao());
        return dto;
    }

    public static RodizioUsuarioAtividadeDto buildConsulta(RodizioUsuarioAtividade rodizio) {
        RodizioUsuarioAtividadeDto dto = new RodizioUsuarioAtividadeDto();
        dto.setCodigo(rodizio.getCodigo());
        dto.setGrupoAtividade(rodizio.getGrupoAtividade());
        dto.setAtividade(rodizio.getAtividade());
        dto.setGrupoTrabalho(rodizio.getGrupoTrabalho());
        dto.setUsuarioAtividade(rodizio.getUsuarioAtividade());
        dto.setPosicao(rodizio.getPosicao());
        dto.setDataAlteracao(DatasUtil.formatarDataTela(rodizio.getDataAlteracao()));
        return dto;
    }
}