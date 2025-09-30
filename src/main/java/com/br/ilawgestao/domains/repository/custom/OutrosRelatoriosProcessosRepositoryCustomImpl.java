package com.br.ilawgestao.domains.repository.custom;

import java.util.List;


import com.br.ilawgestao.domains.dto.relatorios.ContingenciaDto;
import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioProcesso;

public class OutrosRelatoriosProcessosRepositoryCustomImpl implements OutrosRelatoriosProcessosRepositoryCustom {
	

	@Override
	public List<ContingenciaDto> relatorioContingencia(FiltroRelatorioProcesso filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(p.vlprovavel) provavel, sum(p.vlpossivel) possivel, sum(p.vlremoto) remoto, sum(p.vlcausa) causa ");
		sql.append(" from processo p ");
		//Autores
		sql.append(" left join partes pat on (p.cdprocesso = pat.cdprocesso and pat.tpparte = 'A')");
		sql.append(" left join pessoa peat on (peat.cdpessoa = pat.cdpessoa and peat.cdempresa = p.cdempresa)");
		//Réus
		sql.append(" left join partes preu on (p.cdprocesso = preu.cdprocesso and preu.tpparte = 'R')");
		sql.append(" left join pessoa pereu on (preu.cdpessoa = pereu.cdpessoa and pereu.cdempresa = p.cdempresa)");
		//Grupos de Trabalho
		sql.append(" left join grupo_trabalho gt on (gt.cdgrupo = p.cdgrupo and gt.cdempresa = p.cdempresa)");
		
		//Filtros
		//Filtrar por pessoas
		if(!filtro.getPessoa().equals("0")) {
			//Procura por Autores, Réus e Advogados parte contrária
			sql.append(" and (peat.cdpessoa in(" + filtro.getPessoa() + ") or pereu.cdpessoa in(" + filtro.getPessoa() + ") or peadv.cdpessoa in(" + filtro.getPessoa() + "))");
		}
		
		//Filtrar por Grupo de Trabalho
		if(!filtro.getGrupoTrabalho().equals("0")) {
			sql.append(" and gt.cdgrupo in(" + filtro.getGrupoTrabalho() + ")");
		}
		
		return null;
	}

}
