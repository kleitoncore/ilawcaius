package com.br.ilawgestao.domains.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.Partes;

@Repository
public interface PartesRepository extends JpaRepository<Partes, Long> {
	
	List<Partes> findByProcessoCodigoOrderByPessoaNomeAsc(long processo);
	List<Partes> findByProcessoCodigoAndTipoParteOrderByPessoaNomeAsc(long processo, String tipo);
	List<Partes> findByPessoaCodigo(long pessoa);
}
