package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.dto.RelatorioProcessoDto;
import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioProcesso;

public class RelatorioProcessoCustomImpl implements RelatorioProcessoCustom {
	
	@Autowired
	private EntityManager manager;

	@SuppressWarnings("unchecked")
	@Override
	public List<RelatorioProcessoDto> relatorioProcesso(FiltroRelatorioProcesso filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.cdprocesso codigo, group_concat(distinct peat.nopessoa) autores, group_concat(distinct pereu.nopessoa) reus, group_concat(distinct peadv.nopessoa) advogados,");
		sql.append("gt.nogrupo grupoTrabalho, sp.descricao statusProcessual, ta.notipo tipoAcao, at.noarea areaAtuacao, td.notipo tipoDecisao, ");
		// Pagamentos
		sql.append("group_concat(distinct tp.notipo) pagamentos, IFNULL(sum(distinct pp.vlpagamento),0) valorPagamentos,");
		// Custas
		sql.append("group_concat(distinct tc.notipo) custas, IFNULL(sum(distinct cp.vlcustas),0) valorCustas,");
		//Pedidos
		sql.append("group_concat(distinct ped.nopedido) pedidos, IFNULL(sum(distinct pedp.vlprovavel),0) valorProvavel, ");
		sql.append("IFNULL(sum(distinct pedp.vlpossivel),0) valorPossivel, IFNULL(sum(distinct pedp.vlremoto),0) valorRemoto, ");
		sql.append("p.vlcausa valorCausa, ");
		sql.append("p.nrprocesso numeroProcesso, p.nrcnj numeroCnj, p.pasta pasta, p.dtdistribuicao dataDistribuicao, p.dtultima_decisao dataUltimaDecisao, ");
		sql.append("p.dtultima_movimentacao dataUltimaMovimentacao ,p.dtsentenca dataSentenca, p.dscomarca comarca, parq.dtarquivamento dataArquivamento, ");
		sql.append("p.snpush snPush, p.snemail snEmail, p.snhistorico snHistorico, p.snimportante importanteEmpresa, p.snestrategico estrategico, usuimp.nousuario importanteParaMim, p.dtcadastro dataCadastro, ");
		sql.append("usu.nousuario responsavel, p.dsobservacao observacao, banca.nopessoa banca, ");
		// Atividades
		sql.append("atv.dstitulo ultimaAtividade, date(atv.dtlimite) dataUltimaAtividade,");
		// Histórico do Processo
		sql.append("hp.dshistorico ultimoHistorico, hp.dthistorico dataUltimoHistorico,");
		sql.append("group_concat(distinct oa.noobjeto) objetos, ");
		sql.append("p.uf uf, fase.nofase fase, rito.norito rito, mr.nomotivo motivoResultado ");
		sql.append(" from processo p ");
		//Autores
		sql.append(" left join partes pat on (p.cdprocesso = pat.cdprocesso and pat.tpparte = 'A')");
		sql.append(" left join pessoa peat on (peat.cdpessoa = pat.cdpessoa and peat.cdempresa = p.cdempresa)");
		//Réus
		sql.append(" left join partes preu on (p.cdprocesso = preu.cdprocesso and preu.tpparte = 'R')");
		sql.append(" left join pessoa pereu on (preu.cdpessoa = pereu.cdpessoa and pereu.cdempresa = p.cdempresa)");
		//Outras Partes
		sql.append(" left join partes padv on (p.cdprocesso = padv.cdprocesso and padv.tpparte not in('A','R'))");
		sql.append(" left join pessoa peadv on (padv.cdpessoa = peadv.cdpessoa and peadv.cdempresa = p.cdempresa)");
		//Grupos de Trabalho
		sql.append(" left join grupo_trabalho gt on (gt.cdgrupo = p.cdgrupo and gt.cdempresa = p.cdempresa)");
		//Status processual
		sql.append(" left join status_processual sp on (sp.cdstatus = p.cdstatus_processual and sp.cdempresa = p.cdempresa)");
		//Tipo de Ação
		sql.append(" left join tipo_acao ta on(ta.cdtipo = p.cdtipo_acao and ta.cdempresa = p.cdempresa)");
		//Área de Ação
		sql.append(" left join area_atuacao at on(at.cdarea = p.cdarea_atuacao and at.cdempresa = p.cdempresa)");
		//Pagamentos
		sql.append(" left join pagamento_processo pp on (p.cdprocesso = pp.cdprocesso)");
		sql.append(" left join tipo_pagamento tp on(tp.cdtipo = pp.cdpagamento and tp.cdempresa = p.cdempresa)");
		//Custas
		sql.append(" left join custas_processo cp on(p.cdprocesso = cp.cdprocesso)");
		sql.append(" left join tipo_custa tc on(tc.cdtipo = cp.cdcustas and tc.cdempresa = p.cdempresa)");
		//Associando com a última atividade
		sql.append(" left join atividade atv on(atv.cdprocesso = p.cdprocesso and atv.cdatividade = (select max(a2.cdatividade) from atividade a2 where a2.cdprocesso = p.cdprocesso))");
		//Último histórico
		sql.append(" left join historico_processo hp on(hp.cdprocesso = p.cdprocesso and hp.cdhistorico = (select max(h2.cdhistorico) from historico_processo h2 where h2.cdprocesso = p.cdprocesso))");
		//Objetos de Ação
		sql.append(" left join objeto_acao_processo oap on (oap.cdprocesso = p.cdprocesso)");
		sql.append(" left join objeto_acao oa on(oa.cdobjeto = oap.cdobjeto and oa.cdempresa = p.cdempresa)");
		//Processos importantes para o usuário
		sql.append(" left join processo_importancia pi on (p.cdprocesso = pi.cdprocesso)");
		sql.append(" left join usuario usuimp on (usuimp.cdusuario = pi.cdusuario) ");
		//Tipo de Decisao
		sql.append(" left join tipo_decisao td on (td.cdtipo = p.cdtipo_decisao and td.cdempresa = p.cdempresa) ");
		//Pedidos
		sql.append(" left join pedido_processo pedp on (p.cdprocesso = pedp.cdprocesso) ");
		sql.append(" left join pedido ped on(ped.cdpedido = pedp.cdpedido and p.cdempresa = ped.cdempresa) ");
		//Fase
		sql.append(" left join tipo_fase fase on(fase.cdfase = p.cdfase and p.cdempresa = fase.cdempresa) ");
		//Rito
		sql.append(" left join tipo_rito rito on(rito.cdrito = p.cdrito and p.cdempresa = rito.cdempresa) ");
		//Motivo de Resultadi
		sql.append(" left join motivo_resultado mr on(mr.cdmotivo = p.cdmotivo_resultado and p.cdempresa = mr.cdempresa) ");
		//Processos Arquivados
		sql.append(" left join processo_arquivado parq on p.cdprocesso = parq.cdprocesso ");
		//Responsável
		sql.append(" left join usuario usu on (usu.cdusuario = p.cdresponsavel) ");
		//Bancada de Asvogados
		sql.append(" left join pessoa banca on (banca.cdpessoa = p.cdbancada) ");
		//Condição
		sql.append(" where p.cdempresa =:empresa and p.cdstatus = 0 ");
		
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
		
		//Filtra por Tipo de Ação
		if(filtro.getTipoAcao() != 0) {
			sql.append(" and ta.cdtipo = " + filtro.getTipoAcao());
		}
		
		// Filtra por Área de Atuação
		if(filtro.getAreaAtuacao() != 0) {
			sql.append(" and p.cdarea_atuacao = " + filtro.getAreaAtuacao());
		}
		
		//Filtra por Tipo de Decisão
		if(filtro.getTipoDecisao() != 0) {
			sql.append(" and td.cdtipo = " + filtro.getTipoDecisao());
		}
		
		//Data da última decisão
		if(!filtro.getDataInicialDecisao().equals("0") && !filtro.getDataFinalDecisao().equals("0")) {
			sql.append(" and p.dtultima_decisao >= '" + filtro.getDataInicialDecisao() + "'");
			sql.append(" and p.dtultima_decisao <= '" + filtro.getDataFinalDecisao() + "'");
		}
		
		// Data de Distribuição
		if(filtro.getDataInicialDistribuicao().equals("0") && !filtro.getDataFinalDistribuicao().equals("0")) {
			sql.append(" and p.dtdistribuicao >= '" + filtro.getDataInicialDistribuicao() + "'");
			sql.append(" and p.dtdistribuicao <= '" + filtro.getDataFinalDistribuicao() + "'");
		}
		
		//Filtra por Status Processual
		if(filtro.getStatusProcessual() != 0) {
			sql.append(" and p.cdstatus_processual = " + filtro.getStatusProcessual());
		}
		
		//Excetos processos arquivados
		//if(filtro.isExcetoProcessosArquivados()) {
			//sql.append(" and sp.descricao <> 'Arquivado'");
		//}
		
		//Data da Sentença
		if(!filtro.getDataInicialSentenca().equals("0") && !filtro.getDataFinalSentenca().equals("0")) {
			sql.append(" and p.dtsentenca >= '" + filtro.getDataInicialSentenca() + "'");
			sql.append(" and p.dtsentenca <= '" + filtro.getDataFinalSentenca() + "'");
		}
		
		//Objetos de Ação
		if(!filtro.getObjetoAcao().equals("0")) {
			sql.append(" and oa.cdobjeto in(" + filtro.getObjetoAcao() + ")");
		}
		
		//Pagamentos
		if(!filtro.getPagamentos().equals("0")) {
			sql.append(" and tp.cdtipo in(" + filtro.getPagamentos() + ")");
		}
		
		//Custas
		if(!filtro.getCustas().equals("0")) {
			sql.append(" and tc.cdtipo in(" + filtro.getCustas() + ")");
		}
		
		//Data de Cadastro
		if(!filtro.getDataInicialCadastro().equals("0") && !filtro.getDataFinalCadastro().equals("0")) {
			sql.append(" and p.dtcadastro >= '" + filtro.getDataInicialCadastro() + "'");
			sql.append(" and p.dtcadastro <= '" + filtro.getDataFinalCadastro() + "'");
		}
		
		//Processos Alterados
		if(!filtro.getDataInicialAlteracao().equals("0") && !filtro.getDataFinalAlteracao().equals("0")) {
			sql.append(" and p.dtultima_movimentacao >= '" + filtro.getDataInicialAlteracao() + "'");
			sql.append(" and p.dtultima_movimentacao <= '" + filtro.getDataFinalAlteracao() + "'");
		}
		
		//Importante para Empresa
		if(!filtro.getImportanteEmpresa().equals("0")) {
			sql.append(" and p.snimportante = '" + filtro.getImportanteEmpresa() + "'");
		}
		
		//Estratégico
		if(!filtro.getEstrategico().equals("0")) {
			sql.append(" and p.snestrategico = '" + filtro.getEstrategico() + "'");
		}
		
		//Importante para mim
		if(!filtro.getImportanteParaMim().equals("0")) {
			sql.append(" and p.cdprocesso in(select cdprocesso from processo_importancia where cdusuario = " + filtro.getUsuario() + ")");
		}
		
		//Pedidos
		if(!filtro.getPedidos().equals("0")) {
			sql.append(" and ped.cdpedido in(" + filtro.getPedidos() + ")");
		}
		
		//UF
		if(!filtro.getUf().equals("0")) {
			sql.append(" and p.uf = '" + filtro.getUf() + "'");
		}
		
		//Fase
		if(filtro.getFase() != 0) {
			sql.append(" and fase.cdfase = " + filtro.getFase());
		}
		
		//Rito
		if(filtro.getRito() != 0) {
			sql.append(" and rito.cdrito = " + filtro.getRito());
		}
		
		//Motivo de Resultado
		if(filtro.getMotivoResultado() != 0) {
			sql.append(" and mr.cdmotivo = " + filtro.getMotivoResultado());
		}
		
		//Responsável
		if(filtro.getResponsavel() != 0) {
			sql.append(" and p.cdresponsavel = " + filtro.getResponsavel());
		}
		
		//Bancada de Advogados
		if(filtro.getBanca() != 0) {
			sql.append(" and p.cdbancada = " + filtro.getBanca());
		}
		
		//Valor das Custas
		if(filtro.getValorCustas() != 0) {
			if(filtro.getOperadorLogicoCustas().equals("IGUAL")) {
				sql.append(" and cp.vlcustas = " + filtro.getValorCustas());
			} else if(filtro.getOperadorLogicoCustas().equals("MAIOR")) {
				sql.append(" and cp.vlcustas > " + filtro.getValorCustas());
			} else if(filtro.getOperadorLogicoCustas().equals("MAIORIGUAL")) {
				sql.append(" and cp.vlcustas >= " + filtro.getValorCustas());
			} else if(filtro.getOperadorLogicoCustas().equals("MENOR")) {
				sql.append(" and cp.vlcustas < " + filtro.getValorCustas());
			} else if(filtro.getOperadorLogicoCustas().equals("MENORIGUAL")) {
				sql.append(" and cp.vlcustas <= " + filtro.getValorCustas());
			}
		}
		
		//Valor dos Pagamentos
		if(filtro.getValorPagamento() != 0) {
			if(filtro.getOperadorLogicoPagamento().equals("IGUAL")) {
				sql.append(" and pp.vlpagamanto = " + filtro.getValorPagamento());
			} else if(filtro.getOperadorLogicoPagamento().equals("MAIOR")) {
				sql.append(" and pp.vlpagamento > " + filtro.getValorPagamento());
			} else if(filtro.getOperadorLogicoPagamento().equals("MAIORIGUAL")) {
				sql.append(" and pp.vlpagamento >= " + filtro.getValorPagamento());
			} else if(filtro.getOperadorLogicoPagamento().equals("MENOR")) {
				sql.append(" and pp.vlpagamento < " + filtro.getValorPagamento());
			} else if(filtro.getOperadorLogicoPagamento().equals("MENORIGUAL")) {
				sql.append(" and pp.vlpagamento <= " + filtro.getValorPagamento());
			}
		}
		
		//Processos Arquivados
		if(!filtro.getDataArquivamentoInicial().equals("0") && !filtro.getDataArquivamentoFinal().equals("0")) {
			sql.append(" and parq.dtarquivamento >= '" + filtro.getDataArquivamentoInicial() + "'");
			sql.append(" and parq.dtarquivamento <= '" + filtro.getDataArquivamentoFinal() + "'");
		}
		
		//Agrupa
		sql.append(" group by p.cdprocesso, gt.nogrupo, sp.descricao, ta.notipo, at.noarea, tp.notipo, p.nrprocesso, p.nrcnj, p.pasta,");
		sql.append(" 		  p.dtdistribuicao, p.dtultima_decisao, p.dtultima_movimentacao,");
		sql.append("          p.snpush, p.snemail, p.snhistorico, p.snimportante, atv.dstitulo, hp.dshistorico,");
		sql.append("          p.dtsentenca, p.dscomarca, p.dspedidos, atv.dtlimite, hp.dthistorico, p.dtcadastro, p.vlcausa,");
		sql.append("          p.uf, fase.nofase, rito.norito, mr.nomotivo, parq.dtarquivamento, p.snimportante, p.snestrategico, usuimp.nousuario, ");
		sql.append("          usu.nousuario, p.dsobservacao, banca.nopessoa ");
		
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(RelatorioProcessoDto.class));
		
		query.setParameter("empresa", filtro.getEmpresa());
		
		List<RelatorioProcessoDto> result = (List<RelatorioProcessoDto>) query.getResultList();
		
		return result;
	}

}
