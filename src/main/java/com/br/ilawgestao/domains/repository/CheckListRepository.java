package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.CheckListAtividade;

public interface CheckListRepository extends JpaRepository<CheckListAtividade, Long> {
	List<CheckListAtividade> findByAtividadeCodigoOrderByOrdem(long atividade);
}
