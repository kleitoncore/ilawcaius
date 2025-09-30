package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.dto.ProcessoArquivadoDto;
import com.br.ilawgestao.domains.models.ProcessoArquivado;

public class ProcessoArquivadoCustomImpl implements ProcessoArquivadoCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<ProcessoArquivado> processosArquivadosPorDatas(String dataInicial, String dataFinal, long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p from ProcessoArquivado p where p.empresa.codigo =:empresa and p.dataArquivado >=:dtIni and p.dataArquivado <=:dtFim ");
		
		TypedQuery<ProcessoArquivado> query = manager.createQuery(sql.toString(), ProcessoArquivado.class);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessoArquivadoDto> processosArquivadosMes(long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select m.cdmes codigo, m.mes mes, COALESCE(count(p.cdprocesso),0) total ");
		sql.append(" from processo_arquivado p right join mes m on m.cdmes = month(p.dtarquivamento) and m.ano = year(p.dtarquivamento) "); 
		sql.append(" where m.cdmes BETWEEN month(:dtIni) and month(:dtFim) and (p.cdusuario in(select cdusuario from usuario where cdempresa =:empresa) or p.cdusuario is null) ");
		sql.append(" group by m.cdmes, m.mes ");
		sql.append(" order by m.cdmes ");
		
		@SuppressWarnings("deprecation")
		Query query = manager.createNativeQuery(sql.toString())
			.unwrap(org.hibernate.query.Query.class)
			.setResultTransformer(new AliasToBeanResultTransformer(ProcessoArquivadoDto.class));
		
		query.setParameter("dtIni", dataInicial);
		query.setParameter("dtFim", dataFinal);
		query.setParameter("empresa", empresa);
		
		List<ProcessoArquivadoDto> result = (List<ProcessoArquivadoDto>) query.getResultList();
		
		return result;
	}
}
