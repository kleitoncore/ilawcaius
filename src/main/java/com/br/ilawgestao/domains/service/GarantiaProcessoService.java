package com.br.ilawgestao.domains.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.GarantiaProcesso;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.TipoGarantia;
import com.br.ilawgestao.domains.repository.GarantiaProcessoRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.repository.TipoGarantiaRepository;

@Service
public class GarantiaProcessoService {
	
	@Autowired
	private GarantiaProcessoRepository garantiaProcessoRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private TipoGarantiaRepository garantiaRepository;
	
	public GarantiaProcesso cadastrarGarantiaProcesso(GarantiaProcesso garantia) {
		Optional<Processo> processo = processoRepository.findById(garantia.getProcesso().getCodigo());
		if(!processo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		Optional<TipoGarantia> garantiaConsulta = garantiaRepository.findById(garantia.getGarantia().getCodigo());
		if(!garantiaConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Garantia não encontrada");
		}
		
		return garantiaProcessoRepository.save(garantia);
	}
	
	public List<GarantiaProcesso> consultarGarantiasProcesso(long processo) {
		Optional<Processo> processoConsulta = processoRepository.findById(processo);
		if(!processoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		return garantiaProcessoRepository.findByProcessoCodigoOrderByGarantiaNomeAsc(processo);
	}
	
	public void excluirGarantia(long codigo) {
		garantiaProcessoRepository.deleteById(codigo);
	}
}
