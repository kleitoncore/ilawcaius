package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.ObjetoAcao;
import com.br.ilawgestao.domains.repository.custom.ObjetoAcaoProcessoRepositoryCustom;

@Repository
public interface ObjetoAcaoRepository extends JpaRepository<ObjetoAcao, Long>, ObjetoAcaoProcessoRepositoryCustom {	
	
	@Query("from ObjetoAcao where empresa.codigo = :empresa and codigo = objetoPai order by nome")
	public List<ObjetoAcao> listarObjetos(Long empresa);
	
	@Query("from ObjetoAcao where empresa.codigo = :empresa and nome = :nome")
	public Optional<ObjetoAcao> consultarObjetoAcaoPorNome(String nome, long empresa);
	
	@Query("from ObjetoAcao where objetoPai =:codigoPai and codigo <> objetoPai order by nome")
	List<ObjetoAcao> listaObjetosFilhos(long codigoPai);	
}

