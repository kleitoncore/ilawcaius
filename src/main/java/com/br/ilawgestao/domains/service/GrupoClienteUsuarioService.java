package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeJaCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.GrupoCliente;
import com.br.ilawgestao.domains.models.GrupoClienteUsuario;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.GrupoClienteRepository;
import com.br.ilawgestao.domains.repository.GrupoClienteusuarioRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;

@Service
public class GrupoClienteUsuarioService {
	
	@Autowired
	private GrupoClienteusuarioRepository repository;
	
	@Autowired
	private GrupoClienteRepository grupoClienteRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public GrupoClienteUsuario cadastrarGrupoClienteUsuario(GrupoClienteUsuario grupoClienteUsuario) {
		Optional<GrupoCliente> grupo = grupoClienteRepository.findById(grupoClienteUsuario.getGrupo().getCodigo());
		if(!grupo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Cliente não encontrado");
		}
		
		Optional<Usuario> usuario = usuarioRepository.findById(grupoClienteUsuario.getUsuario().getCodigo());
		if(!usuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		//Verifica se a associação entre Grupo de cliente e usuário já existe
		Optional<GrupoClienteUsuario> grupoUsuarioConsulta = repository.consultarUsuarioGrupo(grupoClienteUsuario.getUsuario().getCodigo(), 
				grupoClienteUsuario.getGrupo().getCodigo());
		if(grupoUsuarioConsulta.isPresent()) {
			throw new EntidadeJaCadastradaException("Usuário já cadastrado pare este grupo");
		}
		
		return repository.save(grupoClienteUsuario);
	}
	
	public List<GrupoCliente> listarGruposPorUsuario(long usuario) {
		Optional<Usuario> usuarioConsulta = usuarioRepository.findById(usuario);
		if(!usuarioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		List<GrupoClienteUsuario> lista = repository.consultarGruposPorUsuario(usuario);
		List<GrupoCliente> grupoClienteLista = new ArrayList<GrupoCliente>();
		if(lista != null && !lista.isEmpty()) {
			for(GrupoClienteUsuario gcu : lista) {
				GrupoCliente gc = new GrupoCliente();
				gc.setCodigo(gcu.getGrupo().getCodigo());
				gc.setNome(gcu.getGrupo().getNome());
				gc.setDescricao(gcu.getGrupo().getDescricao());
				gc.setEmpresa(gcu.getGrupo().getEmpresa());
				grupoClienteLista.add(gc);
			}
		}
		
		return grupoClienteLista;
	}
	
	public List<GrupoCliente> listarGruposConsulta(long usuario) {
		return repository.listarGruposUsuario(usuario);
	}
	
	public List<GrupoClienteUsuario> listarusuarioPorGrupo(long grupo) {
		Optional<GrupoCliente> grupoConsulta = grupoClienteRepository.findById(grupo);
		if(!grupoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Cliente não encontrado");
		}
		
		return repository.consultarUsuariosPorGrupo(grupo);
	}
	
	public void excluirGrupoClienteusuario(long codigo) {
		repository.deleteById(codigo);
	}
}
