package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.TipoCusta;

public interface TipoCustasRepositoryCustom {
	
	List<TipoCusta> listaTiposCustasPorCodigos(String codigos);
	
}
