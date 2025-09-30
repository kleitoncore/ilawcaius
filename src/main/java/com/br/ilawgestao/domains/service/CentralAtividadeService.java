package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.models.CentralAtividade;
import com.br.ilawgestao.domains.repository.CentralAtividadeRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class CentralAtividadeService {
	
	@Autowired
	private CentralAtividadeRepository centralRepository;
		
	public int notificacoes(long usuario) {
		List<CentralAtividade> notifica = centralRepository.findByUsuarioCodigoAndStatus(usuario,1);
		return notifica.size();
	}
	
	public List<CentralAtividade> consultarCentral(long usuario, String dataInicial, String dataFinal) {
		List<CentralAtividade> notificacoes = centralRepository.consultarCentralAtividades(dataInicial, dataFinal, usuario);
		List<CentralAtividade> lista = new ArrayList<CentralAtividade>();
		if(notificacoes != null) {
			for(CentralAtividade notif : notificacoes) {
				CentralAtividade central = new CentralAtividade();
				central.setCodigo(notif.getCodigo());
				central.setDescricao(notif.getDescricao());
				central.setAtividade(notif.getAtividade());
				central.setUsuario(notif.getUsuario());
				central.setDataRegistro(DatasUtil.formatarDataTela(notif.getDataRegistro()));
				central.setStatus(notif.getStatus());
				if(notif.getStatus() == 1) {
					central.setNaoLida("naoLida");
				}
				lista.add(central);
			}
		}
		
		return lista;
	}
	
	public CentralAtividade lerNotificacao(long codigo, CentralAtividade central) {
		Optional<CentralAtividade> centralConsulta = centralRepository.findById(codigo);
		BeanUtils.copyProperties(central, centralConsulta.get(),"codigo","atividade","usuario","dataRegistro","descricao");
		return centralRepository.save(centralConsulta.get());
	}
	
	public void excluirNotificacoes(long[] codigos) {
		if(codigos.length > 0) {
			for(int i = 0; i < codigos.length; i++) {
				centralRepository.deleteById(codigos[i]);
			}
		}
	}
}
