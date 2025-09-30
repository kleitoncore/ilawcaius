package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.models.TituloAtividade;
import com.br.ilawgestao.domains.repository.filtros.FiltroTituloAtividade;

import java.util.List;

public interface TituloAtividadeRepositoryCustom {
    List<TituloAtividade> consultarTituloAtividade(FiltroTituloAtividade filtro);
}
