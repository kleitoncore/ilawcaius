package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Cidade;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.Perfil;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.utils.Criptografar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
	
	public Usuario transformaParaObjeto() {
		return new Usuario(codigo, nome, email, Criptografar.encriptografar(senha), cpf, situacao, endereco, 
				numero, complemento, cep, bairro, cidade, telefone1, telefone2, 
				dataCadastro, empresa, perfil, foto, statusLogin);
	}
	
	private Long codigo;
	private String nome;
	private String email;
	private String senha;
	private String cpf;
	private Long situacao;
	private String endereco;
	private String numero;
	private String complemento;
	private String cep;
	private String bairro;
	private Cidade cidade;
	private String telefone1;
	private String telefone2;
	private String dataCadastro;
	private Empresa empresa;
	private Perfil perfil;
	private String foto;
	private String statusLogin;
	
	public static UsuarioDto build(Usuario usuario) {
		UsuarioDto dto = new UsuarioDto();
		dto.setCodigo(usuario.getCodigo());
		dto.setNome(usuario.getNome());
		dto.setEmail(usuario.getEmail());
		return dto;
	}

	public static Usuario buildConsulta(Usuario usuario) {
		Usuario usu = new Usuario();
		usu.setCodigo(usuario.getCodigo());
		usu.setNome(usuario.getNome());
		usu.setEmail(usuario.getEmail());
		usu.setCpf(usuario.getCpf());
		usu.setSituacao(usuario.getSituacao());
		usu.setEndereco(usuario.getEndereco());
		usu.setNumero(usuario.getNumero());
		usu.setComplemento(usuario.getComplemento());
		usu.setCep(usuario.getCep());
		usu.setBairro(usuario.getBairro());
		usu.setCidade(usuario.getCidade());
		usu.setTelefone1(usuario.getTelefone1());
		usu.setTelefone2(usuario.getTelefone2());
		usu.setDataCadastro(usuario.getDataCadastro());
		usu.setEmpresa(usuario.getEmpresa());
		usu.setPerfil(usuario.getPerfil());
		usu.setFoto(usuario.getFoto());
		usu.setStatusLogin(usuario.getStatusLogin());
		return usu;
	}
}
