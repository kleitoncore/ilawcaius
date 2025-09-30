package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.LinhaMovimentacaoProcessual;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinhamovimentacaoProcessualDTO {
    private Long idLinhaMovimentoProcessual;
    private String numeroProcessoCNJ;
    private String numeroAntigoProcesso;
    private String dsClasseProcessual;
    private String dhDistribuicao;
    private String dhAutuacao;
    private String dsMovimentacao;
    private String dhMovimentacao;
    private String dsErro;
    private String idArquivoMovimentoProcessual;

    public static LinhamovimentacaoProcessualDTO build(LinhaMovimentacaoProcessual linha) {
        LinhamovimentacaoProcessualDTO dto = new LinhamovimentacaoProcessualDTO();
        dto.setIdLinhaMovimentoProcessual(linha.getIdLinhaMovimentoProcessual());
        dto.setNumeroProcessoCNJ(linha.getNumeroProcessoCNJ());
        dto.setNumeroAntigoProcesso(linha.getNumeroAntigoProcesso());
        dto.setDsClasseProcessual(linha.getDsClasseProcessual());
        dto.setDhDistribuicao(linha.getDhDistribuicao());
        dto.setDhAutuacao(linha.getDhAutuacao());
        dto.setDsMovimentacao(linha.getDsMovimentacao());
        dto.setDsErro(linha.getDsErro());
        dto.setIdArquivoMovimentoProcessual(linha.getIdArquivoMovimentoProcessual());
        return dto;
    }
}
