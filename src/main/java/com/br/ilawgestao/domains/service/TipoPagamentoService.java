package com.br.ilawgestao.domains.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeJaCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoCadastradaException;
import com.br.ilawgestao.domains.models.TipoPagamento;
import com.br.ilawgestao.domains.repository.TipoPagamentoRepository;

@Service
public class TipoPagamentoService {
	
	@Autowired
	private TipoPagamentoRepository tipoPagamentoRepository;
	
	public TipoPagamento cadastrarTipoPagamento(TipoPagamento tipo) {
		Optional<TipoPagamento> tipoConsulta = tipoPagamentoRepository.findByNomeAndEmpresaCodigo(tipo.getNome(), tipo.getEmpresa().getCodigo());
		if(tipoConsulta.isPresent()) {
			throw new EntidadeJaCadastradaException("Tipo de Pagamento já cadastrado com este nome");
		}
		
		return tipoPagamentoRepository.save(tipo);
	}
	
	public List<TipoPagamento> listarTiposPagamentos(long empresa) {
		return tipoPagamentoRepository.findByEmpresaCodigoOrderByNomeAsc(empresa);
	}
	
	public TipoPagamento consultarTipoPagamento(long codigo) {
		Optional<TipoPagamento> tipoPagamento = tipoPagamentoRepository.findById(codigo);
		if(!tipoPagamento.isPresent()) {
			throw new EntidadeNaoCadastradaException("Tipo de Pagamento não encontrado");
		}
		return tipoPagamento.get();
	}
	
	public TipoPagamento alterarTipoPagamento(long codigo, TipoPagamento tipo) {
		TipoPagamento tipoConsulta = consultarTipoPagamento(codigo);
		Optional<TipoPagamento> tipoNome = tipoPagamentoRepository.findByNomeAndEmpresaCodigo(tipo.getNome(), tipo.getEmpresa().getCodigo());
		if(tipoNome.isPresent() && tipoNome.get().getCodigo() != tipoConsulta.getCodigo()) {
			throw new EntidadeJaCadastradaException("Tipo de Pagamento já cadastrado com este nome");
		}
		BeanUtils.copyProperties(tipo, tipoConsulta,"codigo");
		return tipoPagamentoRepository.save(tipoConsulta);
	}
	
	public void excluirTipoPagamento(long codigo) {
		TipoPagamento tipo = consultarTipoPagamento(codigo);
		try {
			tipoPagamentoRepository.delete(tipo);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException("Tipo de Pagamento não pode ser excluído, já está em uso");
		}
	}
	
	public List<TipoPagamento> consultarTiposPagamentoPorCodigos(String codigos) {
		return tipoPagamentoRepository.listaPagamentosPorCodigos(codigos);
	}
}
