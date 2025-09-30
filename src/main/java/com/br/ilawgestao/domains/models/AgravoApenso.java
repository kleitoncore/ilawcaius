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
@Table(name = "agravo_apenso")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgravoApenso {
	
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdprocesso")
	private Processo processo;
	
	@ManyToOne
	@JoinColumn(name = "cdprocesso_principal")
	private Processo processoPrincipal;
	
	@Column(name = "camara")
	private String camara;
	
	@Column(name = "relator")
	private String relator;
	
	@Column(name = "tipo")
	private String tipo;
}
