package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "grupo_cliente_usuario")
@Data
public class GrupoClienteUsuario {
	
	@Id
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdgrupo")
	@OrderBy(value = "nome")
	private GrupoCliente grupo;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	@OrderBy(value = "nome")
	private Usuario usuario;
}
