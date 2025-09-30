package com.br.ilawgestao.domains.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.ilawgestao.domains.models.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	
	List<Empresa> findEmpresaByStatus(int status);
	List<Empresa> findByNomeContaining(String nome);
	List<Empresa> findByEmailContaining(String email);
}
