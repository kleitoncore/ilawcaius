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
@Table(name = "arquivo_atividade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArquivoAtividade {
	
	@Id
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdatividade")
	private Atividade atividade;
	
	@Column(name = "noarquivo")
	private String arquivo;
	
	@Column(name = "dsarquivo")
	private String dsArquivo;
	
	@Column(name = "dtarquivo")
	private String dtArquivo;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Lob
	@Column(name = "file")
	private byte[] file;
	
	@ManyToOne
	@JoinColumn(name = "cdusuario")
	private Usuario usuario;
	
}
