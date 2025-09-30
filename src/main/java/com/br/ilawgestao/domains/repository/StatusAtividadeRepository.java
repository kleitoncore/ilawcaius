package com.br.ilawgestao.domains.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.br.ilawgestao.domains.models.StatusAtividade;

import java.util.List;
import java.util.Optional;

public interface StatusAtividadeRepository extends JpaRepository<StatusAtividade, Long> {
    List<StatusAtividade> findByEmpresaCodigo(long empresa);
    Optional<StatusAtividade> findByStatusAndEmpresaCodigo(String nome, long empresa);
}
