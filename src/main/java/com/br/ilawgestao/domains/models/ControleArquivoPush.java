package com.br.ilawgestao.domains.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "controle_arquivo_push")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ControleArquivoPush {
    @Column(name = "cdcontrole")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private long codigo;

    @Column(name = "noarquivo")
    private String arquivo;

    @Column(name = "dtregistro")
    private String dataRegistro;

    @Column(name = "snprocessado")
    private String status;
}
