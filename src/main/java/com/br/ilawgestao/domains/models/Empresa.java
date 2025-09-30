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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="empresa")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empresa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="cdempresa")
	@EqualsAndHashCode.Include
	private Long codigo;
	
	@Column(name="noempresa")
	private String nome;
	
	@Column(name="cpfcnpj")
	private String cpfCnpj;
	
	@Column(name="tppessoa")
	private String tipoPessoa;
	
	@Column(name="noendereco")
	private String endereco;
	
	@Column(name="nrcep")
	private String cep;
	
	@Column(name="nobairro")
	private String bairro;
	
	@JoinColumn(name="cdcidade")
	@ManyToOne
	private Cidade cidade;

	@Column(name="data_cadastro")
	private String dataCadastro;
	
	@Column(name="telefone1")
	private String telefone1;
	
	@Column(name="telefone2")
	private String telefone2;
	
	@Column(name="email")
	private String email;
	
	@Column(name="cdstatus")
	private int status;
	
	@Column(name="qtusuarios")
	private int qtdeUsuario;
	
	@Column(name="espaco")
	private int espaco;
	
	@Column(name="qtdeprocpush")
	private int qtdeProcessoPush;
	
	@Column(name = "titulo_predefinido")
	private String tituloPredefinido;
}
