package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.HistoricoProcessoDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.HistoricoProcesso;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.HistoricoProcessoRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class HistoricoProcessoService {
	
	@Autowired
	private HistoricoProcessoRepository historicoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	public HistoricoProcessoDto incluirHistorico(HistoricoProcessoDto dto) {
		Optional<Processo> processo = processoRepository.findById(dto.getProcesso().getCodigo());
		if(!processo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuario().getCodigo());
		if(!usuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		dto.setDataHistorico(DatasUtil.getDataAtual());
		
		HistoricoProcesso historico = HistoricoProcesso.builder()
				.historico(dto.getHistorico())
				.dataHistorico(dto.getDataHistorico())
				.dataOcorrencia(dto.getDataOcorrencia())
				.processo(dto.getProcesso())
				.usuario(dto.getUsuario())
				.tipoAndamento(dto.getTipoAndamento())
				.build();
		
		return HistoricoProcessoDto.build(historicoRepository.save(historico));
	}
	
	public List<HistoricoProcessoDto> listarHistoricoPorProcesso(long processo) {
		Optional<Processo> processoConsulta = processoRepository.findById(processo);
		if(!processoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		List<HistoricoProcesso> lista = historicoRepository.findByProcessoCodigoOrderByCodigoDesc(processo);
		List<HistoricoProcessoDto> dtos = new ArrayList<HistoricoProcessoDto>();
		for(HistoricoProcesso hist: lista) {
			HistoricoProcesso h = new HistoricoProcesso();
			h.setCodigo(hist.getCodigo());
			h.setProcesso(hist.getProcesso());
			h.setHistorico(hist.getHistorico());
			h.setDataHistorico(DatasUtil.formatarDataTela(hist.getDataHistorico()));
			h.setDataOcorrencia(DatasUtil.formatarDataTela(hist.getDataOcorrencia()));
			h.setUsuario(hist.getUsuario());
			h.setTipoAndamento(hist.getTipoAndamento());
			dtos.add(HistoricoProcessoDto.build(h));
		}
		
		return dtos;
	}
	
	public HistoricoProcesso consultarUltimaMovimentacao(long processo) {
		Optional<Processo> processoConsulta = processoRepository.findById(processo);
		if(!processoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		return historicoRepository.consultarUltimaMovimentacao(processo);
	}
	
	public void excluirHistorico(long codigo) {
		historicoRepository.deleteById(codigo);
	}
}
