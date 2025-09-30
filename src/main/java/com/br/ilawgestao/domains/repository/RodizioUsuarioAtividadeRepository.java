package com.br.ilawgestao.domains.repository;

import com.br.ilawgestao.domains.RodizioUsuarioAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RodizioUsuarioAtividadeRepository extends JpaRepository<RodizioUsuarioAtividade, Long> {
    List<RodizioUsuarioAtividade> findByGrupoAtividadeCodigoAndAtividadeCodigoOrderByPosicao(long grupoAtividade,long atividade);
    Optional<RodizioUsuarioAtividade> findByGrupoAtividadeCodigoAndGrupoTrabalhoCodigoAndAtividadeCodigoAndUsuarioAtividadeCodigo(
            long grupoAtividade, long grupoTrabalho, long atividade, long usuario);

    @Query(value = "SELECT * FROM rodizio_usuario_atividade r WHERE r.posicao = (SELECT MAX(r2.posicao) FROM rodizio_usuario_atividade r2" +
            " where r.cdgrupo_atividade = r2.cdgrupo_atividade and r.cdatividade = r2.cdatividade)" +
            " and r.cdgrupo_atividade = :grupoAtividade and r.cdatividade = :atividade", nativeQuery = true)
    Optional<RodizioUsuarioAtividade> encontrarRodizioComMaiorPosicao(long grupoAtividade, long atividade);

}