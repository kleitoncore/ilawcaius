package com.br.ilawgestao.domains.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "conta_financeira")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContaFinanceira {

    @Column(name = "cdconta")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private long codigo;

    @Column(name = "noconta")
    private String nome;

    @Column(name = "saldo")
    private double saldo;

    @Column(name = "dtregistro")
    private String dtRegistro;

    @ManyToOne
    @JoinColumn(name = "cdempresa")
    private Empresa empresa;

    @Column(name = "status")
    private String status;

    @Column(name = "tpconta")
    private String tipoConta;

    @Column(name = "conta_default")
    private String contaDefault;
}
