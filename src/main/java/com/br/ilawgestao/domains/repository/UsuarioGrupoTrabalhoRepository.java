package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.br.ilawgestao.domains.models.UsuarioGrupoTrabalho;
import com.br.ilawgestao.domains.repository.custom.UsuarioGrupoTrabalhoRepositoryCustom;

@Repository
public interface UsuarioGrupoTrabalhoRepository extends JpaRepository<UsuarioGrupoTrabalho, Long>, UsuarioGrupoTrabalhoRepositoryCustom {
	
	@Query("from UsuarioGrupoTrabalho where grupo.codigo = :codigoGrupo")
	public List<UsuarioGrupoTrabalho> listarUsuariosPorGrupo(long codigoGrupo);
	
	@Query("from UsuarioGrupoTrabalho where usuario.codigo = :usuario")
	public List<UsuarioGrupoTrabalho> listarGruposPorUsuario(long usuario);
	
	@Query("from UsuarioGrupoTrabalho where grupo.codigo = :grupo and usuario.codigo = :usuario")
	public Optional<UsuarioGrupoTrabalho> existeUsuarioGrupo(long grupo, long usuario);
}
