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
@Table(name = "agrupamento_tarefa")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AgrupamentoTarefa {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	@Column(name = "cdagrupamento")
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdgrupo")
	private GrupoTarefa grupo;
	
	@ManyToOne
	@JoinColumn(name = "cdtarefa")
	private Tarefa tarefa;
}
