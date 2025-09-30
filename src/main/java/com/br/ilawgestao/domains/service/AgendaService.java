package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.br.ilawgestao.domains.models.StatusAtividade;
import com.br.ilawgestao.domains.repository.StatusAtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.AgendaCalendarDto;
import com.br.ilawgestao.domains.dto.AgendaCalendarioHomeDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Agenda;
import com.br.ilawgestao.domains.models.AtividadeUsuario;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.AtividadeRepository;
import com.br.ilawgestao.domains.repository.AtividadeUsuarioRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.repository.filtros.FiltroAgenda;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class AgendaService {
	
	@Autowired
	private AtividadeRepository atividadeRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private StatusAtividadeRepository statusAtividadeRepository;
	
	@Autowired
	private AtividadeUsuarioRepository atividadeUsuarioRepository;
	
	private boolean isInteressado(long atividade, long usuario) {
		Optional<AtividadeUsuario> atividadeUsuario = atividadeUsuarioRepository.findByAtividadeCodigoAndUsuarioCodigoAndTipo(atividade, usuario, "I");
		if(atividadeUsuario.isPresent()) {
			return true;
		}
		
		return false;
	}
	
	public List<AgendaCalendarDto> listarAgenda(FiltroAgenda filtro) {
		List<AgendaCalendarDto> listaAtividades = new ArrayList<AgendaCalendarDto>();
		List<Agenda> atividades = atividadeRepository.agenda(filtro);
		Optional<Usuario> usuario = usuarioRepository.findById(filtro.getUsuario());
		if(!usuario.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado no sistema");
		}

		for(Agenda ativ : atividades) {
			AgendaCalendarDto dto = new AgendaCalendarDto();
			Optional<StatusAtividade> status = statusAtividadeRepository.findById(ativ.getStatus());
			dto.setId(ativ.getCodigoAtividade());
			if(this.isInteressado(ativ.getCodigoAtividade(), ativ.getCodigoResponsavel())) {
				if(ativ.getTpAtividade().equals("A")) {
					dto.setTitle(ativ.getTitulo() + " - " + DatasUtil.formatarHoraTela(ativ.getDataCompromisso()) + " - (Interessado)");
				} else {
					dto.setTitle(ativ.getTitulo() + " - (Interessado)");
				}
			} else {
				if(ativ.getTpAtividade().equals("A")) {
					dto.setTitle(ativ.getTitulo() + " - " + DatasUtil.formatarHoraTela(ativ.getDataCompromisso()));
				} else {
					dto.setTitle(ativ.getTitulo());
				}
			}
			dto.setStart(DatasUtil.formatarAgenda(ativ.getDataCompromisso()));
			String cor = "";
			String texto = "";

			dto.setColor(status.get().getCor());
			dto.setTextColor("white");
			dto.setStatus(status.get().getStatus());
			dto.setImage_url("/admin.png");
			dto.setImg("/admin.png");
			listaAtividades.add(dto);
		}

		return listaAtividades;
	}
	
	public List<AgendaCalendarDto> listarAgendaHome(FiltroAgenda filtro) {
		List<AgendaCalendarDto> listaAtividades = new ArrayList<AgendaCalendarDto>();
		Optional<Usuario> usuario = usuarioRepository.findById(filtro.getUsuario());
		filtro.setUsuarios(usuario.get().getCodigo().toString());
		List<Agenda> atividades = atividadeRepository.agendaHome(filtro);
		
		for(Agenda ativ : atividades) {
			AgendaCalendarDto dto = new AgendaCalendarDto();
			dto.setId(ativ.getCodigoAtividade());
			dto.setTitle(ativ.getTitulo());
			dto.setStart(DatasUtil.formatarAgenda(ativ.getDataCompromisso()));
			String status = "";
			String cor = "#1229ab";
			String texto = "white";
			dto.setColor(cor);
			dto.setTextColor(texto);
			dto.setStatus(status);
			listaAtividades.add(dto);
		}
		
		return listaAtividades;
	}
	
	public List<AgendaCalendarDto> listarAgendaHomeResumo(FiltroAgenda filtro) {
		List<AgendaCalendarDto> listaAtividades = new ArrayList<AgendaCalendarDto>();
		List<AgendaCalendarioHomeDto> lista = atividadeRepository.agendaHomeResumo(filtro);
		
		int i =1;
		for(AgendaCalendarioHomeDto ah : lista) {
			AgendaCalendarDto dto = new AgendaCalendarDto();
			dto.setId(i);
			dto.setTitle(ah.getAtividades().toString());
			dto.setStart(DatasUtil.formatarAgenda(ah.getDataCompromisso()));
			dto.setStatus("");
			dto.setColor("red");
			dto.setTextColor("white");
			dto.setImg(DatasUtil.formatarDataBanco(ah.getDataCompromisso()));
			listaAtividades.add(dto);
			i++;
		}
		
		return listaAtividades;
	}
}
