package com.br.ilawgestao.domains.repository.custom;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioAtividades;

public class RelatorioAtividadeRepositoryCustomImpl implements RelatorioAtividadeRepositoryCustom {

	@Autowired
	private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Atividade> relatorioAtividades(FiltroRelatorioAtividades filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.* ");
		sql.append("from atividade a ");
		sql.append("left join grupo_trabalho gf on gf.cdgrupo = a.cdsubgrupo ");
		sql.append("left join grupo_trabalho gp on gp.cdgrupo = gf.cdgrupo_pai ");
		sql.append(" where a.cdusuario in(select cdusuario from usuario where cdempresa = " + filtro.getEmpresa() + ")");
		
		//Consulta por data de limite da atividade
		if(!filtro.getDataLimiteInicial().equals("0") && !filtro.getDataLimiteFinal().equals("O")) {
			sql.append(" and date(a.dtlimite) >= '" + filtro.getDataLimiteInicial() + "'");
			sql.append(" and date(a.dtlimite) <= '" + filtro.getDataLimiteFinal() + "'");
		}
			
		//Data Registro
		if(!filtro.getDataCriacaoInicial().equals("0") && !filtro.getDataCriacaoFinal().equals("0")) {
			sql.append(" and date(a.dtregistro) >= '" + filtro.getDataCriacaoInicial() + "'");
			sql.append(" and date(a.dtregistro) <= '" + filtro.getDataCriacaoFinal() + "'");
		}
		
		//Data de Conclusão
		if(!filtro.getDataConcluidoInicial().equals("0") && !filtro.getDataConcluidoFinal().equals("0")) {
			sql.append(" and date(a.dtconcluido) >= '" + filtro.getDataConcluidoInicial() + "'");
			sql.append(" and date(a.dtconcluido) <= '" + filtro.getDataConcluidoFinal() + "'");
		}
		
		//Grupo de Trabalho
		if(filtro.getGrupoTrabalho() != 0) {
			sql.append(" and a.cdsubgrupo in(select cdgrupo from grupo_trabalho where cdgrupo_pai = " + filtro.getGrupoTrabalho() + ")");
		}
		
		//Responsável
		if(filtro.getResponsavel() != 0) {
			sql.append(" and a.cdatividade in(select cdatividade from atividade_usuario where cdusuario = " + filtro.getResponsavel() + " and tpusuario = 'R')");
		}
		
		//Interessado
		if(filtro.getInteressado() != 0) {
			sql.append(" and a.cdatividade in(select cdatividade from atividade_usuario where cdusuario = " + filtro.getInteressado() + " and tpusuario = 'I')");
		}
		
		//Status
		if(!filtro.getStatus().equals("0")) {
			sql.append(" and a.cdstatus in(" + filtro.getStatus() + ")");
		}
		
		//Tipo de Atividade
		if(!filtro.getTipoAtividade().equals("0")) {
			sql.append(" and a.tpatividade = '" + filtro.getTipoAtividade() + "'");
		}
		
		//Por data de Alteração de status
		if(!filtro.getDataAlteracaoInicial().equals("0") && !filtro.getDataAlteracaoFinal().equals("0")) {
			sql.append(" and a.cdatividade in(select cdatividade from historico_atividade where dshistorico like '%alterad%' ");
			sql.append(" and dthistorico >= '" + filtro.getDataAlteracaoInicial() + "' and dthistorico <= '" + filtro.getDataAlteracaoFinal() + "') ");
		}
		
		//Por Prazo Fatal
		if(!filtro.getDataFatalInicial().equals("0") && !filtro.getDataFatalFinal().equals("0")) {
			sql.append(" and date(a.dtfatal) >= '" + filtro.getDataFatalInicial() + "'");
			sql.append(" and date(a.dtfatal) <= '" + filtro.getDataFatalFinal() + "'");
		}
		
		//Ordenação e Classificação
		if(filtro.getClassificacao() == 1) {
			sql.append(" order by a.dtlimite ");
		} else if(filtro.getClassificacao() == 2) {
			sql.append(" order by dtconcluido ");
		} else if(filtro.getClassificacao() == 3) {
			sql.append(" order by dtregistro ");
		}
		
		if(filtro.getOrdenacao() == 1) {
			sql.append(" asc");
		} else {
			sql.append(" desc");
		}
		
		Query query = manager.createNativeQuery(sql.toString(), Atividade.class);
		
		return query.getResultList();
	}
}
