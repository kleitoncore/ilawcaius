package com.br.ilawgestao.domains.repository.custom;

import java.util.List;
import java.util.Optional;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.models.Agenda;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.AtividadeShort;
import com.br.ilawgestao.domains.repository.filtros.FiltroAgenda;
import com.br.ilawgestao.domains.repository.filtros.FiltroKanban;
import com.br.ilawgestao.domains.repository.filtros.FiltroMiniAgenda;

public interface AtividadeRepositoryCustom {
	
	List<AtividadeShort> listaAtividadesAtrasadasResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal);
	List<AtividadeShort> listaAtividadesHojeResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal);
	List<AtividadeShort> listaAtividadesSeteResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal);
	List<AtividadeShort> listaAtividadesTrintaResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal);
	List<AtividadeShort> listaAtividadesAtrasadasInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal);
	List<AtividadeShort> listaAtividadesHojeInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal);
	List<AtividadeShort> listaAtividadesSeteInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal);
	List<AtividadeShort> listaAtividadesTrintaInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal);
	List<AtividadeShort> listaCompromissosAtrasadosResponsavel(long usuario, String pesquisa);
	List<AtividadeShort> listaCompromissosHojeResponsavel(long usuario, String pesquisa);
	List<AtividadeShort> listaCompromissosSeteResponsavel(long usuario, String pesquisa);
	List<AtividadeShort> listaCompromissosTrintaResponsavel(long usuario, String pesquisa);
	int atividadesTotalUsuario(long usuario);
	int atividadeUltimosTrintaDias(long usuario);
	int atividadesConcluidasUltimasTrintaDias(long usuario);
	int atividadesConcluidasComAtrasoUltimosTrintaDias(long usuario);
	int atividadesConsluidasNoPrazoUltimosTrintaDias(long usuario);
	List<Agenda> agenda(FiltroAgenda filtro);
	List<Agenda> agendaHome(FiltroAgenda filtro);
	List<MiniAgendaDto> agendaPorMesUsuario(FiltroAgenda filtro);
	List<AgendaCalendarioHomeDto> agendaHomeResumo(FiltroAgenda filtro);
	List<DiasCalendario> dias(long mes, long ano);
	List<AtividadesGeralHomeDto> consultarAtividadesGeralHome(FiltroMiniAgenda filtro);
	//Kanban
	List<AtividadeShort> painelKanban(FiltroKanban filtro);
	List<AtividadesPontosDto> consultarAtividadesPorUsuarioResponsavel(long usuario, long empresa, String dataInicial, String dataFinal);
	//Abertura de Atividade mais performática
	AtividadeCustomDto consultarAtividadeCustom(long codigo, long empresa);
}
