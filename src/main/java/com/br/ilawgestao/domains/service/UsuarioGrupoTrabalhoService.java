package com.br.ilawgestao.domains.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeNaoCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.models.UsuarioGrupoTrabalho;
import com.br.ilawgestao.domains.repository.GrupoTrabalhoRepository;
import com.br.ilawgestao.domains.repository.UsuarioGrupoTrabalhoRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;

@Service
public class UsuarioGrupoTrabalhoService {
	
	@Autowired
	private UsuarioGrupoTrabalhoRepository repository;
	
	@Autowired
	private GrupoTrabalhoRepository grupoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public UsuarioGrupoTrabalho cadastrarUsuarioGrupoTrabalho(UsuarioGrupoTrabalho usuarioGrupo) {
		Optional<GrupoTrabalho> grupoBusca = grupoRepository.findById(usuarioGrupo.getGrupo().getCodigo());
		if(!grupoBusca.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de trabalho não encontrado");
		}
		
		Optional<Usuario> usuarioBusca = usuarioRepository.findById(usuarioGrupo.getUsuario().getCodigo());
		if(!usuarioBusca.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		Optional<UsuarioGrupoTrabalho> usuarioGrupoExiste = repository.existeUsuarioGrupo(usuarioGrupo.getGrupo().getCodigo(), 
				usuarioGrupo.getUsuario().getCodigo());
		if(usuarioGrupoExiste.isPresent()) {
			throw new EntidadeEmUsoException("Usuário já está cadastrado para este Grupo de Trabalho");
		}
		
		return repository.save(usuarioGrupo);
	}
	
	/**
	 * Adiciona todos os grupos de trabalho ao usuário
	 */
	public void adicionarTodosGruposTrabalho(Usuario usuario) {
		Optional<Usuario> usuarioBusca = usuarioRepository.findById(usuario.getCodigo());
		if(!usuarioBusca.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		int cadastrou = 0;
		
		List<GrupoTrabalho> grupos = grupoRepository.findGrupoTrabalhoByEmpresaOrderByNomeAsc(usuario.getEmpresa());
		if(grupos != null) {
			for(GrupoTrabalho grupo : grupos) {
				UsuarioGrupoTrabalho usuarioGrupo = new UsuarioGrupoTrabalho();
				usuarioGrupo.setUsuario(usuario);
				usuarioGrupo.setGrupo(grupo);
				if(!existeUsduarioGrupo(usuarioGrupo)) {
					repository.save(usuarioGrupo);
					cadastrou++;
				}
			}
		}
		
		if(cadastrou == 0) {
			throw new EntidadeNaoCadastradaException("Atenção! Usuário selecionado possui Grupo(s) de Trabalho já cadastrado(s)");
		}
	}
	
	/**
	 * Lista todos os usuário de um determinado grupo
	 * @param grupo
	 * @return
	 */
	public List<UsuarioGrupoTrabalho> listarUsuariosPorGrupo(GrupoTrabalho grupo) {
		Optional<GrupoTrabalho> grupoBusca = grupoRepository.findById(grupo.getCodigo());
		if(!grupoBusca.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrado");
		}
		
		return repository.listarUsuariosPorGrupo(grupo.getCodigo());
	}
	
	/**
	 * Lista os grupos de trabalho por um determinado usuário
	 * @param usuario
	 * @return
	 */
	public List<UsuarioGrupoTrabalho> listarGruposPorusuario(Usuario usuario) {
		Optional<Usuario> usuarioBusca = usuarioRepository.findById(usuario.getCodigo());
		if(!usuarioBusca.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		return repository.listarGruposPorUsuario(usuario.getCodigo());
	}
	
	public void excluirUsuarioGrupoTrabalho(long codigo) {
		Optional<UsuarioGrupoTrabalho> usuarioGrupoBusca = repository.findById(codigo);
		if(!usuarioGrupoBusca.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário Grupo de Trabalho não encontrado");
		}
		
		repository.deleteById(codigo);
	}
	
	public List<Usuario> listarUsuariosAtividades(long grupo) {
		Optional<GrupoTrabalho> grupoConsulta = grupoRepository.findById(grupo);
		if(!grupoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrado");
		}
		
		return repository.listarUsuariosGrupo(grupo);
	}

	public List<Usuario> listarUsuariosAtividadesAtivos(long grupo) {
		Optional<GrupoTrabalho> grupoConsulta = grupoRepository.findById(grupo);
		if(!grupoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrado");
		}

		return repository.listarUsuariosGrupoAtivos(grupo);
	}
	
	private boolean existeUsduarioGrupo(UsuarioGrupoTrabalho ug) {
		Optional<UsuarioGrupoTrabalho> usuarioGrupo = repository.existeUsuarioGrupo(ug.getGrupo().getCodigo(), ug.getUsuario().getCodigo());
		if(usuarioGrupo.isPresent()) {
			return true;
		}
		return false;
	}
}
