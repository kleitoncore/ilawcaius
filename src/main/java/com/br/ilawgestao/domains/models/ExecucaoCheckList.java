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
@Table(name = "execucao_check_lista_atividade")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecucaoCheckList {
	
	@Column(name = "cdexecucao")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdchecklist")
	private CheckListAtividade checkList;
	
	@ManyToOne
	@JoinColumn(name = "cdatividade")
	private Atividade atividade;
	
	@Column(name = "dtexecucao")
	private String dataExecucao;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@Column(name = "status")
	private String status;
}
