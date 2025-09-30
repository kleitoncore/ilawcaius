package com.br.ilawgestao.domains.repository;

import com.br.ilawgestao.domains.models.ContaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaFinanceiraRepository extends JpaRepository<ContaFinanceira,Long> {
    List<ContaFinanceira> findByEmpresaCodigo(long empresa);
    List<ContaFinanceira> findByEmpresaCodigoAndStatus(long empresa, String status);
    Optional<ContaFinanceira> findByEmpresaCodigoAndContaDefault(long empresa, String contaDefault);
}
