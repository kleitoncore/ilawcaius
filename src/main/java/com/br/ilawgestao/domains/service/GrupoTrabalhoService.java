package com.br.ilawgestao.domains.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeJaCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.models.UsuarioGrupoTrabalho;
import com.br.ilawgestao.domains.repository.AtividadeRepository;
import com.br.ilawgestao.domains.repository.GrupoTrabalhoRepository;
import com.br.ilawgestao.domains.repository.UsuarioGrupoTrabalhoRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;

@Service
public class GrupoTrabalhoService {
	
	@Autowired
	private GrupoTrabalhoRepository grupoTrabalhoRepository;
	
	@Autowired
	private AtividadeRepository atividadeRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioGrupoTrabalhoRepository usuarioGrupoRepository;
	
	public GrupoTrabalho consultarGrupoTrabalhoPorCodigo(long codigo) {
		Optional<GrupoTrabalho> grupo = grupoTrabalhoRepository.findById(codigo);
		if(!grupo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrado");
		}
		
		return grupo.get();
	}

	public List<GrupoTrabalho> consultarGruposTrabalhosAtivos(long empresa) {
		return grupoTrabalhoRepository.listarGruposTrabalhosAtivos(empresa);
	}
	
	public List<GrupoTrabalho> consultarGrupoTrabalhoPorNome(String nome, Empresa empresa) {
		return grupoTrabalhoRepository.listarGruposTrabalho(nome, empresa);
	}
	
	public GrupoTrabalho cadastrarGrupoTrabalho(GrupoTrabalho grupoTrabalho) {
		Optional<GrupoTrabalho> grupo = grupoTrabalhoRepository.consultarGrupoTrabalhoPoNome(grupoTrabalho.getNome(), 
				grupoTrabalho.getEmpresa().getCodigo());
		
		if(grupo.isPresent()) {
			throw new EntidadeJaCadastradaException("Grupo de Trabalho já cadastrado com este nome");
		}
		
		GrupoTrabalho grupoCadastrado = grupoTrabalhoRepository.save(grupoTrabalho);
		grupoCadastrado.setGrupoPai(grupoCadastrado.getCodigo());
		alterarGrupoTrabalho(grupoCadastrado.getCodigo(), grupoCadastrado);
		
		if(grupoCadastrado != null) {
			//Criar sub-Grupos
			this.cadastrarSubGruposAutomaticos(grupoCadastrado);
			//Perfil dos Administradores
			List<Usuario> administradores = usuarioRepository.findByPerfilCodigoAndEmpresaCodigo(1L,grupoTrabalho.getEmpresa().getCodigo());
			if(administradores != null) {
				for(Usuario adm: administradores) {
					UsuarioGrupoTrabalho usuarioGrupoTrabalho = new UsuarioGrupoTrabalho();
					usuarioGrupoTrabalho.setUsuario(adm);
					usuarioGrupoTrabalho.setGrupo(grupoCadastrado);
					usuarioGrupoRepository.save(usuarioGrupoTrabalho);
				}
			}
		}
		
		return grupoCadastrado;
	}
	
	private void cadastrarSubGruposAutomaticos(GrupoTrabalho grupoPai) {
		GrupoTrabalho adm = new GrupoTrabalho();
		adm.setNome("Administrativo");
		adm.setGrupoPai(grupoPai.getCodigo());
		adm.setEmpresa(grupoPai.getEmpresa());
		grupoTrabalhoRepository.save(adm);
		
		GrupoTrabalho prazos = new GrupoTrabalho();
		prazos.setNome("Prazos");
		prazos.setGrupoPai(grupoPai.getCodigo());
		prazos.setEmpresa(grupoPai.getEmpresa());
		grupoTrabalhoRepository.save(prazos);
		
		GrupoTrabalho agenda = new GrupoTrabalho();
		agenda.setNome("Agenda");
		agenda.setGrupoPai(grupoPai.getCodigo());
		agenda.setEmpresa(grupoPai.getEmpresa());
		grupoTrabalhoRepository.save(agenda);
		
		GrupoTrabalho financeiro = new GrupoTrabalho();
		financeiro.setNome("Financeiro");
		financeiro.setGrupoPai(grupoPai.getCodigo());
		financeiro.setEmpresa(grupoPai.getEmpresa());
		grupoTrabalhoRepository.save(financeiro);
	}
	
	public GrupoTrabalho consultarSubGrupoNome(String nome, long grupoPai) {
		return grupoTrabalhoRepository.findGrupoTrabalhoByNomeAndGrupoPai(nome, grupoPai).get(); 
	}
	
	public GrupoTrabalho cadastrarSubGrupoTrabalho(GrupoTrabalho subGrupo) {
		Optional<GrupoTrabalho> grupoConsulta = grupoTrabalhoRepository.findGrupoTrabalhoByNomeAndGrupoPai(subGrupo.getNome(), subGrupo.getGrupoPai());
		if(grupoConsulta.isPresent()) {
			throw new EntidadeJaCadastradaException("Já existe um Sub-Grupo de Trabalho cadastrado com este nome");
		}
		
		Optional<GrupoTrabalho> grupoPai = grupoTrabalhoRepository.findById(subGrupo.getGrupoPai());
		if(!grupoPai.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho Pai não encontrado");
		}
		
		return grupoTrabalhoRepository.save(subGrupo);
	}
	
	public GrupoTrabalho alterarGrupoTrabalho(long codigo, GrupoTrabalho grupoTrabalho) {
		GrupoTrabalho grupoTrabalhoSalvo = consultarGrupoTrabalhoPorCodigo(codigo);
		BeanUtils.copyProperties(grupoTrabalho, grupoTrabalhoSalvo,"codigo");
		return grupoTrabalhoRepository.save(grupoTrabalhoSalvo);
	}
	
	public List<GrupoTrabalho> listarGruposSemUsuario(long empresa, long usuario) {
		return grupoTrabalhoRepository.listarGruposSemUsuarios(empresa, usuario);
	}
	
	public void excluirGrupoTrabalho(long codigo) {
		Optional<GrupoTrabalho> grupoTrabalhoSalvo = grupoTrabalhoRepository.findById(codigo);
		if(!grupoTrabalhoSalvo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrado");
		}
		
		List<GrupoTrabalho> gruposFilho = this.listarSubGrupos(grupoTrabalhoSalvo.get().getGrupoPai());
		if(!gruposFilho.isEmpty()) {
			throw new EntidadeEmUsoException("Grupo de Trabalho não pode ser excluído, grupo(s) filhos existente(s)");
		}
		
		grupoTrabalhoRepository.deleteById(codigo);
	}
	
	public void excluirSubGrupo(long codigo) {
		Optional<GrupoTrabalho> grupoTrabalhoSalvo = grupoTrabalhoRepository.findById(codigo);
		if(!grupoTrabalhoSalvo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrado");
		}
		
		List<Atividade> atividades = atividadeRepository.findBySubGrupoCodigo(codigo);
		if(!atividades.isEmpty()) {
			throw new EntidadeEmUsoException("Sub-Grupo de Trabalho não pode ser excluído, atividades existente(s)");
		}
		
		grupoTrabalhoRepository.deleteById(codigo);
		
	}
	
	public List<GrupoTrabalho> listarGruposUsuario(long usuario) {
		return grupoTrabalhoRepository.listarGruposTrabalhoUsuario(usuario);
	}
	
	public List<GrupoTrabalho> listarGruposUsuario2(long usuario) {
		return grupoTrabalhoRepository.listarGruposTrabalhoUsuario2(usuario);
	}
	
	public List<GrupoTrabalho> listarSubGrupos(long grupoPai) {
		return grupoTrabalhoRepository.listarSubGrupos(grupoPai);
	}
	
	public List<GrupoTrabalho> consultarGrupsTrabalhoPorCodigos(String codigos) {
		return grupoTrabalhoRepository.consultarGruposPorCodigos(codigos);
	}
}
