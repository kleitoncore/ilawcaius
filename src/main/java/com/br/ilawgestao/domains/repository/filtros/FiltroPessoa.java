package com.br.ilawgestao.domains.repository.filtros;

import com.br.ilawgestao.domains.models.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroPessoa {
	
	private String nome;
	private String cpfcnpj;
	private Estado estado;
	private Cidade cidade;
	private String telefone;
	private Empresa empresa;
	private String porLetra;
	private String letra;
	private GrupoCliente grupo;
	private Usuario usuario;
	
	public FiltroPessoa() {
		this.nome = "";
		this.cpfcnpj = "";
		this.estado = null;
		this.cidade = null;
		this.telefone = "";
		this.empresa = null;
		this.porLetra = "";
		this.letra = "";
		this.grupo = null;
		this.usuario = null;
	}
	
	public FiltroPessoa(String nome, String cpfCnpj, Cidade cidade, Estado estado, String telefone, Empresa empresa,
			String porLetra, String letra, GrupoCliente grupo, Usuario usuario) {
		this.nome = nome;
		this.cpfcnpj = cpfCnpj;
		this.cidade = cidade;
		this.estado = estado;
		this.telefone = telefone;
		this.empresa = empresa;
		this.porLetra = porLetra;
		this.letra = letra;
		this.grupo = grupo;
		this.usuario = usuario;
	}
}
