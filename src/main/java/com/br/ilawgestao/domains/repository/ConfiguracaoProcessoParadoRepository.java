package com.br.ilawgestao.domains.repository;

import com.br.ilawgestao.domains.models.ConfiguracaoProcessoParado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfiguracaoProcessoParadoRepository extends JpaRepository<ConfiguracaoProcessoParado,Long> {
    Optional<ConfiguracaoProcessoParado> findByEmpresaCodigo(long empresa);
}
