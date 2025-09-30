package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.GrupoTrabalho;

public interface GruposSemUsuariosRepositoryCustom {
	
	public List<GrupoTrabalho> listarGruposSemUsuarios(long empresa, long usuario);
}
