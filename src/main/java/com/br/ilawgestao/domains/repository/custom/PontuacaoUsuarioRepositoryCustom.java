package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.dto.PontuacaoProjecaoDatasDto;
import com.br.ilawgestao.domains.dto.PontuacaoUsuarioDto;

public interface PontuacaoUsuarioRepositoryCustom {
	List<PontuacaoUsuarioDto> pontuacaoUsuario(long ano, long mes, long usuario, long empresa);
	List<PontuacaoProjecaoDatasDto> pontuacaoProjecaoDatas(long empresa,String dataInicial,String dataFinal);
}
