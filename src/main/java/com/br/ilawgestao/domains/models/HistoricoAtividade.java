package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historico_atividade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoAtividade {
	
	@Column(name = "cdhistorico")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdatividade")
	private Atividade atividade;
	
	@Column(name = "dshistorico")
	private String dsHistorico;
	
	@Column(name = "dthistorico")
	private String dtHistorico;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@Column(name = "tphistorico")
	private String tpHistorico;
}
