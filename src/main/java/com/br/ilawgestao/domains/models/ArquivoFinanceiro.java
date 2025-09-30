package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "arquivo_financeiro")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArquivoFinanceiro {
	
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@Column(name = "noarquivo")
	private String arquivo;
	
	@ManyToOne
	@JoinColumn(name = "cdlancamento")
	private Lancamento lancamento;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@Column(name = "dtregistro")
	private String dtRegistro;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Lob
	@Column(name = "file")
	private byte[] file;
	
	@Column(name = "tipo")
	private String tipo;
	
}
