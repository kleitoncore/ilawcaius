package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.GrupoCliente;

@Repository
public interface GrupoClienteRepository extends JpaRepository<GrupoCliente, Long> {
	
	@Query("from GrupoCliente where empresa.codigo = :codigo order by nome")
	public List<GrupoCliente> consultarGruposClientesPorEmpresa(long codigo);
	
	@Query("from GrupoCliente where empresa.codigo = :empresa and nome = :nome")
	public Optional<GrupoCliente> consultarGrupoPorNome(String nome, long empresa);
}
