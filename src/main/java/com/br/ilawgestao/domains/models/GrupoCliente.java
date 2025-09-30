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
@Table(name = "grupo_cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoCliente {
	
	@Id
	@Column(name = "cdgrupo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@Column(name = "nogrupo")
	private String nome;
	
	@Column(name = "dsgrupo")
	private String descricao;
	
	@ManyToOne
	@JoinColumn(name = "cdempresa")
	private Empresa empresa;
	
}
