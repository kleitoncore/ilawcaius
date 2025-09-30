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
@Table(name = "custas_processo")
@Data
public class CustasProcesso {
	
	@Id
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdprocesso")
	private Processo processo;
	
	@ManyToOne
	@JoinColumn(name = "cdcustas")
	private TipoCusta custas;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "vlcustas")
	private double vlCustas;
	
	@Column(name = "dtpagamento")
	private String dtPagamento;
}
