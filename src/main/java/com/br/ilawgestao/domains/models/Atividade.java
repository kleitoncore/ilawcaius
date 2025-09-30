package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "atividade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Atividade {

	@Id
	@Column(name = "cdatividade")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;

	@Column(name = "dstitulo")
	private String titulo;

	@ManyToOne
	@JoinColumn(name = "cdprocesso")
	private Processo processo;

	@ManyToOne
	@JoinColumn(name = "cdsubgrupo")
	private GrupoTrabalho subGrupo;
	
	@Column(name = "dtlimite")
	private String dtLimite;
	
	@Column(name = "dtfatal")
	private String dtFatal;
	
	@ManyToOne
	@JoinColumn(name = "cdstatus")
	private StatusAtividade status;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "tpatividade")
	private String tipo;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@Column(name = "dtregistro")
	private String dtRegistro;
	
	@Column(name = "dtconcluido")
	private String dtConcluido;
	
	@Column(name = "snimportante")
	private String importante;
	
	@Column(name = "snurgente")
	private String urgente;
	
	@Column(name = "snprivado")
	private String privado;
		
	@Transient
	private String statusCalculado;
	
	@Transient
	private String grupoPai;
	
	@Transient
	private String hora;
	
	@Transient
	private String responsaveis;

	@ManyToOne
	@JoinColumn(name = "cdpessoa")
	private Pessoa pessoa;

	@Column(name = "dtlida")
	private String dataLida;

	@Transient
	private long pontuacao;

	@ManyToOne
	@JoinColumn(name = "cdtitulo_atividade")
	private TituloAtividade tituloAtividade;

	@ManyToOne
	@JoinColumn(name = "cdlancamento")
	private Lancamento lancamentoFinanceiro;
}
