package com.br.ilawgestao.domains.repository;

import com.br.ilawgestao.domains.models.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgenciaRepository extends JpaRepository<Agencia,Long> {
    Optional<Agencia> findByContaFinanceiraCodigo(long codigo);
}
