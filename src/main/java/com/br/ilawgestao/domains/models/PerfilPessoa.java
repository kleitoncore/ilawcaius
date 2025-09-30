package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "perfil_pessoa")
@Data
public class PerfilPessoa {
	
	@Id
	@Column(name = "cdperfil")
	private String codigo;
	
	@Column(name = "noperfil")
	private String nome;
}
