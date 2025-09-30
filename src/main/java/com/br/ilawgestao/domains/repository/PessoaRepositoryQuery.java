package com.br.ilawgestao.domains.repository;

import java.util.List;

import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.repository.filtros.FiltroPessoa;

public interface PessoaRepositoryQuery {
	
	public List<Pessoa> filtrarPessoas(FiltroPessoa filtro);
	public List<Pessoa> listarPessoasForaDasPartesProcesso(long empresa, long processo, String perfil);
	public List<Pessoa> consultarPessoas(String codigos);
}
