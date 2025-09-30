package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.HistoricoPessoa;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoPessoaDto {
    private long codigo;
    private Pessoa pessoa;
    private String dsHistorico;
    private String dataHistorico;
    private String tipo;
    private Usuario usuario;

    public static HistoricoPessoaDto build(HistoricoPessoa historico) {
        HistoricoPessoaDto dto = new HistoricoPessoaDto();
        dto.setPessoa(historico.getPessoa());
        dto.setDsHistorico(historico.getDsHistorico());
        dto.setDataHistorico(historico.getDataHistorico());
        dto.setTipo(historico.getTipo());
        dto.setUsuario(historico.getUsuario());
        return dto;
    }

    public static HistoricoPessoaDto buildConsulta(HistoricoPessoa historico) {
        HistoricoPessoaDto dto = new HistoricoPessoaDto();
        dto.setCodigo(historico.getCodigo());
        dto.setPessoa(historico.getPessoa());
        dto.setDsHistorico(historico.getDsHistorico());
        dto.setDataHistorico(DatasUtil.formatarDataTela(historico.getDataHistorico()));
        if (historico.getTipo().equals("M")) {
            dto.setTipo("Inclusão Manual");
        } else {
            dto.setTipo("Inclusão Automática");
        }
        dto.setUsuario(historico.getUsuario());
        return dto;
    }
}
