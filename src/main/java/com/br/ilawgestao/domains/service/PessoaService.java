package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.br.ilawgestao.domains.dto.HistoricoPessoaDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.PessoaDTO;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeJaCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.HistoricoPessoa;
import com.br.ilawgestao.domains.models.Partes;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.HistoricoPessoaRepository;
import com.br.ilawgestao.domains.repository.PartesRepository;
import com.br.ilawgestao.domains.repository.PessoaRepository;
import com.br.ilawgestao.domains.repository.filtros.FiltroPessoa;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class PessoaService {
	
	private static final String TIPO_HISTORICO_INCLUSAO = "I";
	private static final String TIPO_HISTORICO_ALTERACAO = "A";
	private static final String TIPO_HISTORICO_INATIVACAO = "X";
	
	@Autowired
	private PessoaRepository pessoaReopository;
		
	@Autowired
	private HistoricoPessoaRepository historicoRepository;
		
	@Autowired
	private PartesRepository partesRepository;
	
	@Autowired
	private HistoricoPessoaRepository historicoPessoaRepository;
		
	public PessoaDTO cadastrarPessoa(PessoaDTO dto) {
		Optional<Pessoa> pessoaNome = pessoaReopository.consultarPessoaPorNome(dto.getNome(), dto.getEmpresa().getCodigo());
		if(pessoaNome.isPresent()) {
			throw new EntidadeJaCadastradaException("Pessoa já cadstrada com esse nome");
		}
		
		if(dto.getCpfCnpj() != "") {
			Optional<Pessoa> pessoaCpf = pessoaReopository.consultarPessoaPorCpf(dto.getCpfCnpj(), dto.getEmpresa().getCodigo());	
			if(pessoaCpf.isPresent()) {
				throw new EntidadeJaCadastradaException("Pessoa já cadstrada com esse CPF/CNPJ");
			}
		}
		
		//Data de Cadastro
		dto.setDataCadastro(DatasUtil.getDataAtual());
		//Status automático
		dto.setStatus(0);
		
		//Ajustando Data de Nascimento
		if(dto.getTipoPessoa().equals("J")) {
			dto.setDataNascimento(null);
		} else {
			if(dto.getDataNascimento().equals(null) || dto.getDataNascimento().equals("")) {
				dto.setDataNascimento(null);
			}
		}
		
		Pessoa pessoa = Pessoa.builder()
				.nome(dto.getNome())
				.tipoPessoa(dto.getTipoPessoa())
				.perfil(dto.getPerfil())
				.dataNascimento(dto.getDataNascimento())
				.profissao(dto.getProfissao())
				.atividadeEconomica(dto.getAtividadeEconomica())
				.estadoCivil(dto.getEstadoCivil())
				.cpfCnpj(dto.getCpfCnpj())
				.rg(dto.getRg())
				.passaporte(dto.getPassaporte())
				.tituloEleitor(dto.getTipoConta())
				.reservista(dto.getReservista())
				.pis(dto.getPis())
				.cnh(dto.getCnh())
				.ctps(dto.getCtps())
				.nomeMae(dto.getNomeMae())
				.nomePai(dto.getNomePai())
				.naturalidade(dto.getNaturalidade())
				.nacionalidade(dto.getNacionalidade())
				.email(dto.getEmail())
				.captador(dto.getCaptador())
				.telefone1(dto.getTelefone1())
				.telefone2(dto.getTelefone2())
				.endereco(dto.getEndereco())
				.numero(dto.getNumero())
				.tipoEndereco(dto.getTipoEndereco())
				.bairro(dto.getBairro())
				.cep(dto.getCep())
				.cidade(dto.getCidade())
				.contaCorrente(dto.getContaCorrente())
				.agencia(dto.getAgencia())
				.banco(dto.getBanco())
				.pix(dto.getPix())
				.observacao(dto.getObservacao())
				.status(dto.getStatus())
				.dataCadastro(dto.getDataCadastro())
				.grupoCliente(dto.getGrupoCliente())
				.empresa(dto.getEmpresa())
				.tipoConta(dto.getTipoConta())
				.usuario(dto.getUsuario())
				.foto(dto.getFoto())
				.funcionarioPublico(dto.getFuncionarioPublico())
				.funcionarioPublicoCargo(dto.getFuncionarioPublicoCargo())
				.funcionarioPublicoSnAposentado(dto.getFuncionarioPublicoSnAposentado())
				.funcionarioPublicoDataAposentadoria(dto.getFuncionarioPublicoDataAposentadoria())
				.rendaMensal(dto.getRendaMensal())
				.snFinanciamento(dto.getSnFinanciamento())
				.tipoFinanciamento(dto.getTipoFinanciamento())
				.detalhesFinanciamento(dto.getDetalhesFinanciamento())
				.snCadastroSerasa(dto.getSnCadastroSerasa())
				.loginSerasa(dto.getLoginSerasa())
				.senhaSerasa(dto.getSenhaSerasa())
				.redesSociais(dto.getRedesSociais())
				.build();
				
		Pessoa pessoaSalva = pessoaReopository.save(pessoa);
		
		//Histórico da Pessoa
		HistoricoPessoa historico = new HistoricoPessoa();
		historico.setDataHistorico(pessoa.getDataCadastro());
		historico.setPessoa(pessoaSalva);
		Usuario usu = new Usuario();
		usu.setCodigo(pessoa.getUsuario());
		historico.setUsuario(usu);
		historico.setDsHistorico("Inclusão de nova pessoa");
		historico.setTipo(TIPO_HISTORICO_INCLUSAO);
		HistoricoPessoa historicoSalvo = incluirHistoricoPessoa(historico);
		HistoricoPessoaDto historicoDto  = HistoricoPessoaDto.buildConsulta(historicoSalvo);
		List<HistoricoPessoaDto> historicos = new ArrayList<>();
		historicos.add(historicoDto);
		
		return PessoaDTO.build(pessoaSalva,historicos,pessoa.getDataCadastro());
	}
	
	public PessoaDTO cadastrarPessoaSimples(PessoaDTO dto) {
		Optional<Pessoa> pessoaNome = pessoaReopository.consultarPessoaPorNome(dto.getNome(), dto.getEmpresa().getCodigo());
		if(pessoaNome.isPresent()) {
			throw new EntidadeJaCadastradaException("Pessoa já cadstrada com esse nome");
		}
		
		//Data de Cadastro
		dto.setDataCadastro(DatasUtil.getDataAtual());
		//Status automático
		dto.setStatus(0);
		dto.setDataNascimento(null);
		
		Pessoa pessoa = Pessoa.builder()
				.nome(dto.getNome())
				.tipoPessoa(dto.getTipoPessoa())
				.perfil(dto.getPerfil())
				.dataCadastro(dto.getDataCadastro())
				.empresa(dto.getEmpresa())
				.cidade(dto.getCidade()).build();
		
		Pessoa pessoaSalva = pessoaReopository.save(pessoa);
		
		//Histórico da Pessoa
		if(pessoaSalva != null) {
			HistoricoPessoa historico = new HistoricoPessoa();
			historico.setDataHistorico(dto.getDataCadastro());
			historico.setPessoa(pessoaSalva);
			Usuario usu = new Usuario();
			usu.setCodigo(dto.getUsuario());
			historico.setUsuario(usu);
			historico.setDsHistorico("Inclusão de nova pessoa");
			historico.setTipo(TIPO_HISTORICO_INCLUSAO);
			incluirHistoricoPessoa(historico);
		}
		
		return PessoaDTO.build(pessoaSalva,null,null);
	}
	
	public List<Pessoa> consultarPessoas(FiltroPessoa filtro) {
		return pessoaReopository.filtrarPessoas(filtro);
	}
	
	public List<Pessoa> listarPessoasEmpresa(long empresa) {
		List<Pessoa> lista = pessoaReopository.findByEmpresaCodigoOrderByNome(empresa);
		return lista;
	}
	
	public List<Pessoa> listarPessoasEmpresaPerfil(long empresa, String perfil) {
		return pessoaReopository.findByEmpresaCodigoAndPerfilCodigoOrderByNome(empresa, perfil);
	}
	
	public PessoaDTO alterarPessoa(Long codigo, PessoaDTO dto) {
		PessoaDTO pessoaConsulta = consultarPessoa(codigo);
		
		Optional<Pessoa> pessoaNome = pessoaReopository.consultarPessoaPorNome(dto.getNome(), dto.getEmpresa().getCodigo());
		if(pessoaNome.isPresent() && pessoaNome.get().getCodigo() != pessoaConsulta.getCodigo()) {
			throw new EntidadeJaCadastradaException("Já existe uma Pessoa cadastrada com este nome");
		}
		
		BeanUtils.copyProperties(dto, pessoaConsulta,"codigo", "dataCadastro");
		
		Pessoa pessoa = Pessoa.builder()
				.codigo(pessoaConsulta.getCodigo())
				.nome(pessoaConsulta.getNome())
				.tipoPessoa(pessoaConsulta.getTipoPessoa())
				.perfil(pessoaConsulta.getPerfil())
				.dataNascimento(pessoaConsulta.getDataNascimento())
				.profissao(pessoaConsulta.getProfissao())
				.atividadeEconomica(pessoaConsulta.getAtividadeEconomica())
				.estadoCivil(pessoaConsulta.getEstadoCivil())
				.cpfCnpj(pessoaConsulta.getCpfCnpj())
				.rg(pessoaConsulta.getRg())
				.passaporte(pessoaConsulta.getPassaporte())
				.tituloEleitor(pessoaConsulta.getTipoConta())
				.reservista(pessoaConsulta.getReservista())
				.pis(pessoaConsulta.getPis())
				.cnh(pessoaConsulta.getCnh())
				.ctps(pessoaConsulta.getCtps())
				.nomeMae(pessoaConsulta.getNome())
				.nomePai(pessoaConsulta.getNomePai())
				.naturalidade(pessoaConsulta.getNaturalidade())
				.nacionalidade(pessoaConsulta.getNacionalidade())
				.email(pessoaConsulta.getEmail())
				.captador(pessoaConsulta.getCaptador())
				.telefone1(pessoaConsulta.getTelefone1())
				.telefone2(pessoaConsulta.getTelefone2())
				.endereco(pessoaConsulta.getEndereco())
				.numero(pessoaConsulta.getNumero())
				.tipoEndereco(pessoaConsulta.getTipoEndereco())
				.bairro(pessoaConsulta.getBairro())
				.cep(pessoaConsulta.getCep())
				.cidade(pessoaConsulta.getCidade())
				.contaCorrente(pessoaConsulta.getContaCorrente())
				.agencia(pessoaConsulta.getAgencia())
				.banco(pessoaConsulta.getBanco())
				.pix(pessoaConsulta.getPix())
				.observacao(pessoaConsulta.getObservacao())
				.status(pessoaConsulta.getStatus())
				.dataCadastro(pessoaConsulta.getDataCadastro())
				.grupoCliente(pessoaConsulta.getGrupoCliente())
				.empresa(pessoaConsulta.getEmpresa())
				.tipoConta(pessoaConsulta.getTipoConta())
				.usuario(pessoaConsulta.getUsuario())
				.foto(pessoaConsulta.getFoto())
				.funcionarioPublico(dto.getFuncionarioPublico())
				.funcionarioPublicoCargo(dto.getFuncionarioPublicoCargo())
				.funcionarioPublicoSnAposentado(dto.getFuncionarioPublicoSnAposentado())
				.funcionarioPublicoDataAposentadoria(dto.getFuncionarioPublicoDataAposentadoria())
				.rendaMensal(dto.getRendaMensal())
				.snFinanciamento(dto.getSnFinanciamento())
				.tipoFinanciamento(dto.getTipoFinanciamento())
				.detalhesFinanciamento(dto.getDetalhesFinanciamento())
				.snCadastroSerasa(dto.getSnCadastroSerasa())
				.loginSerasa(dto.getLoginSerasa())
				.senhaSerasa(dto.getSenhaSerasa())
				.redesSociais(dto.getRedesSociais())
				.build();
		
		Pessoa pessoaSalva = pessoaReopository.save(pessoa);

		List<HistoricoPessoaDto> historicoDtos = new ArrayList<>();
		if(pessoaSalva != null) {
			//histórico
			HistoricoPessoa historico = new HistoricoPessoa();
			historico.setDataHistorico(DatasUtil.getDataAtual());
			historico.setPessoa(pessoaSalva);
			Usuario usu = new Usuario();
			usu.setCodigo(pessoa.getUsuario());
			historico.setUsuario(usu);
			historico.setDsHistorico("Alteração de pessoa");
			historico.setTipo(TIPO_HISTORICO_ALTERACAO);
			incluirHistoricoPessoa(historico);

			if(pessoaSalva.getStatus() == 1) {
				HistoricoPessoa historicoInativação = new HistoricoPessoa();
				historicoInativação.setDataHistorico(DatasUtil.getDataAtual());
				historicoInativação.setPessoa(pessoaSalva);
				Usuario us = new Usuario();
				usu.setCodigo(pessoa.getUsuario());
				historicoInativação.setUsuario(us);
				historicoInativação.setDsHistorico("Inativação de psssoa");
				historicoInativação.setTipo(TIPO_HISTORICO_INATIVACAO);
				incluirHistoricoPessoa(historicoInativação);
			}

			List<HistoricoPessoa> historicos = historicoPessoaRepository.findByPessoaCodigoOrderByDataHistoricoDesc(pessoaSalva.getCodigo());
			if(historicos != null && historicos.size() > 0) {
				for(HistoricoPessoa hist : historicos) {
					historicoDtos.add(HistoricoPessoaDto.buildConsulta(hist));
				}
			}
		}
		
		return PessoaDTO.build(pessoaSalva,historicoDtos,DatasUtil.getDataAtual());
	}
	
	/**
	 * Lista pessoas que ainda estão fora das partes do processo
	 * @param processo
	 * @return
	 */
	public List<Pessoa> listarPessoasForaDasPartesProcesso(long empresa, long processo, String perfil) {
		return pessoaReopository.listarPessoasForaDasPartesProcesso(empresa, processo, perfil);
	}
	
	public PessoaDTO consultarPessoa(long codigo) {
		Optional<Pessoa> pessoa = pessoaReopository.findById(codigo);
		if(!pessoa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Pessoa não encontrada");
		}

		List<HistoricoPessoaDto> historicoDtos = new ArrayList<>();
		List<HistoricoPessoa> historicos = historicoPessoaRepository.findByPessoaCodigoOrderByDataHistoricoDesc(pessoa.get().getCodigo());
		if(historicos != null && historicos.size() > 0) {
			for(HistoricoPessoa hist : historicos) {
				historicoDtos.add(HistoricoPessoaDto.buildConsulta(hist));
			}
		}
		Optional<HistoricoPessoa> ultimaAlteracao = historicoPessoaRepository.ultimaAlteracao(codigo);
		PessoaDTO dto = null;
		if(ultimaAlteracao.isPresent()) {
			dto = PessoaDTO.build(pessoa.get(), historicoDtos,ultimaAlteracao.get().getDataHistorico());
		} else {
			dto = PessoaDTO.build(pessoa.get(), historicoDtos,pessoa.get().getDataCadastro());
		}
		return dto;
	}
	
	public void inativarPessoa(long codigo, Pessoa pessoa) {
		pessoaReopository.save(pessoa);
	}
	
	public void excluirPessoa(long codigo) {
		//Verificando se processo para esta pessoa em partes
		List<Partes> partes = partesRepository.findByPessoaCodigo(codigo);
		if(!partes.isEmpty()) {
			throw new EntidadeEmUsoException("Atenção! esta pessoa não pode ser excluída, está cadastrado em um processo");
		}
		
		//Exclui o histórico da pessoa
		List<HistoricoPessoa> historico = historicoPessoaRepository.findByPessoaCodigoOrderByDataHistoricoDesc(codigo);
		if(historico != null) {
			for(HistoricoPessoa hist : historico) {
				historicoPessoaRepository.deleteById(hist.getCodigo());
			}
		}

		pessoaReopository.deleteById(codigo);
	}
	
	public List<HistoricoPessoa> listarHistoricoPessoa(long pessoa) {
		Optional<Pessoa> pessoaConsulta = pessoaReopository.findById(pessoa);
		if(!pessoaConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Pessoa não encontrada");
		}
		return historicoRepository.findByPessoaCodigoOrderByDataHistoricoDesc(pessoa);
	}

	public List<PessoaDTO> consultarPessoasPorNomeOuCpf(long empresa,String pesquisa) {
		List<Pessoa> pessoas = pessoaReopository.consultarPessoaPorNomeOuCpf(empresa,pesquisa);
		if(pessoas == null || pessoas.size() == 0) {
			throw new EntidadeNaoEncontradaException("Nenhum registro encontrado");
		}

		List<PessoaDTO> dtos = new ArrayList<PessoaDTO>();
		for(Pessoa pessoa : pessoas) {
			dtos.add(PessoaDTO.build(pessoa,null,null));
		}

		return dtos;
	}
	
	private HistoricoPessoa incluirHistoricoPessoa(HistoricoPessoa historico) {
		return historicoRepository.save(historico);
	}
	
	public List<Pessoa> consultarPessoas(String codigos) {
		return pessoaReopository.consultarPessoas(codigos);
	}
	
	/**
	 * Lista Captadores
	 * @param perfil
	 * @param empresa
	 * @return
	 */
	public List<Pessoa> listarCaptadores(String perfil, long empresa) {
		return pessoaReopository.findByPerfilCodigoAndEmpresaCodigoOrderByNome(perfil, empresa);
	}
	
	/**
	 * Lista Bancada de Advogados
	 * @param perfil
	 * @param empresa
	 * @return
	 */
	public List<Pessoa> listarBancada(String perfil, long empresa) {
		return pessoaReopository.findByPerfilCodigoAndEmpresaCodigoOrderByNome(perfil, empresa);
	}
}
