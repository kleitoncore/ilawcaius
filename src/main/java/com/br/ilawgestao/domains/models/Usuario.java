package com.br.ilawgestao.domains.models;

import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
	
	@Column(name = "cdusuario")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private Long codigo;
	
	@Column(name = "nousuario")
	private String nome;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "senha")
	@JsonIgnore
	private String senha;
	
	@Column(name = "cpf")
	private String cpf;
	
	@Column(name = "cdsituacao")
	private Long situacao;
	
	@Column(name = "endereco")
	private String endereco;
	
	@Column(name = "numero")
	private String numero;
	
	@Column(name = "complemento")
	private String complemento;
	
	@Column(name = "cep")
	private String cep;
	
	@Column(name = "nobairro")
	private String bairro;
	
	@ManyToOne
	@JoinColumn(name = "cdcidade")
	private Cidade cidade;
	
	@Column(name = "telefone1")
	private String telefone1;
	
	@Column(name = "telefone2")
	private String telefone2;
	
	@Column(name = "dtcadastro")
	private String dataCadastro;
	
	@ManyToOne
	@JoinColumn(name = "cdempresa")
	private Empresa empresa;
	
	@ManyToOne
	@JoinColumn(name = "cdperfil")
	private Perfil perfil;
	
	@Column(name = "foto")
	private String foto;
	
	@Column(name = "status_login")
	private String statusLogin;
	
	@JsonIgnore
	public String getDataCadastroCalculado() throws Exception {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatoBanco = new SimpleDateFormat("yyyy-MM-dd");
		return formato.format(formatoBanco.parse(this.dataCadastro));
	}

}
