package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.br.ilawgestao.domains.dto.PontuacaoProjecaoDatasDto;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.dto.PontuacaoUsuarioDto;

public class PontuacaoUsuarioRepositoryCustomImpl implements PontuacaoUsuarioRepositoryCustom {

	
	@Autowired
	private EntityManager manager;
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<PontuacaoUsuarioDto> pontuacaoUsuario(long ano, long mes, long usuario, long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select day(a.dtconcluido) dia, sum(ta.pontos) pontos ");
		sql.append(" from atividade a, titulo_atividade ta, atividade_usuario au ");
		sql.append(" where a.cdatividade = au.cdatividade ");
		sql.append("   and ta.dstitulo = a.dstitulo and ta.cdempresa =:empresa ");
		sql.append("   and au.cdusuario =:usuario and au.tpusuario = 'R'");
		sql.append("   and month(dtconcluido) =:mes ");
		sql.append("   and year(a.dtconcluido) =:ano ");
		sql.append(" group by day(a.dtconcluido)");
		
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(PontuacaoUsuarioDto.class));
		
		query.setParameter("ano", ano);
		query.setParameter("mes", mes);
		query.setParameter("usuario", usuario);
		query.setParameter("empresa", empresa);
		
		List<PontuacaoUsuarioDto> result = (List<PontuacaoUsuarioDto>) query.getResultList();
		
		return result;

	}

	@Override
	public List<PontuacaoProjecaoDatasDto> pontuacaoProjecaoDatas(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select day(a.dtconcluido) dia, sum(ta.pontos) pontos ");
		sql.append(" from atividade a, titulo_atividade ta ");
		sql.append(" where ta.dstitulo = a.dstitulo ");
		sql.append("   and a.cdusuario in(select cdusuario from usuario where cdempresa = :empresa) ");
		sql.append("   and a.cdstatus = 5 ");
		sql.append("   and ta.cdempresa = :empresa ");
		sql.append("   and a.dtconcluido >= :dtini and dtconcluido <= :dtfim ");
		sql.append(" group by day(a.dtconcluido) ");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(PontuacaoProjecaoDatasDto.class));

		query.setParameter("empresa", empresa);
		query.setParameter("dtini", dataInicial);
		query.setParameter("dtfim", dataFinal);

		List<PontuacaoProjecaoDatasDto> result = (List<PontuacaoProjecaoDatasDto>) query.getResultList();

		return result;
	}

}
