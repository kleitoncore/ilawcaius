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

@Table(name="grupo_trabalho")
@Entity
@Data
public class GrupoTrabalho {
	
	@Id
	@Column(name="cdgrupo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@Column(name="nogrupo")
	private String nome;
	
	@Column(name="dsgrupo")
	private String descricao;
	
	@ManyToOne
	@JoinColumn(name="cdempresa")
	private Empresa empresa;
	
	@Column(name="cdgrupo_pai" )
	private long grupoPai;

	@Column(name = "snativo")
	private String status;

}
