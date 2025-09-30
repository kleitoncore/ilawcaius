package com.br.ilawgestao.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.br.ilawgestao.domains.dto.ProcessoDto;
import com.br.ilawgestao.domains.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.br.ilawgestao.domains.dto.PessoaDTO;
import com.br.ilawgestao.domains.repository.EstadoCivilRepository;
import com.br.ilawgestao.domains.repository.PerfilPessoaRepository;
import com.br.ilawgestao.domains.repository.TipoEnderecoRepository;
import com.br.ilawgestao.domains.repository.filtros.FiltroPessoa;
import com.br.ilawgestao.domains.service.PessoaService;
import com.br.ilawgestao.domains.service.ProcessoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@Autowired
	private PerfilPessoaRepository perfilPessoaRepository;
	
	@Autowired
	private EstadoCivilRepository estadoCivilRepository;
	
	@Autowired
	private TipoEnderecoRepository tipoEnderecoRepository;
	
	@Autowired
	private ProcessoService processoService;
	
	@PostMapping
	public ResponseEntity<PessoaDTO> cadastrarPessoa(@RequestBody PessoaDTO pessoaDTO, HttpServletResponse response) {
		PessoaDTO pessoaSalva = pessoaService.cadastrarPessoa(pessoaDTO);
		publish.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@PostMapping("/processo")
	public ResponseEntity<PessoaDTO> cadastrarPessoaPorProcesso(@RequestBody PessoaDTO pessoaDTO, HttpServletResponse response) {
		PessoaDTO pessoaSalva = pessoaService.cadastrarPessoaSimples(pessoaDTO);
		publish.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	} 
	
	@GetMapping("/porEmpresa/{empresa}")
	public List<Pessoa> listarPessoasEmpresa(@PathVariable long empresa) {
		List<Pessoa> lista = pessoaService.listarPessoasEmpresa(empresa);
		return lista;
	}
	
	@GetMapping("/porEmpresaPerfil")
	public List<Pessoa> listarPessoasEmpresaPerfil(@RequestParam long empresa, @RequestParam String perfil) {
		return pessoaService.listarPessoasEmpresaPerfil(empresa, perfil);
	}
	
	@GetMapping
	public List<Pessoa> consultarPessoas(@RequestParam long empresa,
									     @RequestParam(required = false) String nome,
									     @RequestParam(required = false) String cpfCnpj,
									     @RequestParam(required = false) String cidade,
										 @RequestParam(required = false) String estado,
									     @RequestParam(required = false) String telefone,
									     @RequestParam(required = false) String porLetra,
									     @RequestParam(required = false) String letra,
									     @RequestParam long usuario,
									     @RequestParam(required = false) String grupo) {
		
		Empresa empresaFiltro = new Empresa();
		empresaFiltro.setCodigo(empresa);
		
		Cidade cidadeFiltro = null;
		if(cidade != null) {
			cidadeFiltro = new Cidade();
			cidadeFiltro.setCodigo(Long.parseLong(cidade));
		}

		Estado estadoFiltro = null;
		if(estado != null) {
			estadoFiltro = new Estado();
			estadoFiltro.setCodigo(Long.parseLong(estado));
		}
		
		GrupoCliente grupoFiltro = null;
		if(grupo != null) {
			grupoFiltro = new GrupoCliente();
			grupoFiltro.setCodigo(Long.parseLong(grupo));
		}
		
		Usuario usuarioFiltro = new Usuario();
		usuarioFiltro.setCodigo(usuario);
		
		FiltroPessoa filtro = new FiltroPessoa(nome, cpfCnpj, cidadeFiltro, estadoFiltro, telefone, empresaFiltro,
				porLetra, letra, grupoFiltro, usuarioFiltro);
		
		return pessoaService.consultarPessoas(filtro);
	}
	
	@GetMapping("/{codigo}")
	public PessoaDTO consultarPessoa(@PathVariable long codigo) {
		return pessoaService.consultarPessoa(codigo);
	}
	
	@PutMapping("/{codigo}")
	public PessoaDTO alterarPessoa(@PathVariable Long codigo, @RequestBody PessoaDTO pessoaDTO) {
		return pessoaService.alterarPessoa(codigo, pessoaDTO);
	}
	
	@DeleteMapping("/{codigo}")
	public void excluirPessoa(@PathVariable long codigo) {
		pessoaService.excluirPessoa(codigo);
	}
	
	@GetMapping("/historico/{pessoa}")
	public List<HistoricoPessoa> listarHistoricoPessoa(@PathVariable long pessoa) {
		return pessoaService.listarHistoricoPessoa(pessoa);
	}
	
	@GetMapping("/foraPartes")
	public List<Pessoa> listarPessoasForaDasPartesProcesso(@RequestParam long empresa, long processo, @RequestParam String perfil) {
		return pessoaService.listarPessoasForaDasPartesProcesso(empresa, processo, perfil);
	}
	
	@GetMapping("/perfisPessoa")
	public List<PerfilPessoa> listarPerfisPessoa() {
		return perfilPessoaRepository.findAll();
	}
	
	@GetMapping("/estadoCivil")
	public List<EstadoCivil> listaEstadoCivil() {
		return estadoCivilRepository.findAll();
	}
	
	@GetMapping("/tiposEndereco")
	public List<TipoEndereco> listaTiposEndereco() {
		return tipoEnderecoRepository.findAll();
	}
	
	@GetMapping("/processos")
	public List<ProcessoDto> consultarProcessosPorPessoa(@RequestParam long pessoa, @RequestParam long usuario) {
		return processoService.consultarProcessoPorpessoa(pessoa, usuario);
	}
	
	@GetMapping("/captadores")
	public List<Pessoa> listarCaptadores(@RequestParam String perfil, @RequestParam long empresa) {
		return pessoaService.listarCaptadores(perfil, empresa);
	}
	
	@GetMapping("/pessoasPorCodigos")
	public ResponseEntity<List<Pessoa>> consultarpessoas(@RequestParam String codigos) {
		List<Pessoa> lista = pessoaService.consultarPessoas(codigos);
		return new ResponseEntity<List<Pessoa>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/bancadas/{empresa}")
	public ResponseEntity<List<Pessoa>> listarBancadas(@PathVariable long empresa) {
		List<Pessoa> lista = pessoaService.listarBancada("BA", empresa);
		return new ResponseEntity<List<Pessoa>>(lista, HttpStatus.OK);
	}

	@GetMapping("/pesquisa-por-nome-cpf")
	public ResponseEntity<List<PessoaDTO>> consultarPorNomeOuCpf(@RequestParam long empresa, @RequestParam String pesquisa) {
		List<PessoaDTO> dtos = pessoaService.consultarPessoasPorNomeOuCpf(empresa, pesquisa);
		return new ResponseEntity<List<PessoaDTO>>(dtos,HttpStatus.OK);
	}
}
