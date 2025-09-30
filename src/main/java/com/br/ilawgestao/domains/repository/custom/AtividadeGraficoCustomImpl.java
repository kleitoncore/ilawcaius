package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.AtividadeShort;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;

public class AtividadeGraficoCustomImpl implements AtividadeGraficoCustom {
	
	@Autowired
	private EntityManager manager;

	@SuppressWarnings({ "unchecked"})
	@Override
	public List<GraficoAtividadeDto> graficoAtividades(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select m.cdmes codigo, m.mes mes, ");
		sql.append(" (select count(a.cdatividade) from ilaw.atividade a ");
		sql.append("    where a.dtlimite BETWEEN :dtIni and :dtFim ");
		sql.append("      and m.cdmes = month(a.dtlimite) ");
		sql.append("      and a.cdusuario in(select cdusuario from ilaw.usuario where cdempresa =:empresa)) total ");
		sql.append(" from ilaw.mes m ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadeDto.class));
		
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		query.setParameter("empresa", empresa);
		
		List<GraficoAtividadeDto> result = (List<GraficoAtividadeDto>) query.getResultList();
		
		return result;
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public List<GraficoAtividadeDto> graficoAtividadesDataLimite(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select m.cdmes codigo, m.mes mes, COALESCE(count(a.cdatividade),0) total ");
		sql.append(" from atividade a right join mes m on m.cdmes = month(a.dtlimite) and m.ano = year(a.dtlimite) "); 
		sql.append(" where m.cdmes BETWEEN month(:dtIni) and month(:dtFim) and (a.cdusuario in(select cdusuario from usuario where cdempresa =:empresa) or a.cdusuario is null) ");
		sql.append(" group by m.cdmes, m.mes ");
		sql.append(" order by m.cdmes ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadeDto.class));
		
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		query.setParameter("empresa", empresa);
		
		List<GraficoAtividadeDto> result = (List<GraficoAtividadeDto>) query.getResultList();
		
		return result;
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public List<GraficoAtividadeDto> graficoAtividadesConcluidos(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select m.cdmes codigo, m.mes mes, COALESCE(count(a.cdatividade),0) total ");
		sql.append(" from atividade a right join mes m on m.cdmes = month(a.dtconcluido) and m.ano = year(a.dtconcluido) "); 
		sql.append(" where m.cdmes BETWEEN month(:dtIni) and month(:dtFim) and (a.cdusuario in(select cdusuario from usuario where cdempresa =:empresa) or a.cdusuario is null) ");
		sql.append(" group by m.cdmes, m.mes ");
		sql.append(" order by m.cdmes ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadeDto.class));
		
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		query.setParameter("empresa", empresa);
		
		List<GraficoAtividadeDto> result = (List<GraficoAtividadeDto>) query.getResultList();
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GraficoAtividadesStatusDto> graficoAtividadesStatus(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sa.nostatus status, ");
		sql.append(" count(a.cdatividade) total ");
		sql.append(" from atividade a, status_atividade sa where sa.cdstatus = a.cdstatus ");
		sql.append(" and a.dtLimite >=:dtIni and a.dtLimite <=:dtFim and a.cdusuario in(select cdusuario from usuario where cdempresa =:empresa) ");
		sql.append(" group by sa.nostatus ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadesStatusDto.class));
		
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		query.setParameter("empresa", empresa);
		
		List<GraficoAtividadesStatusDto> result = (List<GraficoAtividadesStatusDto>) query.getResultList();
			
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GraficoAtividadeSituacaoDto> graficoAtividadesSituacao(long empresa, String dataInicial,
			String dataFinal) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(atra.atividades) atrasados, sum(catra.atividades) concluidosAtrasados, sum(cprazo.atividades) concluidosPrazo "); 
		sql.append("from usuario u ");
		//Atrasados
		sql.append(" left join ( ");
		sql.append("select u.cdusuario, count(a.cdatividade) atividades ");
		sql.append(" from usuario u, atividade a, atividade_usuario au ");
		sql.append(" where u.cdusuario = au.cdusuario ");
		sql.append(" and a.cdatividade = au.cdatividade ");
		sql.append(" and au.tpusuario = 'R' ");
		sql.append(" and date(a.dtlimite) < curdate() and a.dtconcluido is null ");
		sql.append(" and a.cdusuario in(select cdusuario from usuario where cdempresa =:empresa) and a.dtlimite >=:dtIni and a.dtlimite <=:dtFim ");
		sql.append(" group by u.cdusuario ");
		sql.append(") atra on u.cdusuario = atra.cdusuario ");
		//Concluidos com atraso
		sql.append(" left join ( ");
		sql.append("select u.cdusuario, count(a.cdatividade) atividades ");
		sql.append(" from usuario u, atividade a, atividade_usuario au ");
		sql.append(" where u.cdusuario = au.cdusuario ");
		sql.append(" and a.cdatividade = au.cdatividade ");
		sql.append(" and au.tpusuario = 'R' ");
		sql.append(" and date(a.dtconcluido) > date(a.dtlimite) ");
		sql.append(" and a.cdusuario in(select cdusuario from usuario where cdempresa =:empresa) and a.dtlimite >=:dtIni and a.dtlimite <=:dtFim ");
		sql.append(" group by u.cdusuario ");
		sql.append(") catra on u.cdusuario = catra.cdusuario ");
		//Concluídos no Prazo
		sql.append(" left join ( ");
		sql.append("select u.cdusuario, count(a.cdatividade) atividades ");
		sql.append(" from usuario u, atividade a, atividade_usuario au ");
		sql.append(" where u.cdusuario = au.cdusuario ");
		sql.append(" and a.cdatividade = au.cdatividade ");
		sql.append(" and au.tpusuario = 'R' ");
		sql.append(" and date(a.dtconcluido) <= date(a.dtlimite) ");
		sql.append(" and a.cdusuario in(select cdusuario from usuario where cdempresa =:empresa) and a.dtlimite >=:dtIni and a.dtlimite <=:dtFim ");
		sql.append(" group by u.cdusuario ");
		sql.append(") cprazo on u.cdusuario = cprazo.cdusuario ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadeSituacaoDto.class));
		
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		query.setParameter("empresa", empresa);
		
		List<GraficoAtividadeSituacaoDto> result = (List<GraficoAtividadeSituacaoDto>) query.getResultList();
		
		return result;
		
	}

	@Override
	public List<GraficoAtividadesStatusDto> graficoAtividadesStatusUsuarioPainel(long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.status status, ");
		sql.append("       sa.cor cor, ");
		sql.append(" count(a.cdatividade) total ");
		sql.append(" from atividade_short a, status_atividade sa ");
		sql.append(" where sa.cdstatus = a.cdstatus and a.cdusuario =:usuario and a.status not in('Concluído','Cancelada') ");
		sql.append(" group by a.status, sa.cor ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadesStatusDto.class));
		
		query.setParameter("usuario", usuario);
		
		@SuppressWarnings("unchecked")
		List<GraficoAtividadesStatusDto> result = (List<GraficoAtividadesStatusDto>) query.getResultList();
			
		return result;
	}

	@Override
	public List<GraficoAtividadeGrupoDto> graficoAtividadeGrupo(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select gtp.nogrupo grupo, count(a.cdatividade) total from atividade a, grupo_trabalho gt, grupo_trabalho gtp ");
		sql.append(" where gt.cdgrupo = a.cdsubgrupo and gt.cdgrupo_pai = gtp.cdgrupo and a.cdusuario in(select cdusuario from usuario where cdempresa =:empresa) ");
		sql.append(" and a.dtlimite >=:dtIni and a.dtlimite <=:dtFim and a.cdstatus <> 3 ");
		sql.append(" group by gtp.nogrupo order by count(a.cdatividade) desc limit 10 ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadeGrupoDto.class));
		
		query.setParameter("empresa", empresa);
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		
		@SuppressWarnings("unchecked")
		List<GraficoAtividadeGrupoDto> result = (List<GraficoAtividadeGrupoDto>) query.getResultList();
			
		return result;
	}

	@Override
	public int graficoAtividadeGruposOutros(long empresa, String grupos, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(a.cdatividade) from atividade a, grupo_trabalho gt, grupo_trabalho gtp ");
		sql.append(" where gt.cdgrupo = a.cdsubgrupo and gtp.cdgrupo = gt.cdgrupo_pai and a.cdusuario in(select cdusuario from usuario where cdempresa =:empresa)");
		sql.append(" and a.dtlimite >=:dtIni and a.dtregistro <=:dtFim ");
		sql.append(" and gtp.nogrupo not in(" + grupos + ")");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("empresa", empresa);
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		
		return query.getSingleResult().hashCode();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<GraficoAtividadeFaseDto> graficoAtividadeFase(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select f.nofase fase, count(a.cdatividade) total ");
		sql.append(" from fase_atividade f, atividade a ");
		sql.append(" where f.cdfase = a.cdfase_atividade ");
		sql.append("   and a.dtlimite >= :dtIni and a.dtlimite <= :dtFim ");
		sql.append("   and a.cdusuario in(select cdusuario from usuario where cdempresa =:empresa) ");
		sql.append(" group by f.nofase ");
		
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadeFaseDto.class));
		
		query.setParameter("empresa", empresa);
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		
		List<GraficoAtividadeFaseDto> result = (List<GraficoAtividadeFaseDto>) query.getResultList();
		
		return result;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<GraficoAtividadeFaseDto> graficoAtividadeFasePorProcesso(long processo, long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select f.nofase fase, count(a.cdatividade) total ");
		sql.append(" from atividade a, fase_atividade f ");
		sql.append(" where f.cdfase = a.cdfase_atividade and a.cdprocesso =:processo ");
		sql.append("   and (a.snprivado <> 'S' or a.cdatividade in(select cdatividade from atividade_usuario where cdusuario =:usuario) or ");
		sql.append("   1 = (select cdperfil from usuario where cdusuario =:usuario)) "); // Perfil de Administrador
		sql.append(" group by f.nofase ");
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadeFaseDto.class));
		
		query.setParameter("processo", processo);
		query.setParameter("usuario", usuario);
		
		List<GraficoAtividadeFaseDto> result = (List<GraficoAtividadeFaseDto>) query.getResultList();
		
		return result;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<GraficoAtividadesStatusDto> graficoAtividadesStatusPorProcesso(long processo, long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sa.nostatus status, count(a.cdatividade) total ");
		sql.append(" from atividade a, status_atividade sa ");
		sql.append(" where sa.cdstatus = a.cdstatus and a.cdprocesso =:processo ");
		sql.append("   and (a.snprivado <> 'S' or a.cdatividade in(select cdatividade from atividade_usuario where cdusuario =:usuario) or ");
		sql.append("   1 = (select cdperfil from usuario where cdusuario =:usuario)) "); // Perfil de Administrador
		sql.append(" group by sa.nostatus ");
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadesStatusDto.class));
		
		query.setParameter("processo", processo);
		query.setParameter("usuario", usuario);
		
		List<GraficoAtividadesStatusDto> result = (List<GraficoAtividadesStatusDto>) query.getResultList();
		
		return result;
	}

	@Override
	public List<GraficoFaseProcessoAtividadeDTO> graficoProcessoFaseAtividade(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select f.nofase fase, count(a.cdatividade) atividades ");
		sql.append(" from processo p, tipo_fase f, atividade a ");
		sql.append(" where f.cdfase = p.cdfase ");
		sql.append("   and p.cdprocesso = a.cdprocesso ");
		sql.append("   and p.cdempresa = :empresa ");
		sql.append("   and a.dtlimite >= :dtini and a.dtlimite <= :dtfim ");
		sql.append(" group by f.nofase ");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(GraficoFaseProcessoAtividadeDTO.class));

		query.setParameter("empresa", empresa);
		query.setParameter("dtini", dataInicial);
		query.setParameter("dtfim", dataFinal);

		List<GraficoFaseProcessoAtividadeDTO> result = (List<GraficoFaseProcessoAtividadeDTO>) query.getResultList();

		return result;
	}

	@Override
	public List<GraficoPontuacaoTotalDto> graficoPontuacaoTotal(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.pontos pontos, \n" +
				"          SUM(t.pontosTotal) pontosTotal\n" +
				"    FROM (\n" +
				"         SELECT (SELECT SUM(ta2.pontos)\n" +
				"          FROM atividade a2,\n" +
				"               titulo_atividade ta2,\n" +
				"               status_atividade sa2 \n" +
				"         WHERE a2.dstitulo = ta2.dstitulo AND ta2.cdempresa =:empresa\n" +
				"           and a2.dtlimite BETWEEN :dtini AND :dtfim\n" +
				"           and sa2.cdstatus = a2.cdstatus \n" +
				"           and sa2.nostatus = 'Concluído' \n" +
				"           and a2.cdusuario in(SELECT cdusuario FROM usuario WHERE cdempresa =:empresa)) pontos,\n" +
				"       SUM(ta.pontos) pontosTotal\n" +
				"  FROM titulo_atividade ta,\n" +
				"       atividade a\n" +
				" WHERE a.dtlimite BETWEEN :dtini AND :dtfim\n" +
				"   AND ta.dstitulo = a.dstitulo AND ta.cdempresa =:empresa\n" +
				"   AND a.cdusuario IN(SELECT cdusuario FROM usuario WHERE cdempresa =:empresa)\n " +
				" GROUP BY ta.pontos) t GROUP BY t.pontos ");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(GraficoPontuacaoTotalDto.class));

		query.setParameter("empresa", empresa);
		query.setParameter("dtini", dataInicial);
		query.setParameter("dtfim", dataFinal);

		List<GraficoPontuacaoTotalDto> result = (List<GraficoPontuacaoTotalDto>) query.getResultList();

		return result;
	}

	@Override
	public List<PontosUsuariosProjecaoDadosDto> pontosUsuariosProjecaoDados(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT\n" +
				"    u.cdusuario AS codigoUsuario,\n" +
				"    u.nousuario AS usuario,\n" +
				"    aconcluidas.quantidade AS atividadesConcluidas,\n" +
				"    aconcluidas.pontos AS pontos,\n" +
				"    atividades.qtde AS atividadesCriadas,\n" +
				"    atividades.proj AS projecao,\n" +
				"    ROUND((COALESCE(aconcluidas.pontos,0) / atividades.proj) * 100,0) percentual\n" +
				"FROM\n" +
				"    atividade a,\n" +
				"    titulo_atividade ta,\n" +
				"    usuario u,\n" +
				"    atividade_usuario au\n" +
				"left join \n" +
						"    (select au3.cdusuario,\n" +
						"            COUNT(a3.cdatividade) quantidade,\n" +
				        "            SUM(ta3.pontos) pontos \n" +
						"       from atividade a3,\n" +
						"            atividade_usuario au3, \n" +
				        "            titulo_atividade ta3, \n" +
				        "            status_atividade sa3 \n" +
						"      WHERE au3.cdatividade = a3.cdatividade\n" +
				        "        AND sa3.cdstatus = a3.cdstatus \n" +
						"        AND sa3.nostatus = 'Concluído' \n" +
				        "        AND a3.dtlimite BETWEEN :dtini AND :dtfim \n" +
						"        AND au3.tpusuario = 'R'\n" +
				        "        AND ta3.dstitulo = a3.dstitulo AND ta3.cdempresa = :empresa \n" +
						"        AND a3.cdusuario IN (SELECT cdusuario FROM usuario WHERE cdempresa = :empresa)\n" +
						"      GROUP BY au3.cdusuario \n" +
						"    ) aconcluidas on au.cdusuario = aconcluidas.cdusuario, \n" +
				"    (\n" +
				"        SELECT\n" +
				"            au2.cdusuario,\n" +
				"            COUNT(a2.cdatividade) AS qtde,\n" +
				"            SUM(ta2.pontos) proj\n" +
				"        FROM\n" +
				"            atividade a2,\n" +
				"            atividade_usuario au2,\n" +
				"            titulo_atividade ta2\n" +
				"        WHERE\n" +
				"            a2.cdusuario IN (SELECT cdusuario FROM usuario WHERE cdempresa =:empresa)\n" +
				"            AND a2.dtlimite BETWEEN :dtini AND :dtfim \n" +
				"            AND au2.tpusuario = 'R'\n" +
				"            AND au2.cdatividade = a2.cdatividade\n" +
				"            AND ta2.dstitulo = a2.dstitulo and ta2.cdempresa =:empresa \n" +
				"        GROUP BY\n" +
				"            au2.cdusuario\n" +
				"    ) atividades\n" +
				"WHERE\n" +
				"    a.dstitulo = ta.dstitulo\n" +
				"    AND ta.cdempresa =:empresa\n" +
				"    AND a.cdusuario IN (SELECT cdusuario FROM usuario WHERE cdempresa =:empresa)\n" +
				"    AND a.cdatividade = au.cdatividade\n" +
				"    AND u.cdusuario = au.cdusuario\n" +
				"    AND au.tpusuario = 'R'\n" +
				"    AND atividades.cdusuario = au.cdusuario\n" +
				"GROUP BY\n" +
				"    u.cdusuario,\n" +
				"    u.nousuario,\n" +
				"    atividades.qtde,\n" +
				"    atividades.proj, \n" +
				"    aconcluidas.quantidade, \n" +
				"    aconcluidas.pontos \n");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(PontosUsuariosProjecaoDadosDto.class));

		query.setParameter("empresa", empresa);
		query.setParameter("dtini", dataInicial);
		query.setParameter("dtfim", dataFinal);

		List<PontosUsuariosProjecaoDadosDto> result = (List<PontosUsuariosProjecaoDadosDto>) query.getResultList();
		return result;
	}

	@Override
	public List<Atividade> consultarAtividadesPorUsuario(long usuario, String dataInicial, String dataFinal, String tipoConsulta) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from atividade where cdatividade in(select cdatividade from atividade_usuario where cdusuario = :usuario and tpusuario = 'R') ");

		if(tipoConsulta.equals("C")) {
			sql.append(" and dtconcluido >= :dtIni and dtconcluido <= :dtFim ");
		} else {
			sql.append(" and dtregistro >= dtregistro >= :dtIni and dtregistro <= :dtFim ");
		}

		sql.append(" order by dtregistro ");

		Query query = manager.createNativeQuery(sql.toString(), Atividade.class);

		query.setParameter("usuario", usuario);
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);

		return query.getResultList();
	}

	@Override
	public List<AtividadesRankingDto> consultarRankingAtividades(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ta.cdtitulo codigo, ta.dstitulo atividade, f.nofase fase, count(a.cdatividade) atividades, ta.pontos pontuacao ");
		sql.append(" from atividade a, titulo_atividade ta left join tipo_fase f on(f.cdfase = ta.cdfase and f.cdempresa = :empresa) ");
		sql.append("where ta.dstitulo = a.dstitulo ");
		sql.append("  and ta.cdempresa = :empresa ");
		sql.append("  and a.cdusuario in(select cdusuario from usuario where cdempresa = :empresa) ");
		sql.append("  and a.dtlimite >= :dtIni and a.dtlimite <= :dtFim ");
		sql.append(" group by ta.cdtitulo, ta.dstitulo, f.nofase, ta.pontos ");
		sql.append("  order by count(a.cdatividade) desc limit 10");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(AtividadesRankingDto.class));

		query.setParameter("empresa", empresa);
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);

		List<AtividadesRankingDto> result = (List<AtividadesRankingDto>) query.getResultList();
		return result;
	}

	@Override
	public List<AtividadesRankingDto> consultarRankingAtividadesDemis(long empresa, String dataInicial, String dataFinal, String atividades) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ta.dstitulo atividade, f.nofase fase, count(a.cdatividade) atividades, ta.pontos pontuacao ");
		sql.append(" from atividade a, titulo_atividade ta left join tipo_fase f on (f.cdfase = ta.cdfase and f.cdempresa = :empresa) ");
		sql.append("where ta.dstitulo = a.dstitulo ");
		sql.append("  and ta.cdempresa = :empresa ");
		sql.append("  and a.cdusuario in(select cdusuario from usuario where cdempresa = :empresa) ");
		sql.append("  and a.dtlimite >= :dtIni and a.dtlimite <= :dtFim ");
		sql.append("  and ta.cdtitulo not in(" + atividades + ") ");
		sql.append(" group by ta.dstitulo, f.nofase, ta.pontos ");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(AtividadesRankingDto.class));

		query.setParameter("empresa", empresa);
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);

		List<AtividadesRankingDto> result = (List<AtividadesRankingDto>) query.getResultList();
		return result;
	}
}
