package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.AgravoApensoDto;
import com.br.ilawgestao.domains.dto.PartesDto;
import com.br.ilawgestao.domains.dto.ProcessoDto;
import com.br.ilawgestao.domains.exception.EntidadeJaCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.AgravoApenso;
import com.br.ilawgestao.domains.models.AreaAtuacao;
import com.br.ilawgestao.domains.models.Cidade;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Partes;
import com.br.ilawgestao.domains.models.PerfilPessoa;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.StatusProcessual;
import com.br.ilawgestao.domains.models.TipoAcao;
import com.br.ilawgestao.domains.models.TipoDecisao;
import com.br.ilawgestao.domains.repository.AgravoApensoRepository;
import com.br.ilawgestao.domains.repository.AreaAtuacaoRepository;
import com.br.ilawgestao.domains.repository.GrupoTrabalhoRepository;
import com.br.ilawgestao.domains.repository.PartesRepository;
import com.br.ilawgestao.domains.repository.PessoaRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.repository.StatusProcessualRepository;
import com.br.ilawgestao.domains.repository.TipoAcaoRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class AgravoApensoService {
	
	@Autowired
	private AgravoApensoRepository agravoApensoRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private GrupoTrabalhoRepository grupoRepository;
	
	@Autowired
	private StatusProcessualRepository statusRepository;
	
	@Autowired
	private AreaAtuacaoRepository areaAtuacaoRepository;
	
	@Autowired
	private TipoAcaoRepository tipoAcaoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PartesRepository parteRepository;
	
	@Autowired
	private ProcessoService processoService;
	
	
	public AgravoApensoDto cadastrar(AgravoApensoDto dto) {
		Optional<Processo> consultaNovoProcesso = processoRepository.findByNrCnjAndEmpresaCodigoAndStatus(dto.getProcesso().getNrCnj(), 
				dto.getProcessoPrincipal().getEmpresa().getCodigo(), 0l);
		
		if(consultaNovoProcesso.isPresent()) {
			Optional<AgravoApenso> consulta = agravoApensoRepository.findByProcessoPrincipalCodigoAndProcessoCodigo(dto.getProcessoPrincipal().getCodigo(), 
					consultaNovoProcesso.get().getCodigo());
			if(consulta.isPresent()) {
				throw new EntidadeJaCadastradaException("Atenção! Processo já cadastrado como apenso ou agravo para este processo principal");
			}
		}
		AgravoApenso agravoApenso = AgravoApenso.builder()
				.processoPrincipal(dto.getProcessoPrincipal())
				.processo(this.completaProcesso(dto))
				.camara(dto.getCamara())
				.relator(dto.getRelator())
				.tipo(dto.getTipo())
				.build();
		
		AgravoApenso agravoApensoSalvo = agravoApensoRepository.save(agravoApenso);
		return AgravoApensoDto.build(agravoApensoSalvo);
	}
	
	private Processo completaProcesso(AgravoApensoDto dto) {
		Optional<Processo> consulta = processoRepository.findByNrCnjAndEmpresaCodigoAndStatus(dto.getProcesso().getNrCnj(), dto.getProcessoPrincipal().getEmpresa().getCodigo(),
				0l);
		if(consulta.isPresent()) {
			return consulta.get();
		}
		ProcessoDto processoDto = new ProcessoDto();
		if(dto.getProcesso().getNrCnj().length() != 20) {
			processoDto.setNrProcesso(this.retiraMascaraProcesso(dto.getProcesso().getNrCnj()));
			processoDto.setNrCnj(null);
		} else {
			processoDto.setNrCnj(this.retiraMascaraProcesso(dto.getProcesso().getNrCnj()));
			processoDto.setNrProcesso(this.retiraMascaraProcesso(dto.getProcesso().getNrCnj()));
		}
		processoDto.setGrupoTrabalho(this.consultarGrupoTranalho(dto.getProcessoPrincipal().getEmpresa().getCodigo(), dto.getTipo()));
		processoDto.setStatusProcessual(this.consultarStatusProcessual(dto.getProcessoPrincipal().getEmpresa().getCodigo(), dto.getTipo()));
		processoDto.setAreaAtuacao(this.consultarAreaAtuacao(dto.getProcessoPrincipal().getEmpresa().getCodigo(), dto.getTipo()));
		processoDto.setTipoAcao(this.consultarTipoAcao(dto.getProcessoPrincipal().getEmpresa().getCodigo(), dto.getTipo()));
		processoDto.setPartes(this.retornaPartes(dto));
		processoDto.setSnPush("N");
		processoDto.setSnEmail("N");
		processoDto.setSnHistorico("N");
		processoDto.setUsuario(dto.getUsuario());
		processoDto.setResponsavel(dto.getUsuario());
		processoDto.setEmpresa(dto.getProcessoPrincipal().getEmpresa());
		processoDto.setStatus(0);
		TipoDecisao tipoDecisao = new TipoDecisao();
		tipoDecisao.setCodigo(1);
		processoDto.setTipoDecisao(tipoDecisao);
		processoDto.setDataCadastro(DatasUtil.getDataAtual());
		processoDto.setDataUltimaMovimentacao(DatasUtil.getDataAtual());
		Processo processo = processoRepository.save(ProcessoDto.build(processoDto));
		//Partes
		if(processo != null) {
			processoDto.setCodigo(processo.getCodigo());
			this.cadastrarPartes(processoDto);
			//Índice de Processo
			processoService.cadastrarIndiceProcesso(processoDto);
		}
		return processo;	
	}
	
	private String retiraMascaraProcesso(String processo) {
		String cnj = processo.replace("-", "");
		cnj = cnj.replace(".", "");
		return cnj;
	}
	
	private List<PartesDto> retornaPartes(AgravoApensoDto dto) {
		List<PartesDto> partesDto = new ArrayList<PartesDto>();
		for(PartesDto parte : dto.getPartes()) {
			Optional<Pessoa> nome = pessoaRepository.consultarPessoaPorNome(parte.getPessoa().getNome(), dto.getProcessoPrincipal().getEmpresa().getCodigo());
			if(!nome.isPresent()) {
				Pessoa cadastro = new Pessoa();
				cadastro.setNome(parte.getPessoa().getNome());
				Cidade cidade = new Cidade();
				cidade.setCodigo(2L);
				cadastro.setCidade(cidade);
				cadastro.setStatus(0);
				cadastro.setDataCadastro(DatasUtil.getDataAtual());
				cadastro.setTipoPessoa("F");
				PerfilPessoa perfil = new PerfilPessoa();
				perfil.setCodigo("CL");
				cadastro.setPerfil(perfil);
				cadastro.setEmpresa(dto.getProcessoPrincipal().getEmpresa());
				Pessoa pessoaCadastrada = pessoaRepository.save(cadastro);
				//Partes
				PartesDto parteDto = new PartesDto();
				parteDto.setPessoa(pessoaCadastrada);
				parteDto.setProcesso(dto.getProcesso());
				parteDto.setTipo(parte.getTipo());
				partesDto.add(parteDto);
			} else {
				//Partes
				PartesDto parteDto = new PartesDto();
				parteDto.setPessoa(nome.get());
				parteDto.setProcesso(dto.getProcesso());
				parteDto.setTipo(parte.getTipo());
				partesDto.add(parteDto);
			}
		}
		
		return partesDto;
	}
	
	public ProcessoDto consultarProcessoExistens(String numero, long empresa) {
		numero = numero.replace("-", "");
		numero = numero.replace(".", "");
		
		//Pesquisa pelo número do CNJ
		Optional<Processo> processo = processoRepository.findByNrCnjAndEmpresaCodigoAndStatus(numero, empresa, 0l);
		if(processo.isPresent()) {
			ProcessoDto dto = ProcessoDto.build(processo.get());
			return dto;
		}
		
		//Pesquisa pelo número do Processo
		Optional<Processo> processoNumero = processoRepository.findByNrProcessoAndEmpresaCodigoAndStatus(numero, empresa, 0l);
		if(processoNumero.isPresent()) {
			ProcessoDto dto = ProcessoDto.build(processoNumero.get());
			return dto;
		}
		
		return null;
	}
	
	private void cadastrarPartes(ProcessoDto processoDto) {
		if(processoDto.getPartes() == null) {
			throw new EntidadeNaoEncontradaException("Partes não encontradas");
		}
		
		for(PartesDto parteDto : processoDto.getPartes()) {
			Partes parte = new Partes();
			parte.setPessoa(parteDto.getPessoa());
			Processo processo = new Processo();
			processo.setCodigo(processoDto.getCodigo());
			parte.setProcesso(processo);
			parte.setTipoParte(parteDto.getTipo());
			parteRepository.save(parte);
		}
	}
	
	public AgravoApensoDto consultarAgravoApenso(long processo) {
		Optional<AgravoApenso> processoConsulta = agravoApensoRepository.findByProcessoCodigo(processo);
		if(processoConsulta.isPresent()) {
			AgravoApensoDto dto = AgravoApensoDto.build(processoConsulta.get());
			return dto;
		}
		
		return null;
	}
	
	public AgravoApensoDto alterarAgravoApenso(AgravoApensoDto dto) {
		Optional<AgravoApenso> consulta = agravoApensoRepository.findById(dto.getCodigo());
		if(!consulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo Agravo/Apenso não encontrado");
		}
		
		AgravoApensoDto dtoConsulta = AgravoApensoDto.buildAlterar(consulta.get());
		
		BeanUtils.copyProperties(dto, dtoConsulta, "codigo","processo","processoPrincipal","tipo");
		AgravoApenso agravoApensoSalvar = AgravoApenso.builder()
				.codigo(dtoConsulta.getCodigo())
				.processo(dtoConsulta.getProcesso())
				.processoPrincipal(dtoConsulta.getProcessoPrincipal())
				.camara(dtoConsulta.getCamara())
				.tipo(dtoConsulta.getTipo())
				.relator(dtoConsulta.getRelator())
				.build();
		return AgravoApensoDto.build(agravoApensoRepository.save(agravoApensoSalvar));
		
	}
	
	public List<AgravoApensoDto> listarProcessos(long processo) {
		List<AgravoApenso> lista = agravoApensoRepository.findByProcessoPrincipalCodigo(processo);
		List<AgravoApensoDto> dtos = new ArrayList<AgravoApensoDto>();
		if(lista != null && lista.size() > 0) {
			for(AgravoApenso agp : lista) {
				dtos.add(AgravoApensoDto.build(agp));
			}
		}
		return dtos;
	}
	
	private TipoAcao consultarTipoAcao(long empresa, String tipo) {
		String tp = "";
		if(tipo.equals("AG")) {
			tp = "Agravo de Instrumento";
		} else {
			tp = "Processo Apenso";
		}
		
		Optional<TipoAcao> tipoAcao = tipoAcaoRepository.findByNomeAndEmpresaCodigo(tp, empresa);
		if(!tipoAcao.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Ação não encontrada não encontrado");
		}
		
		return tipoAcao.get();
	}
	
	private AreaAtuacao consultarAreaAtuacao(long empresa, String tipo) {
		String tp = "";
		if(tipo.equals("AG")) {
			tp = "Agravo de Instrumento";
		} else {
			tp = "Processo Apenso";
		}
		
		Optional<AreaAtuacao> area = areaAtuacaoRepository.findByNomeAndEmpresaCodigo(tp, empresa);
		if(!area.isPresent()) {
			throw new EntidadeNaoEncontradaException("Área de Atuação não encontrada");
		}
		
		return area.get();
	}
	
	private StatusProcessual consultarStatusProcessual(long empresa, String tipo) {
		String tp = "";
		if(tipo.equals("AG")) {
			tp = "Agravo de Instrumento";
		} else {
			tp = "Processo Apenso";
		}
		
		Optional<StatusProcessual> status = statusRepository.findByDescricaoAndEmpresaCodigo(tp, empresa);
		if(!status.isPresent()) {
			throw new EntidadeNaoEncontradaException("Status Processual não encontrado");
		}
		
		return status.get();
	}
	
	private GrupoTrabalho consultarGrupoTranalho(long empresa, String tipo) {
		String tp = "";
		
		if(tipo.equals("AG")) {
			tp = "Agravo de Instrumento";
		} else {
			tp = "Processo Apenso";
		}
		
		Optional<GrupoTrabalho> grupo = grupoRepository.consultarGrupoTrabalhoPoNome(tp, empresa);
		if(!grupo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrado");
		}
		
		return grupo.get();
	}
	
	public ProcessoDto consultarProcessoExistente(long codigo) {
		return processoService.consultarProcessoAgravoApenso(codigo);
	}
	
	public void excluirAgravoApenso(long codigo) {
		agravoApensoRepository.deleteById(codigo);
	}
}
