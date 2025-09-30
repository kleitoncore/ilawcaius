package com.br.ilawgestao.domains.repository.custom;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.Atendimento;
import com.br.ilawgestao.domains.utils.DatasUtil;

public class AtendimentoRepositoryCustomImpl implements AtendimentoRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<Atendimento> listarAtendimentos(long empresa, String dataInicial, String dataFinal, long pessoa,
			long grupo) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select a from Atendimento a where empresa.codigo =:empresa and dtAtendimento >= :dataIni and  dtAtendimento <= :dataFim ");
		
		if(pessoa != 0) {
			sql.append(" and pessoa.codigo =:pessoa ");
		}
		
		if(grupo != 0) {
			sql.append(" and grupo.codigo =:grupo ");
		}
		
		sql.append(" order by dtAtendimento desc ");
		
		TypedQuery<Atendimento> query = manager.createQuery(sql.toString(), Atendimento.class);
		
		query.setParameter("empresa", empresa);
		
		if(dataInicial.equals("") || dataFinal.equals("")) {
			Calendar calendar = Calendar.getInstance();  
			calendar.set(Calendar.MONTH, 0); //Definindo o mês atual para Fevereiro
			
			String strDataInicial = "";
			String strDataFinal = "";
			String mesAtual = DatasUtil.getMesAtual();
			String anoAtual = DatasUtil.getAnoAtual();
			strDataInicial = anoAtual + "-" + mesAtual + "-01";
			strDataFinal = anoAtual + "-" + mesAtual + "-" + calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			query.setParameter("dataIni", strDataInicial);
			query.setParameter("dataFim", strDataFinal);
			
		} else {
			
			query.setParameter("dataIni", dataInicial);
			query.setParameter("dataFim", dataFinal);
		}
		
		if(pessoa != 0) {
			query.setParameter("pessoa", pessoa);
		}
		
		if(grupo != 0) {
			query.setParameter("grupo", grupo);
		}
		
		return query.getResultList();
	}
}
