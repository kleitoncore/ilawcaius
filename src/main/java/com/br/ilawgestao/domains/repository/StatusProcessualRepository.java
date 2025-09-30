package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.StatusProcessual;

@Repository
public interface StatusProcessualRepository extends JpaRepository<StatusProcessual, Long> {
	
	List<StatusProcessual> findByEmpresaCodigoOrderByDescricaoAsc(long codigo);
	Optional<StatusProcessual> findByDescricaoAndEmpresaCodigo(String nome, long codigo);
	
}
