package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.CheckListAtividadeDto;
import com.br.ilawgestao.domains.dto.ExecucaoCheckListDto;
import com.br.ilawgestao.domains.dto.ExecucaoCheckListaCadastroDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.CheckListAtividade;
import com.br.ilawgestao.domains.models.ExecucaoCheckList;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.AtividadeRepository;
import com.br.ilawgestao.domains.repository.CheckListRepository;
import com.br.ilawgestao.domains.repository.ExecucaoCheckListRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class ExecucaoCheckListService {
	
	@Autowired
	private ExecucaoCheckListRepository execucaoRepository;
	
	@Autowired
	private AtividadeRepository atividadeRepository;
	
	@Autowired
	private CheckListRepository checkListRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public ExecucaoCheckListaCadastroDto cadastrar(ExecucaoCheckListaCadastroDto dto) {
		Optional<Atividade> atividade = atividadeRepository.findById(dto.getAtividade().getCodigo());
		if(!atividade.isPresent()) {
			throw new EntidadeNaoEncontradaException("Atividade não encontrada");
		}
		
		List<CheckListAtividadeDto> checkListsSalvos = new ArrayList<CheckListAtividadeDto>();
		
		if(dto.getCheckList() != null && dto.getCheckList().size() > 0) {
			for(CheckListAtividadeDto cl : dto.getCheckList()) {
				CheckListAtividade cla = new CheckListAtividade();
				cla.setCodigo(cl.getCodigo());
				ExecucaoCheckList exec = ExecucaoCheckList.builder()
						.atividade(atividade.get())
						.checkList(cla)
						.dataExecucao(null)
						.usuario(null)
						.status("A")
						.build();
				
				ExecucaoCheckList exs = execucaoRepository.save(exec);
				if(exs != null) {
					Optional<CheckListAtividade> check = checkListRepository.findById(cl.getCodigo());
					checkListsSalvos.add(CheckListAtividadeDto.build(check.get()));
				}
			}
		}
		
		ExecucaoCheckListaCadastroDto execucaoCheckListaCadastroDto = new ExecucaoCheckListaCadastroDto();
		execucaoCheckListaCadastroDto.setAtividade(dto.getAtividade());
		execucaoCheckListaCadastroDto.setCheckList(checkListsSalvos);
		return execucaoCheckListaCadastroDto;
	}
	
	public List<ExecucaoCheckListDto> consultarCheckList(long codigoAtividade) {
		Optional<Atividade> atividade = atividadeRepository.findById(codigoAtividade);
		if(!atividade.isPresent()) {
			throw new EntidadeNaoEncontradaException("Atividade não encontrada");
		}
		
		List<ExecucaoCheckList> lista = execucaoRepository.findByAtividadeCodigoOrderByCheckListOrdem(atividade.get().getCodigo());
		List<ExecucaoCheckListDto> dtos = new ArrayList<ExecucaoCheckListDto>();
		if(lista != null && lista.size() > 0) {
			for(ExecucaoCheckList cl : lista) {
				dtos.add(ExecucaoCheckListDto.buildConsulta(cl));
			}
		}
		
		return dtos;
	}
	
	public ExecucaoCheckListDto selecionarCheckList(ExecucaoCheckListDto dto) {
		Optional<Atividade> atividade = atividadeRepository.findById(dto.getAtividade().getCodigo());
		if(!atividade.isPresent()) {
			throw new EntidadeNaoEncontradaException("Atividade não encontrada");
		}
		
		Optional<CheckListAtividade> checkList = checkListRepository.findById(dto.getCheckList().getCodigo());
		if(!checkList.isPresent()) {
			throw new EntidadeNaoEncontradaException("Item de checklist não encontrado");
		}
		
		Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuario().getCodigo());
		if(!usuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		ExecucaoCheckList exec = ExecucaoCheckList.builder()
				.codigo(dto.getCodigo())
				.atividade(atividade.get())
				.checkList(checkList.get())
				.status(dto.getStatus())
				.dataExecucao(DatasUtil.getDataAtual())
				.usuario(usuario.get())
				.build();
		
		//Regra para desativar os anteriores, colocando para concluídos
		ExecucaoCheckListDto.build(execucaoRepository.save(exec));
		List<ExecucaoCheckList> listaAnterior = execucaoRepository.findByAtividadeCodigoAndCodigoLessThan(atividade.get().getCodigo(), dto.getCodigo());
		if(listaAnterior != null && listaAnterior.size() > 0) {
			for(ExecucaoCheckList la : listaAnterior) {
				ExecucaoCheckList execAnterior = ExecucaoCheckList.builder()
						.codigo(la.getCodigo())
						.atividade(atividade.get())
						.checkList(la.getCheckList())
						.status("C")
						.dataExecucao(DatasUtil.getDataAtual())
						.usuario(la.getUsuario() == null ? dto.getUsuario() : la.getUsuario())
						.build();
				execucaoRepository.save(execAnterior);
			}
		}
		
		return ExecucaoCheckListDto.build(execucaoRepository.save(exec));
	}
	
	public ExecucaoCheckListDto consultarExecucao(long codigo) {
		return ExecucaoCheckListDto.buildConsulta(execucaoRepository.findById(codigo).get());
	}
}
