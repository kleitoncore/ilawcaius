package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;

@Table(name="cidade")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cidade {
	@Id
	@Column(name="cdcidade")
	private Long codigo;
	
	@Column(name="nocidade")
	private String nome;
	
	@ManyToOne
	@JoinColumn(name="cdestado")
	private Estado estado;
}
