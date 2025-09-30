package com.br.ilawgestao.domains.repository;

import com.br.ilawgestao.domains.models.ControleArquivoPush;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ControleArquivoPushRepository extends JpaRepository<ControleArquivoPush, Long> {
    List<ControleArquivoPush> findByStatus(String status);
    Optional<ControleArquivoPush> findByArquivo(String arquivo);
}
