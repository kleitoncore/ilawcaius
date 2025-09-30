package com.br.ilawgestao.domains.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "configuracao_processo_parado")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfiguracaoProcessoParado {
    @Column(name = "cdconfiguracao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private long codigo;

    @ManyToOne
    @JoinColumn(name = "cdempresa")
    private Empresa empresa;

    @Column(name = "dias")
    private long dias;
}
