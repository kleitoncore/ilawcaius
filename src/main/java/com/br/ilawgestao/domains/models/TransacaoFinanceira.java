package com.br.ilawgestao.domains.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "transacao_financeira")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransacaoFinanceira {

    @Column(name = "cdtransacao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private long codigo;

    @ManyToOne
    @JoinColumn(name = "cdlancamento")
    private Lancamento lancamento;

    @ManyToOne
    @JoinColumn(name = "cdconta_financeira")
    private ContaFinanceira contaFinanceira;

    @Column(name = "dtregistro")
    private String dtRegistro;

    @ManyToOne
    @JoinColumn(name = "cdusuario")
    private Usuario usuario;

    @Column(name = "observacao")
    private String observacao;
}
