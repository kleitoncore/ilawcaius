package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.ContaFinanceira;
import com.br.ilawgestao.domains.models.TransferenciaFinanceira;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaFinanceiraDto {
    private long codigo;
    private ContaFinanceira origem;
    private ContaFinanceira destino;
    private double valor;
    private String valorFormat;
    private String dtRegistro;
    private Usuario usuario;
    private String observacao;
    private byte[] file;
    private String tipoFile;
    private String fileName;

    public static TransferenciaFinanceiraDto build(TransferenciaFinanceira trans) {
        TransferenciaFinanceiraDto dto = new TransferenciaFinanceiraDto();
        dto.setCodigo(trans.getCodigo());
        dto.setOrigem(trans.getOrigem());
        dto.setDestino(trans.getDestino());
        dto.setValor(trans.getValor());
        dto.setDtRegistro(trans.getDtRegistro());
        dto.setUsuario(trans.getUsuario());
        dto.setObservacao(trans.getObservacao());
        dto.setFile(trans.getFile());
        dto.setTipoFile(trans.getTipoFile());
        dto.setFileName(trans.getFileName());
        return dto;
    }

    public static TransferenciaFinanceiraDto buildConsulta(TransferenciaFinanceira trans) {
        TransferenciaFinanceiraDto dto = new TransferenciaFinanceiraDto();
        dto.setCodigo(trans.getCodigo());
        dto.setOrigem(trans.getOrigem());
        dto.setDestino(trans.getDestino());
        Locale ptBr = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
        BigDecimal valor = new BigDecimal(trans.getValor());
        String formato = nf.format(valor);
        dto.setValorFormat(formato);
        dto.setValor(trans.getValor());
        dto.setDtRegistro(DatasUtil.formatarDataTela(trans.getDtRegistro()));
        dto.setUsuario(trans.getUsuario());
        dto.setObservacao(trans.getObservacao());
        dto.setFile(trans.getFile());
        dto.setTipoFile(trans.getTipoFile());
        dto.setFileName(trans.getFileName());
        return dto;
    }
}
