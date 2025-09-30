package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.AtividadeCadastroDTO;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.AtividadeUsuario;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.ProcessoImportancia;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.AtividadeRepository;
import com.br.ilawgestao.domains.repository.AtividadeUsuarioRepository;
import com.br.ilawgestao.domains.repository.ProcessoImportanciaRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;

@Service
public class ProcessoImportanciaService {
	
	@Autowired
	private ProcessoImportanciaRepository importanciaRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private AtividadeRepository atividadeRepository;
	
	@Autowired
	private AtividadeUsuarioRepository atividadeUsuarioRepository;
	
	@Autowired
	private AtividadeService atividadeService;
	
	public ProcessoImportancia incluirProcesso(ProcessoImportancia processo) {
		Optional<Processo> processoConsulta = processoRepository.findById(processo.getProcesso().getCodigo());
		if(!processoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		Optional<Usuario> usuarioConsulta = usuarioRepository.findById(processo.getUsuario().getCodigo());
		if(!usuarioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encntrado");
		}
		
		ProcessoImportancia processoImportanciaSalvo = importanciaRepository.save(processo);
		if(processoImportanciaSalvo != null) {
			//Manutenção em Atividade Short
			List<Atividade> atividades = atividadeRepository.findByProcessoCodigoOrderByDtLimiteDesc(processoImportanciaSalvo.getProcesso().getCodigo());
			if(atividades != null) {
				List<Usuario> responsaveis = new ArrayList<Usuario>();
				List<Usuario> interessados = new ArrayList<Usuario>();
				for(Atividade ativ : atividades) {
					List<AtividadeUsuario> atividadeUsuarios = atividadeUsuarioRepository.findByAtividadeCodigo(ativ.getCodigo());
					for(AtividadeUsuario au : atividadeUsuarios) {
						if(au.getTipo().equals("R")) {
							responsaveis.add(au.getUsuario());
						} else {
							interessados.add(au.getUsuario());
						}
					}
					AtividadeCadastroDTO dtoAtividade = new AtividadeCadastroDTO();
					dtoAtividade.setAtividade(ativ);
					dtoAtividade.setResponsaveis(responsaveis);
					dtoAtividade.setInteressados(interessados);
					atividadeService.excluirAtividadesShort(ativ);
					List<Atividade> atividadesImp = new ArrayList<Atividade>();
					atividadesImp.add(dtoAtividade.getAtividade());
					atividadeService.incluirAtividadeShort(dtoAtividade,atividadesImp);
				}
			}
		}
		
		return processoImportanciaSalvo;
	}
	
	public ProcessoImportancia consultarProcessoImportancia(long usuario, long processo) {
		return importanciaRepository.findProcessoImportanciaByUsuarioCodigoAndProcessoCodigo(usuario, processo);
	}
	
	public void excluirProcessoImportancia(long codigo) {
		this.importanciaRepository.deleteById(codigo);
	}
}
