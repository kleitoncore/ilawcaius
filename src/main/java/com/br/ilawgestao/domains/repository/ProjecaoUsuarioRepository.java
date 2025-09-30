package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.ProjecaoUsuario;
import com.br.ilawgestao.domains.repository.custom.ProjecaoUsuarioRepositoryCustom;

public interface ProjecaoUsuarioRepository extends JpaRepository<ProjecaoUsuario, Long>, ProjecaoUsuarioRepositoryCustom {
	List<ProjecaoUsuario> findByEmpresaCodigoOrderByUsuarioNomeAscAnoAscMesAsc(long codigo);
	Optional<ProjecaoUsuario> findByUsuarioCodigoAndAnoAndMes(long usuario, long ano, long mes);
}
