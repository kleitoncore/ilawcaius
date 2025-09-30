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

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name="objeto_acao")
public class ObjetoAcao {
	
	@Id
	@Column(name="cdobjeto")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@Column(name="noobjeto", nullable = false)
	private String nome;
	
	@ManyToOne
	@JoinColumn(name="cdempresa", nullable = false)
	private Empresa empresa;
	
	@Column(name = "cdobjeto_pai")
	private long objetoPai;
}
