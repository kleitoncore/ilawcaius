package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.GrupoCliente;

public interface GrupoClienteUsuarioCustom {
	
	List<GrupoCliente> listarGruposUsuario(long usuario);
}
