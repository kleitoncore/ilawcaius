package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.GrupoTarefaDto;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTarefa;
import com.br.ilawgestao.domains.models.Tarefa;
import com.br.ilawgestao.domains.repository.EmpresaRepository;
import com.br.ilawgestao.domains.repository.GrupoTarefaRepository;
import com.br.ilawgestao.domains.repository.TarefaRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class GrupoTarefaService {
	
	@Autowired
	private GrupoTarefaRepository grupoRepository;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	public GrupoTarefaDto cadastrar(GrupoTarefaDto dto) {
		Optional<Empresa> empresa = empresaRepository.findById(dto.getEmpresa().getCodigo());
		if(!empresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		GrupoTarefa grupo = GrupoTarefa.builder()
				.nome(dto.getNome())
				.empresa(dto.getEmpresa())
				.dataRegistro(DatasUtil.getDataAtual())
				.usuario(dto.getUsuario())
				.snAtivo("A")
				.build();
		return GrupoTarefaDto.build(grupoRepository.save(grupo),null);
	}
	
	public List<GrupoTarefaDto> listar(long empresa) {
		List<GrupoTarefa> entitysGrupo = grupoRepository.findByEmpresaCodigoOrderByNome(empresa);
		List<GrupoTarefaDto> dtos = new ArrayList<>();
		if(entitysGrupo != null && entitysGrupo.size() > 0) {
			for(GrupoTarefa grupo : entitysGrupo) {
				List<Tarefa> tarefas = tarefaRepository.listarTarefasPorGrupo(grupo.getCodigo());
				dtos.add(GrupoTarefaDto.build(grupo, tarefas));
			}
		}
		
		return dtos;
	}
	
	public GrupoTarefaDto consultar(long codigo) {
		Optional<GrupoTarefa> entity = grupoRepository.findById(codigo);
		if(!entity.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de tarefa não encontrado");
		}
		List<Tarefa> tarefas = tarefaRepository.listarTarefasPorGrupo(codigo);
		return GrupoTarefaDto.build(entity.get(), tarefas);
	}
	
	public GrupoTarefaDto alterar(GrupoTarefaDto dto) {
		GrupoTarefaDto consulta = this.consultar(dto.getCodigo());
		GrupoTarefa grupo = GrupoTarefa.builder()
				.codigo(consulta.getCodigo())
				.nome(dto.getNome())
				.empresa(dto.getEmpresa())
				.dataRegistro(DatasUtil.getDataAtual())
				.usuario(dto.getUsuario())
				.snAtivo(dto.getSnAtivo())
				.build();
		return GrupoTarefaDto.build(grupo, null);
	}
	
	public void excluir(long codigo) {
		List<Tarefa> tarefas = tarefaRepository.listarTarefasPorGrupo(codigo);
		if(tarefas != null && tarefas.size() > 0) {
			throw new EntidadeEmUsoException("Atenção! Grupo de Tarefa não pode ser excluído, existem tarefas associadas");
		}
		grupoRepository.deleteById(codigo);
	}
}
