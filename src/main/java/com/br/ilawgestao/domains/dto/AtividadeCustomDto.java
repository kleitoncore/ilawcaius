package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtividadeCustomDto {
    private long codigo;
    private String titulo;
    private BigInteger pontos;
    private String descricao;
    private String dataCriacao;
    private String dataLimite;
    private String prazoFatal;
    private String dataLida;
    private long statusCodigo;
    @Transient
    private StatusAtividade statusEntty;
    private String tipo;
    private String hora;
    private Integer codigoProcesso;
    private String processo;
    private String fase;
    private String partes;
    private Integer codigoPessoa;
    private String nomePessoa;
    @Transient
    private Pessoa pessoa;
    private long codigoGrupo;
    private String grupo;
    private long codigoSubGrupo;
    private String subGrupo;
    @Transient
    private List<Usuario> responsaveisAtividade;
    @Transient
    private List<Usuario> interessadosAtividade;
    @Transient
    private List<Usuario> usuariosGrupo;
    @Transient
    private List<StatusAtividade> status;
    private String importante;
    private String urgente;
    private String privado;
    private String usuarioCriou;
    @Transient
    private List<ArquivoAtividade> arquivos;
    @Transient
    private List<HistoricoAtividade> historico;
    private Integer lancamentoFinanceiro;
}
