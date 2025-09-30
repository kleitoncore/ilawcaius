package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.repository.custom.AtividadeGraficoCustom;
import com.br.ilawgestao.domains.repository.custom.AtividadeRepositoryCustom;
import com.br.ilawgestao.domains.repository.custom.PontuacaoUsuarioRepositoryCustom;
import com.br.ilawgestao.domains.repository.custom.RelatorioAtividadeRepositoryCustom;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long>, AtividadeRepositoryCustom, AtividadeGraficoCustom, 
			RelatorioAtividadeRepositoryCustom, PontuacaoUsuarioRepositoryCustom {
	List<Atividade> findByProcessoCodigoOrderByDtLimiteDesc(long processo);
	List<Atividade> findBySubGrupoCodigo(long codigo);
}
