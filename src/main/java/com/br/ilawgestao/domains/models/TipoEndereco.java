package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "tipo_endereco")
@Data
public class TipoEndereco {
	
	@Id
	@Column(name = "cdtipo_endereco")
	private String codigo;
	
	@Column(name = "notipo")
	private String nome;
}
