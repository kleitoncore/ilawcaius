package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "atividade_short")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtividadeShort {
	@Id
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@Column(name = "cdatividade")
	private long codigoAtividade;
	
	@Column(name = "dstitulo")
	private String titulo;
	
	@Column(name = "dtlimite")
	private String dtLimite;
	
	@Column(name = "dtfatal")
	private String dtfatal;
	
	@Column(name = "cdprocesso")
	private long codigoProcesso;
	
	@Column(name = "nrcnj")
	private String cnj;
	
	@Column(name = "nrpasta")
	private String pasta;
	
	@Column(name = "nrprocesso")
	private String nrprocesso;
	
	@Column(name = "partes")
	private String partes;
	
	@Column(name = "grupo")
	private String grupo;
	
	@Column(name = "cdgrupo")
	private long codigoGrupo;
	
	@Column(name = "subgrupo")
	private String subGrupo;
	
	@Column(name = "responsaveis")
	private String responsaveis;
	
	@Column(name = "interessados")
	private String interessados;
	
	@Column(name = "cdusuario")
	private long codigoResponsavel;
	
	@Column(name = "cdstatus")
	private long status;
	
	@Column(name = "cdusuario_interessado")
	private long codigoInteressado;
	
	@Column(name = "tpatividade")
	private String tipoAtividade;
	
	@Column(name = "status")
	private String descricaoStatus;
	
	@Column(name = "dtregistro")
	private String dtRegistro;
	
	@Column(name = "dtconcluido")
	private String dtConcluido;
	
	@Column(name = "favorito")
	private String favorito;
	
	@Column(name = "importante")
	private String importante;
	
	@Column(name = "estrategico")
	private String estrategico;
	
	@Column(name = "fase")
	private String fase;
	
	@Column(name = "snimportante")
	private String importanteAtividade;
	
	@Column(name = "snurgente")
	private String urgencia;
	
	@Column(name = "snprivado")
	private String privado;

	@Column(name = "cdcliente")
	private long codigoCliente;

	@Column(name = "nocliente")
	private String nomeCliente;

	@Column(name = "cor_status")
	private String corStatus;
	
	@Transient
	private String snFinanceiro;

	@Column(name = "dtatividade_lida")
	private String dataLida;
}
