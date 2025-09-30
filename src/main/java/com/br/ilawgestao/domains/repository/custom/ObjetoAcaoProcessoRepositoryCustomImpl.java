package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import com.br.ilawgestao.domains.models.ObjetoAcao;

public class ObjetoAcaoProcessoRepositoryCustomImpl implements ObjetoAcaoProcessoRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<ObjetoAcao> listarObjetosForaDoProcesso(long empresa, long processo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select o from ObjetoAcao o where o.codigo not in(select objeto.codigo from ObjetoAcaoProcesso where processo.codigo =:codigoProcesso) "
				+ "and empresa.codigo =:codigoEmpresa and codigo = objetoPai order by nome");
		TypedQuery<ObjetoAcao> query = manager.createQuery(sql.toString(), ObjetoAcao.class);
		query.setParameter("codigoEmpresa", empresa);
		query.setParameter("codigoProcesso", processo);
		return query.getResultList();
	}
	
	@Override
	public List<ObjetoAcao> listarSubObjetosForaDoProcesso(long processo, long objetoPai) {
		StringBuilder sql = new StringBuilder();
		sql.append("select o from ObjetoAcao o where o.codigo not in(select objeto.codigo from ObjetoAcaoProcesso where processo.codigo =:codigoProcesso) "
				+ "and objetoPai =:objetoPai and codigo <> objetoPai order by nome");
		TypedQuery<ObjetoAcao> query = manager.createQuery(sql.toString(), ObjetoAcao.class);
		query.setParameter("codigoProcesso", processo);
		query.setParameter("objetoPai", objetoPai);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjetoAcao> listaObjetosPorCodigos(String codigos) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from objeto_acao where cdobjeto in(" + codigos + ")");
		Query query = manager.createNativeQuery(sql.toString(), ObjetoAcao.class);
		return query.getResultList();
	}
}
