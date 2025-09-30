package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import com.br.ilawgestao.domains.repository.custom.PessoasIndicadoresRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery, PessoasIndicadoresRepositoryCustom {
	
	@Query(value ="select * from pessoa where cdempresa =:codigoEmpresa and nopessoa =:nome limit 1", nativeQuery = true)
	public Optional<Pessoa> consultarPessoaPorNome(String nome, long codigoEmpresa);
	@Query("from Pessoa where empresa.codigo = :codigoEmpresa and cpfcnpj = :cpf")
	public Optional<Pessoa> consultarPessoaPorCpf(String cpf, long codigoEmpresa);
	public List<Pessoa> findByEmpresaCodigoOrderByNome(long empresa);	
	public List<Pessoa> findByEmpresaCodigoAndPerfilCodigoOrderByNome(long empresa, String perfil);
	public List<Pessoa> findByPerfilCodigoAndEmpresaCodigoOrderByNome(String perfil, long empresa);
	@Query("select p from Pessoa p where p.empresa.codigo = ?1 and (p.nome LIKE %?2% OR p.cpfCnpj LIKE %?2%)")
	public List<Pessoa> consultarPessoaPorNomeOuCpf(long empresa, String pesquisa);
}
