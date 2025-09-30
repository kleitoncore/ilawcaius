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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grupo_tarefa")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrupoTarefa {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	@Column(name = "cdgrupo")
	private long codigo;
	
	@Column(name = "nogrupo")
	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "cdempresa")
	private Empresa empresa;
	
	@Column(name = "dtregistro")
	private String dataRegistro;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@Column(name = "snativo")
	private String snAtivo;
}
