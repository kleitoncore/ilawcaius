package com.br.ilawgestao.domains.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "processo")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Processo {
	
	@Id
	@Column(name = "cdprocesso")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdgrupo")
	private GrupoTrabalho grupoTrabalho;
	
	@Column(name = "pasta")
	private String pasta;
	
	@Column(name = "nrprocesso")
	private String nrProcesso;
	
	@Column(name = "nrcnj")
	private String nrCnj;
	
	@Column(name = "nrinstancia")
	private String nrInstancia;
	
	@Column(name = "dscomarca")
	private String dsComarca;
	
	@ManyToOne
	@JoinColumn(name = "cdtipo_acao")
	private TipoAcao tipoAcao;
	
	@ManyToOne
	@JoinColumn(name = "cdstatus_processual")
	private StatusProcessual statusProcessual;
	
	@ManyToOne
	@JoinColumn(name = "cdarea_atuacao")
	private AreaAtuacao areaAtuacao;
	
	@ManyToOne
	@JoinColumn(name = "cdtipo_decisao")
	private TipoDecisao tipoDecisao;
	
	@Column(name = "dtdistribuicao")
	private String dataDistribuicao;
	
	@Column(name = "dtultima_decisao")
	private String dataUltimaDecisao;
	
	@Column(name = "vlprovavel")
	private double vlProvavel;
	
	@Column(name = "vlpossivel")
	private double vlPossivel;
	
	@Column(name = "vlremoto")
	private double vlRemoto;
	
	@Column(name = "vlcausa")
	private double vlCausa;
	
	@Column(name = "dspedidos")
	private String dsPedidos;
	
	@Column(name = "dsobservacao")
	private String observacao;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "cdresponsavel")
	private Usuario responsavel;
	
	@Column(name = "cdstatus_interno")
	private long statusInterno;
	
	@Column(name = "dtcadastro")
	private String dataCadastro;
	
	@Column(name = "snpush")
	private String snPush;
	
	@Column(name = "snhistorico")
	private String snHistorico;
	
	@Column(name = "snemail")
	private String snEmail;
	
	@Column(name = "cdstatus")
	private long status;
	
	@ManyToOne
	@JoinColumn(name = "cdempresa")
	private Empresa empresa;
	
	@Column(name = "dtultima_movimentacao")
	private String dataUltimaMovimentacao;
	
	@Column(name = "dtsentenca")
	private String dataSentenca;
	
	@Column(name = "snimportante")
	private String snImportante;
	
	@Column(name = "tpcontingencia_contabil")
	private String tipoContingenciaContabil;
	
	@ManyToOne
	@JoinColumn(name = "cdmotivo_resultado")
	private MotivoResultado motivoResultado;
	
	@Column(name = "uf")
	private String uf;
	
	@ManyToOne
	@JoinColumn(name = "cdfase")
	private Fase fase;
	
	@ManyToOne
	@JoinColumn(name = "cdrito")
	private Rito rito;
	
	@Column(name = "snestrategico")
	private String estrategico;
	
	@Column(name = "orgao_colegiado")
	private String orgaoColegiado;
	
	@Column(name = "relator")
	private String relator;
	
	@ManyToOne
	@JoinColumn(name = "cdbancada")
	private Pessoa bancada;
	
	@Transient
	private String autor;
	
	@Transient
	private String reu;	
	
	@Transient
	private String importanteParaMim;
	
	@Transient
	private String importanteParaEmpresa;
	
	@Transient
	private String partes;
}
