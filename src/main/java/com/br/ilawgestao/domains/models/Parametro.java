package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parametro")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parametro {
	
	@Column(name = "cdparametro")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@Column(name = "noparametro")
	private String parametro;
	
	@Column(name = "valor")
	private String valor;
	
	@Column(name = "complemento")
	private String complemento;
}
