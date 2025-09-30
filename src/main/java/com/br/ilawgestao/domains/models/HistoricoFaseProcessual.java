package com.br.ilawgestao.domains.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "historico_fase_processual")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoricoFaseProcessual {

    @Column(name = "cdhistorico")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private long codigo;

    @ManyToOne
    @JoinColumn(name = "cdprocesso")
    private Processo processo;

    @ManyToOne
    @JoinColumn(name = "cdatividade")
    private Atividade atividade;

    @ManyToOne
    @JoinColumn(name = "cdfase")
    private Fase fase;
}
