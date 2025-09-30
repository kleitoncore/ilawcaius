package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.CustasProcesso;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.TipoCusta;
import com.br.ilawgestao.domains.repository.CustasProcessoRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.repository.TipoCustaRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class CustasProcessoService {
	
	@Autowired
	private CustasProcessoRepository custasProcessoRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private TipoCustaRepository custasRespository;
	
	public CustasProcesso cadastrarCustasProcesso(CustasProcesso custa) {
		Optional<Processo> processo = processoRepository.findById(custa.getProcesso().getCodigo());
		if(!processo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		Optional<TipoCusta> custasConsulta = custasRespository.findById(custa.getCustas().getCodigo());
		if(!custasConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Custas não encontrado");
		}
		
		return custasProcessoRepository.save(custa);
	}
	
	public List<CustasProcesso> consultarCustasProcesso(long processo) {
		Optional<Processo> processoConsulta = processoRepository.findById(processo);
		if(!processoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		List<CustasProcesso> lista = custasProcessoRepository.findByProcessoCodigoOrderByCustasNomeAsc(processo);
		List<CustasProcesso> custas = new ArrayList<CustasProcesso>();
		for(CustasProcesso cp : lista) {
			CustasProcesso custa = new CustasProcesso();
			custa.setCodigo(cp.getCodigo());
			custa.setProcesso(cp.getProcesso());
			custa.setDtPagamento(DatasUtil.formatarDataTela(cp.getDtPagamento()));
			if(cp.getTipo().equals("C")) {
				custa.setTipo("Crédito");
			} else {
				custa.setTipo("Débito");
			}
			custa.setCustas(cp.getCustas());
			custa.setVlCustas(cp.getVlCustas());
			custas.add(custa);
		}
		
		return custas;
	}
	
	public void excluirCustasProcesso(long codigo) {
		custasProcessoRepository.deleteById(codigo);
	}
}
