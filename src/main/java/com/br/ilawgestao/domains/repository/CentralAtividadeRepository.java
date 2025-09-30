package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.CentralAtividade;
import com.br.ilawgestao.domains.repository.custom.CentralAtividadeRepositoryCustom;

public interface CentralAtividadeRepository extends JpaRepository<CentralAtividade, Long>, CentralAtividadeRepositoryCustom {
	List<CentralAtividade> findByUsuarioCodigoAndStatus(long usuario, long status);
	List<CentralAtividade> findByAtividadeCodigo(long atividade);
}
