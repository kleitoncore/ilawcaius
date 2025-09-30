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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lancamento")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lancamento {
	
	@Id
	@Column(name = "cdlancamento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@Column(name = "dtlancamento")
	private String dtLancamento;
	
	@Column(name = "dtvencimento")
	private String dtVencimento;
	
	@Column(name = "vllancamento")
	private double vlLancamento;
	
	@Column(name = "vlpago")
	private double vlPago;
	
	@Column(name = "dtpago")
	private String dtPago;
	
	@ManyToOne
	@JoinColumn(name = "cdtipo")
	private TipoDespesaReceita tipo;
	
	@Column(name = "dslancamento")
	private String dsLancamento;
	
	@Column(name = "observacao")
	private String observacao;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "cdprocesso")
	private Processo processo;
	
	@Transient
	private String status;
	
	@Column(name = "situacao")
	private String situacao;
	
	@Transient
	private String categoria;
	
	@Transient
	private String valorLancamentoFormat;
	
	@Transient
	private String valorPagamentoFormat;
}
