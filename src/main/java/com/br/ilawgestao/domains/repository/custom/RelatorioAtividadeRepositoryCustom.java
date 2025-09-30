package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioAtividades;

public interface RelatorioAtividadeRepositoryCustom {
	
	List<Atividade> relatorioAtividades(FiltroRelatorioAtividades filtro);
}
