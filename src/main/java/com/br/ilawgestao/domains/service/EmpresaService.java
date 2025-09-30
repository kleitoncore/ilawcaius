package com.br.ilawgestao.domains.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.repository.EmpresaRepository;

@Service
public class EmpresaService {
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	public Empresa cadastrarEmpresa(Empresa empresa) {
		return empresaRepository.save(empresa);
	}
	
	public Empresa alterarEmpresa(long codigo, Empresa empresa) {
		Empresa empresaSalva = consultarEmpresaPorCodigo(codigo);
		BeanUtils.copyProperties(empresa, empresaSalva,"codigo");
		return empresaRepository.save(empresaSalva);
	}
	
	public Empresa consultarEmpresaPorCodigo(long codigo) {
		Optional<Empresa> empresaBuscada = empresaRepository.findById(codigo);
		if(!empresaBuscada.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return empresaBuscada.get();
	}
	
	public Page<Empresa> listarEmpresas(Pageable pageble) {
		return empresaRepository.findAll(pageble);
	}
	
	public List<Empresa> consultarEmpresaPorStatus(int status) {
		return empresaRepository.findEmpresaByStatus(status);
	}
	
	public List<Empresa> consultarEmpresaPorNome(String nome) {
		return empresaRepository.findByNomeContaining(nome);
	}
	
	public List<Empresa> consultarEmpresaPorEmail(String email) {
		return empresaRepository.findByEmailContaining(email);
	}
	
}
