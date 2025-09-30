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
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pessoa")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pessoa {
	
	@Id
	@Column(name = "cdpessoa")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private long codigo;
	
	@Column(name = "nopessoa")
	private String nome;
	
	@Column(name = "tppessoa")
	private String tipoPessoa;
	
	@Transient
	public String getTipoPessoaCalculada() {
		if(this.tipoPessoa.equals("F")) {
			return "Física";
		} else {
			return "Jurídica";
		}
	}
	
	@ManyToOne
	@JoinColumn(name = "cdperfil")
	private PerfilPessoa perfil;
	
	@Column(name = "dtnascimento")
	private String dataNascimento;
	
	@Column(name = "profissao")
	private String profissao;
	
	@Column(name = "atividade_economica")
	private String atividadeEconomica;
	
	@ManyToOne
	@JoinColumn(name = "cdestado_civil")
	private EstadoCivil estadoCivil;
	
	@Column(name = "cpfcnpj")
	private String cpfCnpj;
	
	@Column(name = "rg")
	private String rg;
	
	@Column(name = "passaporte")
	private String passaporte;
	
	@Column(name = "titulo")
	private String tituloEleitor;
	
	@Column(name = "reservista")
	private String reservista;
	
	@Column(name = "pis")
	private String pis;
	
	@Column(name = "cnh")
	private String cnh;
	
	@Column(name = "ctps")
	private String ctps;
	
	@Column(name = "nomae")
	private String nomeMae;
	
	@Column(name = "nopai")
	private String nomePai;
	
	@Column(name = "naturalidade")
	private String naturalidade;
	
	@Column(name = "nacionalidade")
	private String nacionalidade;
		
	@Column(name = "email")
	private String email;
	
	@Column(name = "telefone1")
	private String telefone1;
	
	@Column(name = "telefone2")
	private String telefone2;
	
	@Column(name = "endereco")
	private String endereco;
	
	@Column(name = "numero")
	private String numero;
	
	@ManyToOne
	@JoinColumn(name = "cdtipo_endereco")
	private TipoEndereco tipoEndereco;
	
	@Column(name = "bairro")
	private String bairro;
	
	@Column(name = "cep")
	private String cep;
	
	@ManyToOne
	@JoinColumn(name = "cdcidade")
	private Cidade cidade;
	
	@Column(name = "conta_corrente")
	private String contaCorrente;
	
	@Column(name = "agencia")
	private String agencia;
	
	@Column(name = "banco")
	private String banco;
	
	@Column(name = "pix")
	private String pix;
	
	@Column(name = "observacao")
	private String observacao;
	
	@Column(name = "cdstatus")
	private int status;
	
	@Transient
	public String getStatusCalculado() {
		if(this.status == 0) {
			return "Ativo";
		} else {
			return "Inativo";
		}
	}
	
	@Column(name = "dtcadastro")
	private String dataCadastro;
	
	@ManyToOne
	@JoinColumn(name = "cdgrupo_cliente")
	private GrupoCliente grupoCliente;
	
	@ManyToOne
	@JoinColumn(name = "cdempresa")
	private Empresa empresa;
	
	@Column(name = "tipo_conta")
	private String tipoConta;
	
	@Lob
	@Column(name = "img_foto")
	private byte[] foto;
	
	@Transient
	private long usuario;
	
	@ManyToOne
	@JoinColumn(name = "cdcaptador")
	private Pessoa captador;

	@Column(name = "funcionario_publico")
	private String funcionarioPublico;

	@Column(name = "funcionario_publico_cargo")
	private String funcionarioPublicoCargo;

	@Column(name = "funcionario_publico_snaposentado")
	private String funcionarioPublicoSnAposentado;

	@Column(name = "funcionario_publico_data_aposentadoria")
	private String funcionarioPublicoDataAposentadoria;

	@Column(name = "renda_mensal")
	private double rendaMensal;

	@Column(name = "snfinanciamento")
	private String snFinanciamento;

	@Column(name = "tipo_financiamento")
	private String tipoFinanciamento;

	@Column(name = "detalhe_financiamento")
	private String detalhesFinanciamento;

	@Column(name = "sncadastro_serasa")
	private String snCadastroSerasa;

	@Column(name = "login_serasa")
	private String loginSerasa;

	@Column(name = "senha_serasa")
	private String senhaSerasa;

	@Column(name = "redes_sociais")
	private String redesSociais;

}
