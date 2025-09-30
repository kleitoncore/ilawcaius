package com.br.ilawgestao.domains.dto;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "movimento_push_fim")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovimentoPushFim {
    @Column(name = "cdmovimento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private long codigo;

    @Column(name = "cdprocesso")
    private long codigoProcesso;

    @Column(name = "nrpasta")
    private String pasta;

    @Column(name = "nrcnj")
    private String nrCnj;

    @Column(name = "autor")
    private String autor;

    @Column(name = "reu")
    private String reu;

    @Column(name = "nosituacao")
    private String statusProcessual;

    @Column(name = "dtmovimentacao")
    private String dtMovimentacao;

    @Column(name = "dsmovimentacao")
    private String movimentacao;

    @Column(name = "cdgrupo")
    private long codigoGrupo;

    @Column(name = "nogrupo")
    private String nomeGrupo;

    @Column(name = "cdempresa")
    private long codigoEmpresa;

    @Column(name = "snenviou")
    private String snEnviou;

    @Column(name = "dtcarregamento")
    private String dataCarregamento;
}
