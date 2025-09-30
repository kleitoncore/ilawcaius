package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.ContaFinanceira;
import com.br.ilawgestao.domains.models.Lancamento;
import com.br.ilawgestao.domains.models.TransacaoFinanceira;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoFinanceiraDto {
    private long codigo;
    private Lancamento lancamento;
    private ContaFinanceira contaFinanceira;
    private String dtRegistro;
    private Usuario usuario;
    private String observacao;

    public static TransacaoFinanceiraDto build(TransacaoFinanceira trans) {
        TransacaoFinanceiraDto dto = new TransacaoFinanceiraDto();
        dto.setCodigo(trans.getCodigo());
        dto.setLancamento(trans.getLancamento());
        dto.setContaFinanceira(trans.getContaFinanceira());
        dto.setDtRegistro(trans.getDtRegistro());
        dto.setUsuario(trans.getUsuario());
        dto.setObservacao(trans.getObservacao());
        return dto;
    }

    public static TransacaoFinanceiraDto buildConsulta(TransacaoFinanceira trans) {
        TransacaoFinanceiraDto dto = new TransacaoFinanceiraDto();
        dto.setCodigo(trans.getCodigo());
        dto.setLancamento(trans.getLancamento());
        dto.setContaFinanceira(trans.getContaFinanceira());
        dto.setDtRegistro(DatasUtil.formatarDataTela(trans.getDtRegistro()));
        dto.setUsuario(trans.getUsuario());
        dto.setObservacao(trans.getObservacao());
        return dto;
    }
}
