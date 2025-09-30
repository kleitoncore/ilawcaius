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
@Table(name = "historico_processo")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoProcesso {
	
	@Column(name = "cdhistorico")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdprocesso")
	private Processo processo;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@Column(name = "dshistorico")
	private String historico;
	
	@Column(name = "dthistorico")
	private String dataHistorico;
	
	@Column(name = "dtocorrencia")
	private String dataOcorrencia;
	
	@ManyToOne
	@JoinColumn(name = "cdtipo_andamento")
	private TipoAndamentoProcessual tipoAndamento;
}
