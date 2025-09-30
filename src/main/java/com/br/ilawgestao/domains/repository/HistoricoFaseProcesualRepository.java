package com.br.ilawgestao.domains.repository;

import com.br.ilawgestao.domains.models.HistoricoFaseProcessual;
import com.br.ilawgestao.domains.repository.custom.HistoricoFaseProcessualRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoricoFaseProcesualRepository extends JpaRepository<HistoricoFaseProcessual, Long>, HistoricoFaseProcessualRepositoryCustom {
    List<HistoricoFaseProcessual> findByProcessoCodigo(long processo);
    Optional<HistoricoFaseProcessual> findByProcessoCodigoAndAtividadeCodigo(long processo, long atividade);
}
