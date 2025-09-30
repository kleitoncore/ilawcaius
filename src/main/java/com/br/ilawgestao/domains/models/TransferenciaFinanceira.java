package com.br.ilawgestao.domains.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "transferencia_financeira")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferenciaFinanceira {

    @Column(name = "cdtransferencia")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private long codigo;

    @ManyToOne
    @JoinColumn(name = "cdorigem")
    private ContaFinanceira origem;

    @ManyToOne
    @JoinColumn(name = "cddestino")
    private ContaFinanceira destino;

    @Column(name = "valor")
    private double valor;

    @Column(name = "dtregistro")
    private String dtRegistro;

    @ManyToOne
    @JoinColumn(name = "cdusuario")
    private Usuario usuario;

    @Column(name = "observacao")
    private String observacao;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @Column(name = "tipo_file")
    private String tipoFile;

    @Column(name = "file_name")
    private String fileName;
}
