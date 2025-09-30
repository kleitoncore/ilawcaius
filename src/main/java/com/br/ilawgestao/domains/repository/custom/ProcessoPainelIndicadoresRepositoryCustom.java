package com.br.ilawgestao.domains.repository.custom;

public interface ProcessoPainelIndicadoresRepositoryCustom {
	
	int totalProcessos(long empresa);
	int totalAtivos(long empresa);
	int parados(long empresa);
	int arquivados(long empresa);
	int excluidos(long empresa);
	int processoPush(long empresa);
	
}
