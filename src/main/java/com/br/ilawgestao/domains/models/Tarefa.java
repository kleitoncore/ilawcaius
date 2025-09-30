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
@Table(name = "tarefa")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tarefa {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	@Column(name = "cdtarefa")
	private long codigo;
	
	@Column(name = "dstitulo")
	private String titulo;
	
	@Column(name = "prazo")
	private long prazo;
	
	@Column(name = "dtregistro")
	private String dataRegistro;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "cdempresa")
	private Empresa empresa;
	
	@Column(name = "snativo")
	private String snAtivo;
	
	@Column(name = "pontuacao")
	private long pontuacao;
	
	@Column(name = "tempo")
	private String tempo;
}
