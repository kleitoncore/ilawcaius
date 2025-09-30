package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.ExecucaoCheckList;

public interface ExecucaoCheckListRepository extends JpaRepository<ExecucaoCheckList, Long> {
	List<ExecucaoCheckList> findByAtividadeCodigoOrderByCheckListOrdem(long atividade);
	List<ExecucaoCheckList> findByAtividadeCodigoAndCodigoLessThan(long atividade, long codigo);
}
