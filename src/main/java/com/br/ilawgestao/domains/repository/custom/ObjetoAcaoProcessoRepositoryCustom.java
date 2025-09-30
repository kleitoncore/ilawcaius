package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.models.ObjetoAcao;

public interface ObjetoAcaoProcessoRepositoryCustom {
	
	List<ObjetoAcao> listarObjetosForaDoProcesso(long empresa, long processo);
	List<ObjetoAcao> listarSubObjetosForaDoProcesso(long processo, long objetoPai);
	List<ObjetoAcao> listaObjetosPorCodigos(String codigos);
}
