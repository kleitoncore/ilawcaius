package com.br.ilawgestao.domains.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "agencia")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Agencia {

    @Column(name = "cdagencia")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private long codigo;

    @Column(name = "cdbanco")
    private long codigoBanco;

    @Column(name = "nragencia")
    private String numeroAgencia;

    @Column(name = "nrconta")
    private String numeroConta;

    @Column(name = "tpconta")
    private String tipoConta;

    @ManyToOne
    @JoinColumn(name = "cdconta")
    private ContaFinanceira contaFinanceira;
}
