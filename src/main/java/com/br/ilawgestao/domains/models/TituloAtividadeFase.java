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
@Table(name = "titulo_atividade_fase")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TituloAtividadeFase {
	
	@Id
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@JoinColumn(name = "cdfase")
	@ManyToOne
	private FaseTarefa fase;
	
	@JoinColumn(name = "cdtitulo")
	@ManyToOne
	private TituloAtividade titulo;
}
