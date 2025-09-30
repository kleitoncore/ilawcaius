package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "usuario_grupo_trabalho")
@Data
public class UsuarioGrupoTrabalho {
	
	@Id
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdgrupo")
	private GrupoTrabalho grupo;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
}
