package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.custom.UsuarioSemGrupoRepositoryCustom;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioSemGrupoRepositoryCustom {
	
	@Query("from Usuario where empresa.codigo = :empresa and nome like %:nome% order by nome desc")
	public List<Usuario> listarUsuarioPorNome(long empresa, String nome);
	public Usuario findUsuarioByCodigo(Long codigo);
	boolean existsByEmail(String email);
	@Query("from Usuario where email =:email and senha =:senha and situacao = 0")
	public Usuario autenticarUsuario(String email, String senha);
	public List<Usuario> findByEmpresaCodigoOrderByNome(long empresa);
	public Optional<Usuario> findByEmail(String email);
	List<Usuario> findByPerfilCodigoAndEmpresaCodigo(long perfil, long empresa);

	@Query(value = "select u.*\n" +
			"  from usuario u\n" +
			" where u.cdusuario in(select r.cdusuario_atividade \n" +
			"        from rodizio_usuario_atividade r \n" +
			"       where r.cdgrupo_trabalho = :grupoTrabalho \n" +
			"         and r.cdgrupo_atividade = :grupoAtividade \n" +
			"         and r.cdatividade = :atividade\n" +
			"         and r.posicao = (select min(r2.posicao) from rodizio_usuario_atividade r2\n" +
			"                                                 where r.cdgrupo_atividade = r2.cdgrupo_atividade\n" +
			"                                                   and r.cdgrupo_trabalho = r2.cdgrupo_trabalho\n" +
			"                                                   and r.cdatividade = r2.cdatividade))", nativeQuery = true)
	Optional<Usuario> consultarUsuarioRodizioPrimeiroRanking(long grupoTrabalho, long grupoAtividade, long atividade);
}
