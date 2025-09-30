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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "arquivo_processo")
@Data
public class ArquivoProcesso {
	
	@Id
	@Column(name = "cdarquivo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdprocesso")
	private Processo processo;
	
	@Column(name = "noarquivo")
	private String nome;
	
	@Column(name = "dsarquivo")
	private String dsArquivo;
	
	@Column(name = "dtarquivo")
	private String dataArquivo;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
	@Lob
	@Column(name = "file")
	private byte[] file;
	
	@Column(name = "tipo")
	private String tipo;
}
