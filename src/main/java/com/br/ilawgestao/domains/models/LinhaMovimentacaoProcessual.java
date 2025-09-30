package com.br.ilawgestao.domains.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "movimento_processual")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinhaMovimentacaoProcessual {
    @Column(name = "cdmovimento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private Long idLinhaMovimentoProcessual;

    @Column(name = "nrprocessocnj")
    private String numeroProcessoCNJ;

    @Column(name = "nrprocesso_antigo")
    private String numeroAntigoProcesso;

    @Column(name = "dsclasse_processual")
    private String dsClasseProcessual;

    @Column(name = "dtdistribuicao")
    private String dhDistribuicao;

    @Column(name = "dtatuacao")
    private String dhAutuacao;

    @Column(name = "dsmovimentacao")
    private String dsMovimentacao;

    @Column(name = "dtmovimentacao")
    private String dhMovimentacao;

    @Column(name = "dserro")
    private String dsErro;

    @Column(name = "cdarquivo_movimento_processual")
    private String idArquivoMovimentoProcessual;
}
