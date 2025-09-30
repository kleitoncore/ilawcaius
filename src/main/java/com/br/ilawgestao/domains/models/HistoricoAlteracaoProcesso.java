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
@Table(name = "historico_alteracao_processo")
@Data
public class HistoricoAlteracaoProcesso {
	
	@Id
	@Column(name = "cdalteracao")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdprocesso")
	private Processo processo;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario_alterou")
	private Usuario usuarioAlterou;
	
	@Column(name = "dsalteracao")
	private String dsAlteracao;
	
	@Column(name = "dtalteracao")
	private String dataAlteracao;
	
	@Column(name = "tpalteracao")
	private String tipoAlteracao;
}
