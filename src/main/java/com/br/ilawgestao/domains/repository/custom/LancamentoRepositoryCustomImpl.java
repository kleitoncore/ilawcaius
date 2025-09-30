package com.br.ilawgestao.domains.repository.custom;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.br.ilawgestao.domains.dto.*;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.Lancamento;

public class LancamentoRepositoryCustomImpl implements LancamentoRepositoryCustom {
	
	@Autowired
	private EntityManager manager;

	@SuppressWarnings({ "unchecked"})
	@Override
	public List<LancamentoPorTipoDto> lancamantoPorTipoDespesaData(String dataInicial, String dataFinal, long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select m.cdmes codigo, m.mes mes,");
		sql.append("(select ROUND(COALESCE(sum(l.vllancamento),0),2) ");
		sql.append("from ilaw.lancamento l where l.cdtipo in(select cdtipo from ilaw.tipo_despesa_receita where cdempresa =:empresa and tipo = 'D')");
		sql.append(" and l.dtvencimento BETWEEN :dtIni and :dtFim ");
		sql.append(" and m.cdmes = month(l.dtvencimento) and m.ano = year(l.dtvencimento) and l.situacao <> 'C' ");
		sql.append(" and l.cdusuario in(select cdusuario from ilaw.usuario where cdempresa =:empresa)) total");
		sql.append(" from ilaw.mes_financeiro m ");
		sql.append("  where m.ano = year(:dtIni) ");
		
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(LancamentoPorTipoDto.class));
		
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		query.setParameter("empresa", empresa);
		
		List<LancamentoPorTipoDto> result = (List<LancamentoPorTipoDto>) query.getResultList();
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LancamentoPorTipoDto> lancamantoPorTipoReceitaData(String dataInicial, String dataFinal, long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select m.cdmes codigo, m.mes mes,");
		sql.append("(select ROUND(COALESCE(sum(l.vllancamento),0),2) ");
		sql.append("from ilaw.lancamento l where l.cdtipo in(select cdtipo from ilaw.tipo_despesa_receita where cdempresa =:empresa and tipo = 'R')");
		sql.append(" and l.dtvencimento BETWEEN :dtIni and :dtFim ");
		sql.append(" and m.cdmes = month(l.dtvencimento) and m.ano = year(l.dtvencimento) and l.situacao <> 'C' ");
		sql.append(" and l.cdusuario in(select cdusuario from ilaw.usuario where cdempresa =:empresa)) total");
		sql.append(" from ilaw.mes_financeiro m ");
		sql.append("  where m.ano = year(:dtIni)");
				
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(LancamentoPorTipoDto.class));
		
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		query.setParameter("empresa", empresa);
		
		List<LancamentoPorTipoDto> result = (List<LancamentoPorTipoDto>) query.getResultList();
		
		return result;
	}

	@Override
	public List<Lancamento> lancamentosPorTipoPeriodo(long empresa, String dataInicial, String dataFinal, String tipo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select l from Lancamento l where usuario.empresa.codigo =:empresa and dtVencimento >=:dataIni and dtVencimento <=:dataFim and l.tipo.tipo =:tipo and l.situacao <> 'C' order by l.dtVencimento");
		
		TypedQuery<Lancamento> query = manager.createQuery(sql.toString(), Lancamento.class);
		
		query.setParameter("empresa", empresa);
		query.setParameter("dataIni", dataInicial);
		query.setParameter("dataFim", dataFinal);
		query.setParameter("tipo", tipo);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LancamentoPorTipoGraficoDto> lancamentoGrafico(long empresa, String dataInicial, String dataFinal, String tipo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.notipo tipo, t.cor, sum(l.vllancamento) valor from lancamento l, tipo_despesa_receita t where t.cdtipo = l.cdtipo and t.cdempresa =:empresa ");
		sql.append(" and l.dtvencimento >=:dataIni and l.dtvencimento <=:dataFim and t.tipo =:tipo and l.situacao <> 'C' group by t.notipo, t.cor ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(LancamentoPorTipoGraficoDto.class));
		
		query.setParameter("empresa", empresa);
		query.setParameter("dataIni", dataInicial);
		query.setParameter("dataFim", dataFinal);
		query.setParameter("tipo", tipo);
		
		return query.getResultList();
	}

	@Override
	public List<Lancamento> lancamentosPorTipoDescricaoPeriodo(long empresa, String dataInicial, String dataFinal,
			long tipoCodigo, String tipo, String situacao) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select l from Lancamento l where usuario.empresa.codigo =:empresa and dtVencimento >=:dataIni ");
		sql.append(" and dtVencimento <=:dataFim and l.tipo.tipo =:tipo and l.situacao <> 'C' ");
		
		if(tipoCodigo != 0) {
			sql.append(" and l.tipo.codigo =:tipoCodigo ");
		}
		
		if(!situacao.equals("T")) {
			sql.append(" and l.situacao =:sit ");
		}
		
		sql.append(" order by l.dtVencimento ");
		
		TypedQuery<Lancamento> query = manager.createQuery(sql.toString(), Lancamento.class);
		
		
		query.setParameter("empresa", empresa);
		query.setParameter("dataIni", dataInicial);
		query.setParameter("dataFim", dataFinal);
		if(tipoCodigo != 0) {
			query.setParameter("tipoCodigo", tipoCodigo);
		}
		
		if(!situacao.equals("T")) {
			query.setParameter("sit", situacao);
		}
		
		query.setParameter("tipo", tipo);
		
		return query.getResultList();
	}

	@Override
	public List<Lancamento> relatorioDetalhado(long empresa, String dataInicial, String dataFinal, long tipo,
			String categoria, String situacao, long codigo, long processo, String descricao,
			String classificacao, String ordem) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select l from Lancamento l where l.usuario.empresa.codigo =:empresa ");
		
		//Filtra por datas de Lançamento
		if(!dataInicial.equals("S") && !dataFinal.equals("S")) {
			sql.append(" and l.dtVencimento >=:dataIni ");
			sql.append(" and l.dtVencimento <=:dataFim ");
		}
		
		//Filtra pelo tipo de despesa ou receita
		if(tipo != 0) {
			sql.append(" and l.tipo.codigo =:tipo ");
		}
		
		//Filtra por Categoria Despesa ou Receita
		if(!categoria.equals("T")) {
			sql.append(" and l.tipo.tipo =:categoria ");
		}
		
		//Filtra por situação - Ativos, Pagos ou Cancelados
		if(!situacao.equals("T")) {
			sql.append(" and l.situacao =:situacao ");
		}
		
		//Filtra pelo código do Lançamento
		if(codigo != 0) {
			sql.append(" and l.codigo =:codigo ");
		}
		
		//Filtra pelo processo
		if(processo != 0) {
			sql.append(" and l.processo.codigo =:processo ");
		}
		
		//Filtra por descrição do lançamento
		if(!descricao.equals("100")) {
			sql.append(" and l.dsLancamento like :descricao ");
		}
		
		//Classificação e Ordem
		if(classificacao.equals("TIPO")) {
			sql.append(" order by l.tipo.nome ");
		} else if(classificacao.equals("DATA")) {
			sql.append(" order by l.dtVencimento ");
		} else {
			sql.append(" order by l.vlLancamento ");
		}
		
		if(ordem.equals("DESC")) {
			sql.append(" desc");
		} 
		
		TypedQuery<Lancamento> query = manager.createQuery(sql.toString(), Lancamento.class);
		
		query.setParameter("empresa", empresa);
		
		if(!dataInicial.equals("S") && !dataFinal.equals("S")) {
			query.setParameter("dataIni", dataInicial);
			query.setParameter("dataFim", dataFinal);
		}
		
		if(tipo != 0) {
			query.setParameter("tipo", tipo);
		}
		if(!categoria.equals("T")) {
			query.setParameter("categoria", categoria);
		}
		if(!situacao.equals("T")) {
			query.setParameter("situacao", situacao);
		}
		
		if(codigo != 0) {
			query.setParameter("codigo", codigo);
		}
		
		if(processo != 0) {
			query.setParameter("processo", processo);
		}
		
		if(!descricao.equals("100")) {
			query.setParameter("descricao", "%" + descricao + "%");
		}
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelatorioFinanceiroResumidoDto> relatorioResumidoCategoria(long empresa, String dataInicial, String dataFinal, String situacao) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select t.tipo label, sum(l.vllancamento) valor from lancamento l, tipo_despesa_receita t ");
		sql.append(" where t.cdtipo = l.cdtipo and l.dtVencimento >=:dataIni and l.dtVencimento <=:dataFim ");
		
		if(!situacao.equals("T")) {
			sql.append(" and l.situacao =:situacao");
		}
		sql.append(" group by t.tipo ");
		sql.append(" order by t.tipo ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(RelatorioFinanceiroResumidoDto.class));
		
		if(!situacao.equals("T")) {
			query.setParameter("situacao", situacao);
		}
		
		query.setParameter("dataIni", dataInicial);
		query.setParameter("dataFim", dataFinal);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RelatorioFinanceiroResumidoDto> relatorioResumidoTipo(long empresa, String dataInicial, String dataFinal, String situacao) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select t.notipo label, sum(l.vllancamento) valor from lancamento l, tipo_despesa_receita t ");
		sql.append(" where t.cdtipo = l.cdtipo and l.dtVencimento >=:dataIni and l.dtVencimento <=:dataFim ");
		
		if(!situacao.equals("T")) {
			sql.append(" and l.situacao =:situacao");
		}
		sql.append(" group by t.notipo ");
		sql.append(" order by t.notipo ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(RelatorioFinanceiroResumidoDto.class));
		
		if(!situacao.equals("T")) {
			query.setParameter("situacao", situacao);
		}
		
		query.setParameter("dataIni", dataInicial);
		query.setParameter("dataFim", dataFinal);
		
		return query.getResultList();
	}

	@SuppressWarnings("deprecation")
	@Override
	public FinanceiroVencendoDto vencendoAmanha(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select COALESCE(sum(l.vllancamento),0) vencendo from lancamento l where date(dtvencimento) = date(curdate() + interval 1 day) and l.cdusuario in(select cdusuario from usuario where cdempresa =:empresa)");
		
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(FinanceiroVencendoDto.class));
		
		query.setParameter("empresa", empresa);
		
		return (FinanceiroVencendoDto) query.getSingleResult();
	}

	@SuppressWarnings("deprecation")
	@Override
	public FinanceiroVencendoDto vencendoHoje(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select COALESCE(sum(l.vllancamento),0) vencendo ");
		sql.append("  from lancamento l ");
		sql.append(" where date(l.dtvencimento) = date(curdate()) ");
		sql.append("   and l.cdusuario in(select cdusuario from usuario where cdempresa =:empresa)");
		sql.append("   and l.situacao = 'A' ");
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(FinanceiroVencendoDto.class));
		
		query.setParameter("empresa", empresa);
		
		return (FinanceiroVencendoDto) query.getSingleResult();
	}

	@SuppressWarnings("deprecation")
	@Override
	public FinanceiroVencendoDto pendentes(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select COALESCE(sum(l.vllancamento),0) vencendo from lancamento l where date(dtvencimento) < date(curdate()) and l.situacao = 'A' and l.cdusuario in(select cdusuario from usuario where cdempresa =:empresa)");
		
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(FinanceiroVencendoDto.class));
		
		query.setParameter("empresa", empresa);
		
		return (FinanceiroVencendoDto) query.getSingleResult();
	}

	@Override
	public List<LancamentosPorContaFinanceiraDto> lancamentosPagosPorContaFinanceira(String dataInicial, String dataFinal, long empresa, String tipo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select cf.noconta conta, COALESCE(sum(l.vllancamento),0) valor " );
		sql.append("   from lancamento l, conta_financeira cf, transacao_financeira tf, tipo_despesa_receita tdr ");
		sql.append("  where l.cdlancamento = tf.cdlancamento ");
		sql.append("    and cf.cdconta = tf.cdconta_financeira ");
		sql.append("    and tdr.cdtipo = l.cdtipo and tdr.tipo = :tipo ");
		sql.append("    and l.cdusuario in(select cdusuario from usuario where cdempresa =:empresa) ");
		sql.append("    and l.dtpago >= :dtini and l.dtpago <= :dtfim ");
		sql.append("  group by cf.noconta ");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(LancamentosPorContaFinanceiraDto.class));

		query.setParameter("empresa", empresa);
		query.setParameter("dtini", dataInicial);
		query.setParameter("dtfim", dataFinal);
		query.setParameter("tipo", tipo);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> lancamentosPendente(long empresa, long tipoCodigo, String tipo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select l.* from lancamento l, tipo_despesa_receita t where t.cdtipo = l.cdtipo and date(dtvencimento) < date(curdate()) and l.situacao = 'A' ");
		sql.append(" and l.cdusuario in(select cdusuario from usuario where cdempresa =:empresa)");
		
		if(tipo != null) {
			sql.append(" and t.tipo = '" + tipo + "'");
		}
		
		if(tipoCodigo != 0) {
			sql.append(" and l.cdtipo = " + tipoCodigo);
		}
		
		sql.append(" order by l.dtvencimento desc ");
		
		Query query = manager.createNativeQuery(sql.toString(), Lancamento.class);
		query.setParameter("empresa", empresa);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> lancamentosVencendoHoje(long empresa, long tipoCodigo, String tipo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select l.* from lancamento l, tipo_despesa_receita t where t.cdtipo = l.cdtipo and date(dtvencimento) = date(curdate()) and l.situacao = 'A' ");
		sql.append(" and l.cdusuario in(select cdusuario from usuario where cdempresa =:empresa)");
		
		if(tipo != "") {
			sql.append(" and t.tipo '" + tipo + "'");
		}
		
		if(tipoCodigo != 0) {
			sql.append(" and l.cdtipo = " + tipoCodigo);
		}
		
		sql.append(" order by l.dtvencimento desc ");
		
		Query query = manager.createNativeQuery(sql.toString(), Lancamento.class);
		query.setParameter("empresa", empresa);
		
		return query.getResultList();
	}

}
