package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import com.br.ilawgestao.domains.dto.GraficoProcessoDto;
import com.br.ilawgestao.domains.dto.GraficoProcessoMesDto;

public class GraficoProcessoCustomImpl implements GraficoProcessoCustom {
	
	@Autowired
	private EntityManager manager;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<GraficoProcessoDto> graficoStatus(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sp.descricao nome, count(p.cdprocesso) quantidade from status_processual sp, processo p ");
		sql.append(" where sp.cdstatus = p.cdstatus_processual and p.cdstatus = 0 and p.cdempresa =:empresa ");
		sql.append(" group by sp.descricao ");
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(GraficoProcessoDto.class));
			
		query.setParameter("empresa", empresa);
		
		List<GraficoProcessoDto> result = (List<GraficoProcessoDto>) query.getResultList();
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<GraficoProcessoDto> graficoGrupoTrabalho(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select gt.nogrupo nome, count(p.cdprocesso) quantidade from grupo_trabalho gt, processo p ");
		sql.append(" where gt.cdgrupo = p.cdgrupo and p.cdstatus = 0 and p.cdempresa =:empresa ");
		sql.append(" group by gt.nogrupo order by  count(p.cdprocesso) desc limit 10 ");
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(GraficoProcessoDto.class));
			
		query.setParameter("empresa", empresa);
		
		List<GraficoProcessoDto> result = (List<GraficoProcessoDto>) query.getResultList();
		return result;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<GraficoProcessoDto> graficoAreaAtuacao(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select at.noarea nome, count(p.cdprocesso) quantidade from area_atuacao at, processo p ");
		sql.append(" where at.cdarea = p.cdarea_atuacao and p.cdstatus = 0 and p.cdempresa =:empresa ");
		sql.append(" group by at.noarea ");
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(GraficoProcessoDto.class));
			
		query.setParameter("empresa", empresa);
		
		List<GraficoProcessoDto> result = (List<GraficoProcessoDto>) query.getResultList();
		return result;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<GraficoProcessoDto> graficoTipoAcao(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ta.notipo nome, count(p.cdprocesso) quantidade from tipo_acao ta, processo p ");
		sql.append(" where ta.cdtipo = p.cdtipo_acao and p.cdstatus = 0 and p.cdempresa =:empresa ");
		sql.append(" group by ta.notipo ");
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(GraficoProcessoDto.class));
			
		query.setParameter("empresa", empresa);
		
		List<GraficoProcessoDto> result = (List<GraficoProcessoDto>) query.getResultList();
		return result;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<GraficoProcessoDto> graficoObjetos(long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select oa.noobjeto nome, count(p.cdprocesso) quantidade from objeto_acao oa, processo p, objeto_acao_processo oap ");
		sql.append(" where oa.cdobjeto = oap.cdobjeto and p.cdprocesso = oap.cdprocesso and p.cdstatus = 0 and p.cdempresa =:empresa ");
		sql.append(" group by oa.noobjeto ");
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(GraficoProcessoDto.class));
			
		query.setParameter("empresa", empresa);
		
		List<GraficoProcessoDto> result = (List<GraficoProcessoDto>) query.getResultList();
		return result;
	}

	@Override
	public List<GraficoProcessoDto> graficoPagamentos(long empresa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GraficoProcessoDto> graficoCustas(long empresa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int graficoGrupoTrabalhoOutros(String grupos, long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(p.cdprocesso) from grupo_trabalho gt, processo p ");
		sql.append(" where gt.cdgrupo = p.cdgrupo and p.cdstatus = 0 and p.cdempresa =:empresa ");
		sql.append(" and gt.nogrupo not in(" + grupos + ")");
		
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("empresa", empresa);
		
		return query.getSingleResult().hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GraficoProcessoMesDto> graficoProcessoMes(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select m.cdmes codigo, m.mes mes, COALESCE(count(p.cdprocesso),0) total ");
		sql.append(" from processo p right join mes m on m.cdmes = month(p.dtcadastro) and m.ano = year(p.dtcadastro) "); 
		sql.append(" where m.cdmes BETWEEN month(:dtIni) and month(:dtFim) and p.cdempresa =:empresa or p.cdempresa is null ");
		sql.append(" group by m.cdmes, m.mes ");
		sql.append(" order by m.cdmes ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(GraficoProcessoMesDto.class));
		
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		query.setParameter("empresa", empresa);
		
		List<GraficoProcessoMesDto> result = (List<GraficoProcessoMesDto>) query.getResultList();
		
		return result;
	}
}
