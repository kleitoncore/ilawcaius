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
@Table(name = "motivo_resultado")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MotivoResultado {
	
	@Column(name = "cdmotivo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@Column(name = "nomotivo")
	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "cdempresa")
	private Empresa empresa;
	
}
