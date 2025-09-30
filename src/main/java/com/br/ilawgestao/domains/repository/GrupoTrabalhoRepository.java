package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.repository.custom.GrupoTrabalhoRepositoryCustom;
import com.br.ilawgestao.domains.repository.custom.GruposSemUsuariosRepositoryCustom;

@Repository
public interface GrupoTrabalhoRepository extends JpaRepository<GrupoTrabalho, Long>, GrupoTrabalhoRepositoryCustom, GruposSemUsuariosRepositoryCustom {
	
	List<GrupoTrabalho> findGrupoTrabalhoByEmpresaOrderByNomeAsc(Empresa empresa);
	
	@Query("from GrupoTrabalho where nome = :nome and empresa.codigo = :empresa and grupoPai = codigo")
	Optional<GrupoTrabalho> consultarGrupoTrabalhoPoNome(String nome, long empresa);
	
	@Query("From GrupoTrabalho where grupoPai = :grupo and codigo <> grupoPai order by nome ")
	List<GrupoTrabalho> listarSubGrupos(long grupo);
	
	Optional<GrupoTrabalho> findGrupoTrabalhoByNomeAndGrupoPai(String nome, long grupoPai);
	
}
