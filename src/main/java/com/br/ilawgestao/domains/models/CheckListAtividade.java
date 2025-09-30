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
@Table(name = "check_list_atividade")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckListAtividade {
	
	@Column(name = "cdchecklist")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdtitulo_atividade")
	private TituloAtividade atividade;
	
	@Column(name = "ordem")
	private long ordem;
	
	@Column(name = "tarefa")
	private String tarefa;
	
	@Column(name = "descricao")
	private String descricao;
}
