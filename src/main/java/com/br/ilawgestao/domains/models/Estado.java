package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name="estado")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estado {
	@Id
	@Column(name="cdestado")
	private Long codigo;
	
	@Column(name="dsestado")
	private String estado;
}
