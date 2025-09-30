package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.ConfiguracaoProcessoParado;
import com.br.ilawgestao.domains.models.Empresa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracaoProcessoParadoDto {
    private long codigo;
    private Empresa empresa;
    private long dias;

    public static ConfiguracaoProcessoParadoDto build(ConfiguracaoProcessoParado conf) {
        ConfiguracaoProcessoParadoDto dto = new ConfiguracaoProcessoParadoDto();
        dto.setCodigo(conf.getCodigo());
        dto.setEmpresa(conf.getEmpresa());
        dto.setDias(conf.getDias());
        return dto;
    }
}
