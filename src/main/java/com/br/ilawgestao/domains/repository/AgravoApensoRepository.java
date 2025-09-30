package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.AgravoApenso;

public interface AgravoApensoRepository extends JpaRepository<AgravoApenso, Long> {
	
	List<AgravoApenso> findByProcessoPrincipalCodigo(long processoId);
	Optional<AgravoApenso> findByProcessoPrincipalCodigoAndProcessoCodigo(long processoPrincipal, long processo);
	Optional<AgravoApenso> findByProcessoCodigo(long codigo);
}
