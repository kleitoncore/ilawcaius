package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.ProcessoExcluido;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessoExcluidoDto {
    private long codigo;
    private ProcessoDto processo;
    private Usuario usuario;
    private String dataExcluido;
    private String status;

    public static ProcessoExcluidoDto build(ProcessoExcluido processo) {
        ProcessoExcluidoDto dto = new ProcessoExcluidoDto();
        dto.setCodigo(processo.getCodigo());
        dto.setProcesso(ProcessoDto.build(processo.getProcesso()));
        dto.setUsuario(processo.getUsuario());
        dto.setDataExcluido(processo.getDataExcluido());
        dto.setStatus(processo.getStatus());
        return dto;
    }

    public static ProcessoExcluidoDto buildConsulta(ProcessoExcluido processo) {
        ProcessoExcluidoDto dto = new ProcessoExcluidoDto();
        dto.setCodigo(processo.getCodigo());
        dto.setProcesso(ProcessoDto.build(processo.getProcesso()));
        dto.setUsuario(processo.getUsuario());
        dto.setDataExcluido(DatasUtil.formatarDataTela(processo.getDataExcluido()));
        dto.setStatus(processo.getStatus());
        return dto;
    }
}
