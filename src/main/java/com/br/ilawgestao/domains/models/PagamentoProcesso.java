package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "")
@Data
public class PagamentoProcesso {
	
	@Id
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdprocesso")
	private Processo processo;
	
	@ManyToOne
	@JoinColumn(name = "cdpagamento")
	private TipoPagamento pagamento;
	
	@Column(name = "vlpagamento")
	private double vlPagamento;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "dtpagamento")
	private String dtPagamento;
	
}
