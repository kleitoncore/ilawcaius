package com.br.ilawgestao.domains.tipb;

import lombok.Data;

@Data
public class LinhaMovimentacaoProcessual {
    private Integer idLinhaMovimentoProcessual;
    private String numeroProcessoCNJ;
    private String numeroAntigoProcesso;
    private String dsClasseProcessual;
    private String dhDistribuicao;
    private String dhAutuacao;
    private String dsMovimentacao;
    private String dhMovimentacao;
    private String dsErro;
    private String idArquivoMovimentoProcessual;

    public LinhaMovimentacaoProcessual() {
        this.idLinhaMovimentoProcessual = 0;
        this.numeroProcessoCNJ = "";
        this.numeroAntigoProcesso = "";
        this.dsClasseProcessual = "";
        this.dhDistribuicao = "";
        this.dhAutuacao = "";
        this.dsMovimentacao = "";
        this.dhMovimentacao = "";
        this.dsErro = "";
        this.idArquivoMovimentoProcessual = "";
    }

    public LinhaMovimentacaoProcessual(Integer idLinhaMovimentoProcessual, String numeroProcessoCNJ,
                                       String numeroAntigoProcesso, String dsClasseProcessual, String dhDistribuicao, String dhAutuacao,
                                       String dsMovimentacao, String dhMovimentacao, String dsErro, String idArquivo) {
        super();
        this.idLinhaMovimentoProcessual = idLinhaMovimentoProcessual;
        this.numeroProcessoCNJ = numeroProcessoCNJ;
        this.numeroAntigoProcesso = numeroAntigoProcesso;
        this.dsClasseProcessual = dsClasseProcessual;
        this.dhDistribuicao = dhDistribuicao;
        this.dhAutuacao = dhAutuacao;
        this.dsMovimentacao = dsMovimentacao;
        this.dhMovimentacao = dhMovimentacao;
        this.dsErro = dsErro;
        this.idArquivoMovimentoProcessual = idArquivo;
    }
}
