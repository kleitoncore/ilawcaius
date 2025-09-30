package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.TipoDecisao;

@Repository
public interface TipoDecisaoRepository extends JpaRepository<TipoDecisao, Long> {
	
	List<TipoDecisao> findByEmpresaCodigoOrderByNome(long empresa);
	Optional<TipoDecisao> findByNomeAndEmpresaCodigo(String nome, long empresa);
	
}
