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
@Table(name = "atendimento")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Atendimento {
	
	@Column(name = "cdatendimento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdpessoa")
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name = "cdprocesso")
	private Processo processo;
	
	@ManyToOne
	@JoinColumn(name = "cdgrupo")
	private GrupoTrabalho grupo;
	
	@Column(name = "dsassunto")
	private String assunto;
	
	@Column(name = "dtatendimento")
	private String dtAtendimento;
	
	@ManyToOne
	@JoinColumn(name = "cdempresa")
	private Empresa empresa;
}
