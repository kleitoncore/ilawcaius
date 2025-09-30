package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "agenda")
@AllArgsConstructor
@NoArgsConstructor
public class Agenda {
	
	@Column(name = "cdagenda")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@Column(name = "cdatividade")
	private long codigoAtividade;
	
	@Column(name = "titulo")
	private String titulo;
	
	@Column(name = "cdusuario")
	private long codigoResponsavel;
	
	@Column(name = "cdgrupo")
	private long codigoGrupo;
	
	@Column(name = "cdstatus")
	private long status;
	
	@Column(name = "responsavel")
	private String responsavel;
	
	@Column(name = "processo")
	private String processo;
	
	@Column(name = "dtcompromisso")
	private String dataCompromisso;
	
	@Column(name = "tpatividade")
	private String tpAtividade;
	
	@Column(name = "cdfase")
	private long fase;
	
	@Transient
	private String hora;

	@Column(name = "dtatividade_lida")
	private String dataLida;
}
