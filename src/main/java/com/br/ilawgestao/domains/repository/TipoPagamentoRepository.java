package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.TipoPagamento;
import com.br.ilawgestao.domains.repository.custom.TipoPagamentoRepositoryCustom;

@Repository
public interface TipoPagamentoRepository extends JpaRepository<TipoPagamento, Long>, TipoPagamentoRepositoryCustom {
	
	List<TipoPagamento> findByEmpresaCodigoOrderByNomeAsc(long codigo);
	Optional<TipoPagamento> findByNomeAndEmpresaCodigo(String nome, long empresa);
		
}
