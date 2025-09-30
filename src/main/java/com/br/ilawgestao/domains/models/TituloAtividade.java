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
@Table(name = "titulo_atividade")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TituloAtividade {
	
	@Column(name = "cdtitulo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@Column(name = "dstitulo")
	private String titulo;
	
	@JoinColumn(name = "cdempresa")
	@ManyToOne
	private Empresa empresa;
	
	@Column(name = "pontos")
	private long pontos;

	@ManyToOne
	@JoinColumn(name = "cdfase")
	private Fase fase;

	@Column(name = "status")
	private String status;
}
