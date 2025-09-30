package com.br.ilawgestao.domains.repository;

import java.util.List;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;

public interface GrupoTrabalhoRepositoryQuery {
	
	public List<GrupoTrabalho> listarGrupoTrabalhoPorNome(String nome, Empresa empresa);
}
