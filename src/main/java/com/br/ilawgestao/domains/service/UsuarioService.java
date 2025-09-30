package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.UsuarioDto;
import com.br.ilawgestao.domains.dto.UsuarioShortDto;
import com.br.ilawgestao.domains.exception.EmailJaCadastradoException;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.exception.UsuarioOuSenhaNaoConferemExpetion;
import com.br.ilawgestao.domains.models.GrupoCliente;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.LogUsuario;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.GrupoClienteRepository;
import com.br.ilawgestao.domains.repository.GrupoTrabalhoRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private LogUsuarioService serviceLog;
	@Autowired
	private GrupoTrabalhoRepository grupoTrabalhoRepository;
	@Autowired
	private GrupoClienteRepository grupoClienteRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Usuario autenticarUsuario(UsuarioDto usuario) {
		
		Optional<Usuario> usuarioConsulta = usuarioRepository.findByEmail(usuario.getEmail());
		if(!usuarioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Email não enconntrado");
		}
		
		boolean confereSenha = passwordEncoder.matches(usuario.getSenha(), usuarioConsulta.get().getSenha());
		
		if(!confereSenha) {
			throw new UsuarioOuSenhaNaoConferemExpetion("Senha não confere");
		}
		
		//Log de Autenticalçao
		LogUsuario log = new LogUsuario();
		log.setUsuario(usuarioConsulta.get());
		log.setTipoLog("E"); //Entrada
		inserirLogUsuario(log);
		
		return usuarioConsulta.get();
	}
	
	public void inserirLogUsuario(LogUsuario log) {
		log.setDataLog(DatasUtil.getDataHoraAtual());
		serviceLog.cadastrarLogusuario(log);
	}
	
	public Usuario cadastrarUsuario(Usuario usuario) {
		if(usuarioRepository.existsByEmail(usuario.getEmail())) {
			throw new EmailJaCadastradoException("Email já em uso por outro usuário");
		}
		
		String senha = "abc123"; // Senha padrão de criação
		usuario.setSenha(passwordEncoder.encode(senha));
		//usuario.setSenha(Criptografar.encriptografar(senha));
		//Data Atual
		usuario.setDataCadastro(DatasUtil.getDataAtual());
		//Entra como ativo
		usuario.setSituacao((long)0);
		//Status do Login - Criado
		usuario.setStatusLogin("C");
		
		return usuarioRepository.save(usuario);		
	}
	
	public Usuario consultarUsuario(Long codigo) {
		Optional<Usuario> usuario = usuarioRepository.findById(codigo);
		if(!usuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		return usuario.get();
	}
	
	public List<Usuario> listarUsuarios(long empresa, String nome) {
		return usuarioRepository.listarUsuarioPorNome(empresa,nome);
	}
	
	public Usuario alterarUsuario(Long codigo, Usuario usuario) {
		Usuario usuarioConsulta = consultarUsuario(codigo);
		BeanUtils.copyProperties(usuario, usuarioConsulta,"codigo","dataCadastro", "statusLogin","senha","empresa");
		return usuarioRepository.save(usuarioConsulta);
	}
	
	/**
	 * Lista usuário que ainda não esteja associados a determinados grupos de trabalho
	 * @param grupo
	 * @return
	 */
	public List<Usuario> listarUsuarioSemGrupo(long grupo, long empresa) {
		Optional<GrupoTrabalho> grupoConsulta = grupoTrabalhoRepository.findById(grupo);
		if(!grupoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Trabalho não encontrado");
		}
		return usuarioRepository.listarUsuariosSemGrupo(grupo, empresa);
	}
	
	/**
	 * Lista usuarios que ainda não esatão associados a um Grupo de Clientes
	 * @param grupo
	 * @param empresa
	 * @return
	 */
	public List<Usuario> listarUsuariosSemGruposPessoas(long grupo, long empresa) {
		Optional<GrupoCliente> grupoConsulta = grupoClienteRepository.findById(grupo);
		if(!grupoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Grupo de Cliente não encontrado");
		}
		
		return usuarioRepository.listarUsuariosSemGruposPessoa(grupo, empresa);
	}
	
	public void excluirUsuario(Long codigo) {
		try {
			usuarioRepository.deleteById(codigo);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException("Usuário não pode ser excluído, já está sendo utilizado");
		}
	}
	
	public List<Usuario> listarUsuariosPorEmpresa(long empresa) {
		return usuarioRepository.findByEmpresaCodigoOrderByNome(empresa);
	}
	
	public Usuario alterarSenha(long codigo, String senha, String novaSenha, String confirmacao, Usuario usuario) {
		Usuario usuarioConsulta = consultarUsuario(codigo);
		
		boolean confereSenha = passwordEncoder.matches(senha, usuarioConsulta.getSenha());
		
		if(!confereSenha) {
			throw new UsuarioOuSenhaNaoConferemExpetion("Senha atual não confere");
		}
		
		boolean confereSenhaNova = passwordEncoder.matches(novaSenha,passwordEncoder.encode(confirmacao));
		
		if(!confereSenhaNova) {
			throw new UsuarioOuSenhaNaoConferemExpetion("Confirmação de nova senha inválida");
		}
		
		usuario.setSenha(passwordEncoder.encode(novaSenha));
		
		BeanUtils.copyProperties(usuario, usuarioConsulta,"codigo","nome","email","cpf","situacao","endereco","numero","complemento","cep",  
				"bairro","cidade","telefone1","telefone2","dataCadastro","empresa","perfil","foto","statusLogin",
				"dataCadastro", "statusLogin","empresa");
		
		return usuarioRepository.save(usuarioConsulta);
	}
	
	public List<UsuarioShortDto> listarShort(long empresa) {
		List<Usuario> usuarios = usuarioRepository.findByEmpresaCodigoOrderByNome(empresa);
		List<UsuarioShortDto> dtos = new ArrayList<UsuarioShortDto>();
		if(usuarios != null && usuarios.size() > 0) {
			for(Usuario usu : usuarios) {
				dtos.add(UsuarioShortDto.build(usu));
			}
		}
		
		return dtos;
	}
}
