package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Atendimento;
import com.br.ilawgestao.domains.models.AtendimentoHistorico;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.AtendimentoHistoricoRepository;
import com.br.ilawgestao.domains.repository.AtendimentoRepository;
import com.br.ilawgestao.domains.repository.EmpresaRepository;
import com.br.ilawgestao.domains.repository.GrupoTrabalhoRepository;
import com.br.ilawgestao.domains.repository.PessoaRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class AtendimentoService {
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private AtendimentoHistoricoRepository atendimentoHistoricoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private GrupoTrabalhoRepository grupoRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	public Atendimento cadastrarPrimeiroAtendimento(Atendimento atendimento) {
		Optional<Pessoa> pessoaConsulta = pessoaRepository.findById(atendimento.getPessoa().getCodigo());
		if(!pessoaConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Pessoa não encontrada");
		}
		
		Optional<GrupoTrabalho> grupoConsulta = grupoRepository.findById(atendimento.getGrupo().getCodigo());
		if(!grupoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrada");
		}
		
		Optional<Empresa> empresaConsulta = empresaRepository.findById(atendimento.getEmpresa().getCodigo());
		if(!empresaConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		if(atendimento.getProcesso() != null) {
			Optional<Processo> processoConsulta = processoRepository.findById(atendimento.getProcesso().getCodigo());
			if(!processoConsulta.isPresent()) {
				throw new EntidadeNaoEncontradaException("Proceso não encontrado");
			}
		}
		
		atendimento.setDtAtendimento(DatasUtil.getDataAtual());
		
		return atendimentoRepository.save(atendimento);
 	}
	
	public AtendimentoHistorico cadastrarHistorico(AtendimentoHistorico historico) {
		Optional<Atendimento> atendimentoConsulta = atendimentoRepository.findById(historico.getAtendimento().getCodigo());
		if(!atendimentoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Atendimento não encontrado");
		}
		
		Optional<Usuario> usuarioConsulta = usuarioRepository.findById(historico.getUsuario().getCodigo());
		if(!usuarioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("usuário não encontrado");
		}
		
		historico.setDtAtendimento(DatasUtil.getDataAtual());
		
		return atendimentoHistoricoRepository.save(historico);
	}
	
	public List<AtendimentoHistorico> consultarAtendimentoHistorico(long atendimento) {
		List<AtendimentoHistorico> lista = atendimentoHistoricoRepository.findByAtendimentoCodigoOrderByCodigoDesc(atendimento);
		List<AtendimentoHistorico> historicos = new ArrayList<AtendimentoHistorico>();
		if(lista != null) {
			for(AtendimentoHistorico hist : lista) {
				AtendimentoHistorico historico = new AtendimentoHistorico();
				historico.setCodigo(hist.getCodigo());
				historico.setAtendimento(hist.getAtendimento());
				historico.setUsuario(hist.getUsuario());
				historico.setDtAtendimento(DatasUtil.formatarDataTela(hist.getDtAtendimento()));
				historico.setHistorico(hist.getHistorico());
				historicos.add(historico);
			}
		}
		
		return historicos;
	}
	
	public Atendimento consultarAtendimento(long codigo) {
		Optional<Atendimento> atendimento = atendimentoRepository.findById(codigo);
		if(!atendimento.isPresent()) {
			throw new EntidadeNaoEncontradaException("Atendimento não encontrado");
		}
		
		Atendimento atendimentoRetorno = new Atendimento(atendimento.get().getCodigo(), 
				atendimento.get().getPessoa(), atendimento.get().getProcesso(), atendimento.get().getGrupo(), 
				atendimento.get().getAssunto(), DatasUtil.formatarDataTela(atendimento.get().getDtAtendimento()), atendimento.get().getEmpresa());
		
		return atendimentoRetorno;
	}
	
	public List<Atendimento> listarAtendimento(long empresa, String dataInicial, String dataFinal, long pessoa, long grupo) {
		List<Atendimento> atendimentos = new ArrayList<Atendimento>();
		List<Atendimento> lista = atendimentoRepository.listarAtendimentos(empresa, dataInicial, dataFinal, pessoa, grupo);
		if(lista != null) {
			for(Atendimento atend: lista) {
				Atendimento atendimento = new Atendimento();
				atendimento.setCodigo(atend.getCodigo());
				atendimento.setPessoa(atend.getPessoa());
				atendimento.setProcesso(atend.getProcesso());
				atendimento.setGrupo(atend.getGrupo());
				atendimento.setAssunto(atend.getAssunto());
				atendimento.setEmpresa(atend.getEmpresa());
				atendimento.setDtAtendimento(DatasUtil.formatarDataTela(atend.getDtAtendimento()));
				atendimentos.add(atendimento);
			}
		}
		
		return atendimentos;
	}
	
	public void excluirHistorico(long codigo) {
		atendimentoHistoricoRepository.deleteById(codigo);
	}
	
	public Atendimento alterarAtendimentoGrupo(long codigo, Atendimento atendimento) {
		Atendimento atendimentoConsulta = atendimentoRepository.findById(codigo).get();
		BeanUtils.copyProperties(atendimento, atendimentoConsulta,"codigo","dtAtendimento", "processo","pessoa","empresa", "assunto");
		return atendimentoRepository.save(atendimentoConsulta);
	}
	
	public Atendimento alterarAtendimentoProcesso(long codigo, Atendimento atendimento) {
		Atendimento atendimentoConsulta = atendimentoRepository.findById(codigo).get();
		BeanUtils.copyProperties(atendimento, atendimentoConsulta,"codigo","dtAtendimento", "grupo","pessoa","empresa", "assunto");
		return atendimentoRepository.save(atendimentoConsulta);
	}
	
	public List<Atendimento> listarAtendimentosPorPessoa(long pessoa) {
		List<Atendimento> lista = atendimentoRepository.findByPessoaCodigoOrderByCodigoDesc(pessoa);
		List<Atendimento> atendimentos = new ArrayList<Atendimento>();
		if(lista != null) {
			for(Atendimento atend: lista) {
				Atendimento atendimento = new Atendimento();
				atendimento.setCodigo(atend.getCodigo());
				atendimento.setPessoa(atend.getPessoa());
				atendimento.setProcesso(atend.getProcesso());
				atendimento.setGrupo(atend.getGrupo());
				atendimento.setAssunto(atend.getAssunto());
				atendimento.setEmpresa(atend.getEmpresa());
				atendimento.setDtAtendimento(DatasUtil.formatarDataTela(atend.getDtAtendimento()));
				atendimentos.add(atendimento);
			}
		}
		
		return atendimentos;
	}
}
