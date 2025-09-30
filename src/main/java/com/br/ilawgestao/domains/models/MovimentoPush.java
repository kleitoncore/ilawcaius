package com.br.ilawgestao.domains.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "movimento_push")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovimentoPush {
    @Column(name = "cdmovimento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private long codMovimento;

    @Column(name = "nrprocessocnj")
    private String cnj;

    @Column(name = "nrprocesso_antigo")
    private String processo;

    @Column(name = "dsmovimentacao")
    private String dsMovimento;

    @Column(name = "dtmovimentacao")
    private String dtMovimentacao;

    @Column(name = "cdempresa")
    private long codEmpresa;
}
