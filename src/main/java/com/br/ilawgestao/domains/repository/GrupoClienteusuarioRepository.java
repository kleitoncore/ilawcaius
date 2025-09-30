package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.GrupoClienteUsuario;
import com.br.ilawgestao.domains.repository.custom.GrupoClienteUsuarioCustom;

@Repository
public interface GrupoClienteusuarioRepository extends JpaRepository<GrupoClienteUsuario, Long>, GrupoClienteUsuarioCustom {
	
	@Query("from GrupoClienteUsuario where usuario.codigo = :usuario")
	public List<GrupoClienteUsuario> consultarGruposPorUsuario(long usuario);
	
	@Query("from GrupoClienteUsuario where grupo.codigo = :grupo")
	public List<GrupoClienteUsuario> consultarUsuariosPorGrupo(long grupo);
	
	@Query("from GrupoClienteUsuario where grupo.codigo = :grupo and usuario.codigo = :usuario ")
	public Optional<GrupoClienteUsuario> consultarUsuarioGrupo(long usuario, long grupo);
}
