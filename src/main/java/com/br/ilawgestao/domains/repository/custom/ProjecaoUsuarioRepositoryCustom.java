package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.ProjecaoUsuario;
import com.br.ilawgestao.domains.repository.filtros.FiltroProjecaoUsuario;

public interface ProjecaoUsuarioRepositoryCustom {
	List<ProjecaoUsuario> consultarProjecoesUsuario(FiltroProjecaoUsuario filtro);
}
