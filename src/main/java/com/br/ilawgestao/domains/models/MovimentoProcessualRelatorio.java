package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "movimento_push_relatorio")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovimentoProcessualRelatorio {
	
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@Column(name = "cdprocesso")
	private long cdprocesso;
	
	@Column(name = "nrcnj")
	private String nrcnj;
	
	@Column(name = "nrpasta")
	private String nrpasta;
	
	@Column(name = "autor")
	private String autor;
	
	@Column(name = "reu")
	private String reu;
	
	@Column(name = "nosituacao")
	private String nosituacao;
	
	@Column(name = "dtmovimentacao")
	private String dtMovimentacao;
	
	@Column(name = "dsmovimentacao")
	private String dsmovimentacao;
	
	@Column(name = "cdgrupo")
	private long cdgrupo;
	
	@Column(name = "nogrupo")
	private String nogrupo;
	
	@Column(name = "cdempresa")
	private long cdempresa;
	
	@Column(name = "snenviou")
	private String snEnviou;
	
	@Column(name = "dtcarregamento")
	private String dtCarregamento;
	
	@Column(name = "tem_historico")
	private String temHistorico;
	
	@Column(name = "snoculto")
	private String snOculto;
}
