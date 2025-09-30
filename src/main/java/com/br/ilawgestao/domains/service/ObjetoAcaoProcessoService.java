package com.br.ilawgestao.domains.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.ObjetoAcao;
import com.br.ilawgestao.domains.models.ObjetoAcaoProcesso;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.repository.ObjetoAcaoProcessoRepository;
import com.br.ilawgestao.domains.repository.ObjetoAcaoRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;

@Service
public class ObjetoAcaoProcessoService {
	
	@Autowired
	private ObjetoAcaoProcessoRepository objetoProcessoRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private ObjetoAcaoRepository objetoRepository;
	
	public ObjetoAcaoProcesso cadastrarObjetoProcesso(ObjetoAcaoProcesso objetoProcesso) {
		Optional<Processo> processo = processoRepository.findById(objetoProcesso.getProcesso().getCodigo());
		if(!processo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		Optional<ObjetoAcao> objeto = objetoRepository.findById(objetoProcesso.getObjeto().getCodigo());
		if(!objeto.isPresent()) {
			throw new EntidadeNaoEncontradaException("Objeto de Ação não encontrado");
		}
		
		return objetoProcessoRepository.save(objetoProcesso);
	}
	
	public List<ObjetoAcaoProcesso> consultarObjetosPorProcesso(long processo) {
		return objetoProcessoRepository.findByProcessoCodigoOrderByObjetoNomeAsc(processo);
	}
	
	public void excluirObjetoProcesso(long codigo) {
		objetoProcessoRepository.deleteById(codigo);
	}
}
