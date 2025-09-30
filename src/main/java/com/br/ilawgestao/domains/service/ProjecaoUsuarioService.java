package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.ProjecaoUsuarioDto;
import com.br.ilawgestao.domains.dto.UsuarioDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.ProjecaoUsuario;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.ProjecaoUsuarioRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.repository.filtros.FiltroProjecaoUsuario;

@Service
public class ProjecaoUsuarioService {
	
	@Autowired
	private ProjecaoUsuarioRepository projecaoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public ProjecaoUsuarioDto cadastrar(ProjecaoUsuarioDto dto) {
		Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuario().getCodigo());
		if(!usuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		ProjecaoUsuario proj = ProjecaoUsuario.builder()
				.usuario(usuario.get())
				.ano(dto.getAno())
				.mes(dto.getMes())
				.pontos(dto.getPontos())
				.empresa(dto.getEmpresa())
				.build();
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setCodigo(usuario.get().getCodigo());
		usuarioDto.setNome(usuario.get().getNome());
		return ProjecaoUsuarioDto.build(projecaoRepository.save(proj),usuarioDto);
	}
	
	public List<ProjecaoUsuarioDto> listar(FiltroProjecaoUsuario filtro) {
		List<ProjecaoUsuario> entitys = projecaoRepository.consultarProjecoesUsuario(filtro);
		List<ProjecaoUsuarioDto> dtos = new ArrayList<ProjecaoUsuarioDto>();
		if(entitys != null && entitys.size() > 0) {
			for(ProjecaoUsuario proj : entitys) {
				UsuarioDto usuarioDto = new UsuarioDto();
				usuarioDto.setCodigo(proj.getUsuario().getCodigo());
				usuarioDto.setNome(proj.getUsuario().getNome());
				dtos.add(ProjecaoUsuarioDto.build(proj, usuarioDto));
			}
		}
		
		return dtos;
	}
	
	public ProjecaoUsuarioDto alterar(ProjecaoUsuarioDto dto) {
		Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuario().getCodigo());
		if(!usuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		ProjecaoUsuario proj = ProjecaoUsuario.builder()
				.codigo(dto.getCodigo())
				.usuario(usuario.get())
				.ano(dto.getAno())
				.mes(dto.getMes())
				.pontos(dto.getPontos())
				.empresa(dto.getEmpresa())
				.build();
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setCodigo(usuario.get().getCodigo());
		usuarioDto.setNome(usuario.get().getNome());
		return ProjecaoUsuarioDto.build(projecaoRepository.save(proj),usuarioDto);
	}
	
	public void excluir(long codigo) {
		Optional<ProjecaoUsuario> projecao = projecaoRepository.findById(codigo);
		if(!projecao.isPresent()) {
			throw new EntidadeNaoEncontradaException("Registro não encontrado");
		}
		
		projecaoRepository.deleteById(codigo);
	}
	
	public ProjecaoUsuarioDto consultar(long codigo) {
		Optional<ProjecaoUsuario> entity = projecaoRepository.findById(codigo);
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setCodigo(entity.get().getUsuario().getCodigo());
		usuarioDto.setNome(entity.get().getUsuario().getNome());
		usuarioDto.setEmail(entity.get().getUsuario().getEmail());
		return ProjecaoUsuarioDto.build(entity.get(),usuarioDto);
	}
}
