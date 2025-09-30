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
@Table(name = "atendimento_historico")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtendimentoHistorico {
	
	@Column(name = "cdhistorico")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdatendimento")
	private Atendimento atendimento;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@Column(name = "dshistorico")
	private String historico;
	
	@Column(name = "dtatendimento")
	private String dtAtendimento;
}
