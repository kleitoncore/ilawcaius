package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.br.ilawgestao.domains.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.AgrupamentoTarefaDto;
import com.br.ilawgestao.domains.dto.GrupoTarefaDto;
import com.br.ilawgestao.domains.dto.TarefaDto;
import com.br.ilawgestao.domains.dto.UsuarioTarefaDto;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.AgrupamentoTarefa;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTarefa;
import com.br.ilawgestao.domains.models.Tarefa;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.models.UsuarioTarefa;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class TarefaService {
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	@Autowired
	private GrupoTarefaRepository grupoRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private UsuarioTarefaRepository usuarioTarefaRepository;
	
	@Autowired
	private AgrupamentoTarefaRepository agrupamentoTarefaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public TarefaDto cadastrar(TarefaDto tarefaDto) {
		Optional<Empresa> empresa = empresaRepository.findById(tarefaDto.getEmpresa().getCodigo());
		if(!empresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		Tarefa tarefa = Tarefa.builder()
				.titulo(tarefaDto.getTitulo())
				.dataRegistro(DatasUtil.getDataAtual())
				.empresa(empresa.get())
				.usuario(tarefaDto.getUsuario())
				.prazo(tarefaDto.getPrazo())
				.snAtivo("S")
				.pontuacao(tarefaDto.getPontuacao())
				.tempo(tarefaDto.getTempo())
				.build();
		
		return TarefaDto.build(tarefaRepository.save(tarefa),null);
	}
	
	public List<TarefaDto> listar(long empresaCodigo) {
		Optional<Empresa> empresa = empresaRepository.findById(empresaCodigo);
		if(!empresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		List<Tarefa> entitys = tarefaRepository.findByEmpresaCodigoOrderByTitulo(empresa.get().getCodigo());
		List<TarefaDto> dtos = new ArrayList<TarefaDto>();
		if(entitys != null && entitys.size() > 0) {
			for(Tarefa tarefa : entitys) {
				List<UsuarioTarefa> usuariosTarefa = usuarioTarefaRepository.listarUsuariosTarefa(tarefa.getCodigo());
				dtos.add(TarefaDto.build1(tarefa,usuariosTarefa));
			}
		}
		
		return dtos;
	}
	
	public TarefaDto consultar(long codigo) {
		Optional<Tarefa> tarefa = tarefaRepository.findById(codigo);
		if(!tarefa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tarefa não encontrada");
		}
		List<UsuarioTarefa> usuariosTarefa = usuarioTarefaRepository.listarUsuariosTarefa(codigo);
		return TarefaDto.build1(tarefa.get(),usuariosTarefa);
	}
	
	public TarefaDto alterarTarefa(TarefaDto tarefaDto) {
		Tarefa tarefa = Tarefa.builder()
				.codigo(tarefaDto.getCodigo())
				.titulo(tarefaDto.getTitulo())
				.dataRegistro(tarefaDto.getDataRegistro())
				.empresa(tarefaDto.getEmpresa())
				.usuario(tarefaDto.getUsuario())
				.prazo(tarefaDto.getPrazo())
				.pontuacao(tarefaDto.getPontuacao())
				.tempo(tarefaDto.getTempo())
				.snAtivo(tarefaDto.getSnAtivo())
				.build();
		
		return TarefaDto.build(tarefaRepository.save(tarefa),null);
	}
	
	public void excluirTarefa(long codigo) {
		List<UsuarioTarefa> usuariosTarefa = usuarioTarefaRepository.listarUsuariosTarefa(codigo);
		if(usuariosTarefa != null && usuariosTarefa.size() > 0) {
			throw new EntidadeEmUsoException("Tarefa não pode ser excluída! possui usuários designados");
		}
		tarefaRepository.deleteById(codigo);
	}
	
	public List<Usuario> listarUsuariosNaoCadastrados(long tarefa, long empresa) {
		return usuarioTarefaRepository.listarUsuariosNaoCadastrados(tarefa, empresa);
	}
	
	public AgrupamentoTarefaDto adicionarTerefaAoGrupo(AgrupamentoTarefaDto agrupaDto) {
		Optional<GrupoTarefa> grupo = grupoRepository.findById(agrupaDto.getGrupo().getCodigo());
		if(!grupo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Tarefa não encontrado");
		}
		
		Optional<Tarefa> tarefa = tarefaRepository.findById(agrupaDto.getTarefa().getCodigo());
		if(!tarefa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tarefa não encontrada");
		}
		
		AgrupamentoTarefa agrupa = AgrupamentoTarefa.builder()
				.grupo(grupo.get())
				.tarefa(tarefa.get())
				.build();
		return AgrupamentoTarefaDto.build(agrupamentoTarefaRepository.save(agrupa));
	}
	
	public List<GrupoTarefaDto> listarGruposPorTarefa(long tarefa) {
		List<GrupoTarefa> entitys = grupoRepository.listarGruposPorTarefa(tarefa);
		List<GrupoTarefaDto> dtos = new ArrayList<GrupoTarefaDto>();
		if(entitys != null && entitys.size() > 0) {
			for(GrupoTarefa grupo : entitys) {
				dtos.add(GrupoTarefaDto.build(grupo, null));
			}
		}
		
		return dtos;
	}
	
	public List<GrupoTarefaDto> listarGruposSemTarefa(long tarefa, long empresa) {
		List<GrupoTarefa> entitys = grupoRepository.listarGruposSemTarefa(tarefa, empresa);
		List<GrupoTarefaDto> dtos = new ArrayList<GrupoTarefaDto>();
		if(entitys != null && entitys.size() > 0) {
			for(GrupoTarefa grupo : entitys) {
				dtos.add(GrupoTarefaDto.build(grupo, null));
			}
		}
		
		return dtos;
	}
	
	public void excluirAgrupamento(long tarefa, long grupo) {
		Optional<AgrupamentoTarefa> agrupa = agrupamentoTarefaRepository.findByTarefaCodigoAndGrupoCodigo(tarefa, grupo);
		if(!agrupa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Agrupamento não econtrado");
		}
		//Verifica se este Agrupamento está já associado com algum usuário responsável de tarefa, se sim, exclui o usuário da tarefa
		List<UsuarioTarefa> usuariosTarefa = usuarioTarefaRepository.findByAgrupamentoCodigo(agrupa.get().getCodigo());
		if(usuariosTarefa != null && usuariosTarefa.size() > 0) {
			for(UsuarioTarefa usu : usuariosTarefa) {
				usuarioTarefaRepository.deleteById(usu.getCodigo());
			}
		}
		agrupamentoTarefaRepository.deleteById(agrupa.get().getCodigo());
	}
	
	public AgrupamentoTarefaDto consultarAgrupamento(long tarefa, long grupo) {
		Optional<AgrupamentoTarefa> entity = agrupamentoTarefaRepository.findByTarefaCodigoAndGrupoCodigo(tarefa, grupo);
		if(!entity.isPresent()) {
			throw new EntidadeNaoEncontradaException("Agrupamento de tarefa não encontrado");
		}
		
		AgrupamentoTarefaDto dto = AgrupamentoTarefaDto.build(entity.get());
		return dto;
	}
	
	public List<TarefaDto> listarTarefasSemGrupo(long grupo, long empresa) {
		List<Tarefa> entitys = tarefaRepository.listarTarefasSemGrupo(grupo, empresa);
		List<TarefaDto> dtos = new ArrayList<TarefaDto>();
		if(entitys != null && entitys.size() > 0) {
			for(Tarefa tarefa : entitys) {
				dtos.add(TarefaDto.build(tarefa, null));
			}
		}
		
		return dtos;
	}
	
	public UsuarioTarefaDto adicionarUsuarioTarefa(UsuarioTarefaDto dto) {
		Optional<AgrupamentoTarefa> agrupamento = agrupamentoTarefaRepository.findByTarefaCodigoAndGrupoCodigo(dto.getAgrupamento().getTarefa().getCodigo(), 
				dto.getAgrupamento().getGrupo().getCodigo());
		if(!agrupamento.isPresent()) {
			throw new EntidadeNaoEncontradaException("Agrupamento de tarefa não encontrado");
		}
		
		UsuarioTarefa usuarioTarefa = UsuarioTarefa.builder()
				.agrupamento(agrupamento.get())
				.usuario(dto.getUsuario())
				.tipo("R")
				.build();
		
		return UsuarioTarefaDto.build(usuarioTarefaRepository.save(usuarioTarefa));	
	}
	
	public List<UsuarioTarefaDto> listarusuariosTarefas(long tarefa) {
		List<UsuarioTarefa> entitys = usuarioTarefaRepository.listarUsuariosTarefa(tarefa);
		List<UsuarioTarefaDto> dtos = new ArrayList<UsuarioTarefaDto>();
		if(entitys != null && entitys.size() > 0) {
			for(UsuarioTarefa usuTa : entitys) {
				dtos.add(UsuarioTarefaDto.build(usuTa));
			}
		}
		
		return dtos;
	}
	
	public void excluirUsuarioTarefa(long codigo) {
		usuarioTarefaRepository.deleteById(codigo);
	}

	public List<TarefaDto> listarTarefasPorGrupo(long grupoTrabalho, long grupoAtividade) {
		List<Tarefa> entitys = tarefaRepository.listarTarefasPorGrupo(grupoAtividade);
		List<TarefaDto> dtos = new ArrayList<TarefaDto>();
		if(entitys != null && entitys.size() > 0) {
			for(Tarefa tarefa : entitys) {
				Optional<Usuario> responsavel = usuarioRepository.consultarUsuarioRodizioPrimeiroRanking(grupoTrabalho,
						grupoAtividade,tarefa.getCodigo());
				if(responsavel.isPresent()) {
					dtos.add(TarefaDto.build(tarefa, responsavel.get()));
				} else {
					dtos.add(TarefaDto.build(tarefa, null));
				}
			}
		}

		return dtos;
	}

	public List<TarefaDto> listarTarefasPorGrupoAtividadeEGrupoTrabalho(long grupoAtividade, long grupoTrabalho) {
		List<Tarefa> entitys = tarefaRepository.listarTarefasPorGrupoTrabalhoEGrupoAtividade(grupoAtividade,grupoTrabalho);
		List<TarefaDto> dtos = new ArrayList<TarefaDto>();
		if(entitys != null && entitys.size() > 0) {
			for(Tarefa tarefa : entitys) {
				Optional<Usuario> responsavel = usuarioRepository.consultarUsuarioRodizioPrimeiroRanking(grupoTrabalho,
						grupoAtividade,tarefa.getCodigo());
				if(responsavel.isPresent()) {
					dtos.add(TarefaDto.build(tarefa, responsavel.get()));
				} else {
					dtos.add(TarefaDto.build(tarefa, null));
				}
			}
		}

		return dtos;
	}
}
