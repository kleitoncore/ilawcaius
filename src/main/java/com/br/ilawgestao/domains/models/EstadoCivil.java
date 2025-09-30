package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "estado_civil")
@Data
public class EstadoCivil {
	
	@Id
	@Column(name = "cdestado_civil")
	private String codigo;
	
	@Column(name = "noestado")
	private String nome;
}
