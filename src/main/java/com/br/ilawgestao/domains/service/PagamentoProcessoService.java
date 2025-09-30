package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.PagamentoProcesso;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.TipoPagamento;
import com.br.ilawgestao.domains.repository.PagamentoProcessoRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.repository.TipoPagamentoRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class PagamentoProcessoService {
	
	@Autowired
	private PagamentoProcessoRepository pagamentoProcessoRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private TipoPagamentoRepository pagamentoRepository;
	
	public PagamentoProcesso cadastrarPagamentoProcesso(PagamentoProcesso pagamento) {
		Optional<Processo> processoConsulta = processoRepository.findById(pagamento.getProcesso().getCodigo());
		if(!processoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		Optional<TipoPagamento> pagamentoConsulta = pagamentoRepository.findById(pagamento.getPagamento().getCodigo());
		if(!pagamentoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Pagamento não encontrado");
		}
		
		return pagamentoProcessoRepository.save(pagamento);
	}
	
	public List<PagamentoProcesso> consultarPagamentos(long processo) {
		Optional<Processo> processoConsulta = processoRepository.findById(processo);
		if(!processoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		List<PagamentoProcesso> retorno = new ArrayList<PagamentoProcesso>();
		List<PagamentoProcesso> lista = pagamentoProcessoRepository.findByProcessoCodigoOrderByDtPagamentoDesc(processo); 
		for(PagamentoProcesso pag: lista) {
			PagamentoProcesso p = new PagamentoProcesso();
			p.setCodigo(pag.getCodigo());
			p.setPagamento(pag.getPagamento());
			p.setProcesso(pag.getProcesso());
			p.setDtPagamento(DatasUtil.formatarDataTela(pag.getDtPagamento()));
			p.setVlPagamento(pag.getVlPagamento());
			if(pag.getTipo().equals("C")) {
				p.setTipo("Crédito");
			} else {
				p.setTipo("Débito");
			}
			retorno.add(p);
		}
		
		return retorno;
	}
	
	public void excluirPagamento(long codigo) {
		pagamentoProcessoRepository.deleteById(codigo);
	}
}
