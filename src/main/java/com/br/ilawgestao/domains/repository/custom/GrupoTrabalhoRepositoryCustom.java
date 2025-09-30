package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;

public interface GrupoTrabalhoRepositoryCustom {
	
	List<GrupoTrabalho> listarGruposTrabalho(String nome, Empresa empresa);
	List<GrupoTrabalho> listarGruposTrabalhoUsuario(long usuario);
	List<GrupoTrabalho> listarGruposTrabalhoUsuario2(long usuario);
	List<GrupoTrabalho> consultarGruposPorCodigos(String codigos);
	List<GrupoTrabalho> listarGruposTrabalhosAtivos(long empresa);
}
