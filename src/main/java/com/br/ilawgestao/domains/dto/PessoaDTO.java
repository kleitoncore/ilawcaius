package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Cidade;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.EstadoCivil;
import com.br.ilawgestao.domains.models.GrupoCliente;
import com.br.ilawgestao.domains.models.PerfilPessoa;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.TipoEndereco;

import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class PessoaDTO {
	private long codigo;
	private String nome;
	private String tipoPessoa;
	private PerfilPessoa perfil;
	private String dataNascimento;
	private String profissao;
	private String atividadeEconomica;
	private EstadoCivil estadoCivil;
	private String cpfCnpj;
	private String rg;
	private String passaporte;
	private String tituloEleitor;
	private String reservista;
	private String pis;
	private String cnh;
	private String ctps;
	private String nomeMae;
	private String nomePai;
	private String naturalidade;
	private String nacionalidade;
	private String email;
	private Pessoa captador;
	private String telefone1;
	private String telefone2;
	private String endereco;
	private String numero;
	private TipoEndereco tipoEndereco;
	private String bairro;
	private String cep;
	private Cidade cidade;
	private String contaCorrente;
	private String agencia;
	private String banco;
	private String pix;
	private String observacao;
	private int status;
	private String dataCadastro;
	private String dataUltimaAlteracao;
	private GrupoCliente grupoCliente;
	private Empresa empresa;
	private String tipoConta;
	private long usuario;
	private byte[] foto;
	private String funcionarioPublico;
	private String funcionarioPublicoCargo;
	private String funcionarioPublicoSnAposentado;
	private String funcionarioPublicoDataAposentadoria;
	private double rendaMensal;
	private String snFinanciamento;
	private String tipoFinanciamento;
	private String detalhesFinanciamento;
	private String snCadastroSerasa;
	private String loginSerasa;
	private String senhaSerasa;
	private String redesSociais;
	private List<HistoricoPessoaDto> historico;
	private List<ProcessoDto> processos;
	private int idade;
	
	public static PessoaDTO build(Pessoa pessoa, List<HistoricoPessoaDto> historico, String dataUltimaAlteracao) {
		PessoaDTO dto = new PessoaDTO();
		dto.setCodigo(pessoa.getCodigo());
		dto.setNome(pessoa.getNome());
		dto.setTipoPessoa(pessoa.getTipoPessoa());
		dto.setPerfil(pessoa.getPerfil());
		dto.setDataNascimento(pessoa.getDataNascimento());
		dto.setProfissao(pessoa.getProfissao());
		dto.setAtividadeEconomica(pessoa.getAtividadeEconomica());
		dto.setEstadoCivil(pessoa.getEstadoCivil());
	    dto.setCpfCnpj(pessoa.getCpfCnpj());
	    dto.setRg(pessoa.getRg());
	    dto.setPassaporte(pessoa.getPassaporte());
	    dto.setTituloEleitor(pessoa.getTituloEleitor());
	    dto.setReservista(pessoa.getReservista());
	    dto.setPis(pessoa.getPis());
	    dto.setCnh(pessoa.getCnh());
	    dto.setCtps(pessoa.getCtps());
	    dto.setNomeMae(pessoa.getNomeMae());
	    dto.setNomePai(pessoa.getNomePai());
	    dto.setNaturalidade(pessoa.getNaturalidade());
	    dto.setNacionalidade(pessoa.getNacionalidade());
	    dto.setEmail(pessoa.getEmail());
	    dto.setCaptador(pessoa.getCaptador());
	    dto.setTelefone1(pessoa.getTelefone1());
	    dto.setTelefone2(pessoa.getTelefone2());
	    dto.setEndereco(pessoa.getEndereco());
	    dto.setNumero(pessoa.getNumero());
	    dto.setTipoEndereco(pessoa.getTipoEndereco());
	    dto.setBairro(pessoa.getBairro());
	    dto.setCep(pessoa.getCep());
	    dto.setCidade(pessoa.getCidade());
	    dto.setContaCorrente(pessoa.getContaCorrente());
	    dto.setAgencia(pessoa.getAgencia());
	    dto.setBanco(pessoa.getBanco());
	    dto.setPix(pessoa.getPix());
	    dto.setObservacao(pessoa.getObservacao());
	    dto.setStatus(pessoa.getStatus());
	    dto.setDataCadastro(pessoa.getDataCadastro());
		dto.setDataUltimaAlteracao(dataUltimaAlteracao);
	    dto.setGrupoCliente(pessoa.getGrupoCliente());
	    dto.setEmpresa(pessoa.getEmpresa());
	    dto.setTipoConta(pessoa.getTipoConta());
	    dto.setUsuario(pessoa.getUsuario());
	    dto.setFoto(pessoa.getFoto());
		dto.setFuncionarioPublico(pessoa.getFuncionarioPublico());
		dto.setFuncionarioPublicoCargo(pessoa.getFuncionarioPublicoCargo());
		dto.setFuncionarioPublicoSnAposentado(pessoa.getFuncionarioPublicoSnAposentado());
		dto.setFuncionarioPublicoDataAposentadoria(pessoa.getFuncionarioPublicoDataAposentadoria());
		dto.setRendaMensal(pessoa.getRendaMensal());
		dto.setSnFinanciamento(pessoa.getSnFinanciamento());
		dto.setTipoFinanciamento(pessoa.getTipoFinanciamento());
		dto.setDetalhesFinanciamento(pessoa.getDetalhesFinanciamento());
		dto.setSnCadastroSerasa(pessoa.getSnCadastroSerasa());
		dto.setLoginSerasa(pessoa.getLoginSerasa());
		dto.setSenhaSerasa(pessoa.getSenhaSerasa());
		dto.setRedesSociais(pessoa.getRedesSociais());
		dto.setHistorico(historico);
	    return dto;
	}

	public static PessoaDTO buildAniversariantes(Pessoa pessoa) {
		PessoaDTO dto = new PessoaDTO();
		dto.setCodigo(pessoa.getCodigo());
		dto.setNome(pessoa.getNome());
		dto.setDataNascimento(DatasUtil.formatarDataTela(pessoa.getDataNascimento()));
		dto.setEmail(pessoa.getEmail());
		String dataNascimentoStr = pessoa.getDataNascimento();
		// Converte a string para LocalDate
		LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		// Data atual
		LocalDate hoje = LocalDate.now();
		// Calcula a idade
		int idade = calcularIdade(dataNascimento, hoje);
		dto.setIdade(idade);
		return dto;
	}

	public static int calcularIdade(LocalDate dataNascimento, LocalDate hoje) {
		if (dataNascimento != null && hoje != null) {
			return Period.between(dataNascimento, hoje).getYears();
		}
		throw new IllegalArgumentException("Data de nascimento e data atual não podem ser nulas");
	}
}
