package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "historico_pessoa")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoPessoa {
	
	@Id
	@Column(name = "cdhistorico")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdpessoa")
	private Pessoa pessoa;
	
	@Column(name = "dshistorico")
	private String dsHistorico;
	
	@Column(name = "dthistorico")
	private String dataHistorico;
	
	@Column(name = "tphistorico")
	private String tipo;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
}
