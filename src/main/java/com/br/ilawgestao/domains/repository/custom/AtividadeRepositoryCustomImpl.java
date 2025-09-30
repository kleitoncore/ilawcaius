package com.br.ilawgestao.domains.repository.custom;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.br.ilawgestao.domains.dto.*;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.Agenda;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.AtividadeShort;
import com.br.ilawgestao.domains.repository.filtros.FiltroAgenda;
import com.br.ilawgestao.domains.repository.filtros.FiltroKanban;
import com.br.ilawgestao.domains.repository.filtros.FiltroMiniAgenda;


public class AtividadeRepositoryCustomImpl implements AtividadeRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaAtividadesAtrasadasResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario =:usuario and cdusuario <> cdusuario_interessado");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) < curdate() and status not in('Concluído','Cancelada')");
		} else { 
			sql.append(" select a.* from atividade_short a where a.cdusuario =:usuario and ");
            sql.append(" a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
            sql.append(" and date(a.dtlimite) < curdate() and status not in('Concluído','Cancelada')");
		}

		if(importante.equals("S")) {
			sql.append(" and snimportante = 'S'");
		}

		if(urgente.equals("S")) {
			sql.append(" and snurgente = 'S'");
		}

		if(fatal.equals("S")) {
			sql.append(" and dtfatal is not null ");
		}

		sql.append(" order by dtlimite ");
		
		if(pesquisa != "") {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			query.setParameter("pesquisa", "%" + pesquisa + "%");
			return query.getResultList();
		} else {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaAtividadesHojeResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario =:usuario ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) = date(curdate()) and status not in('Concluído','Cancelada') ");
		} else { 
			sql.append("select * from atividade_short a where a.cdusuario =:usuario ");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) = date(curdate()) and status not in('Concluído','Cancelada') ");
			//sql.append("select a from AtividadeShort a where a.codigoResponsavel =:usuario and a.tipoAtividade = 'T' and date(a.dtLimite) = date(curdate()) and a.status not in(5,3) order by a.dtLimite");		}
		}

		if(importante.equals("S")) {
			sql.append(" and snimportante = 'S'");
		}

		if(urgente.equals("S")) {
			sql.append(" and snurgente = 'S'");
		}

		if(fatal.equals("S")) {
			sql.append(" and dtfatal is not null ");
		}

		sql.append(" order by dtlimite ");
		
		if(pesquisa != "") {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			query.setParameter("pesquisa", "%" + pesquisa + "%");
			return query.getResultList();
		} else {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaAtividadesSeteResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario =:usuario ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) >= curdate() + interval 1 day ");
			sql.append(" and date(dtlimite) < curdate() + interval 7 day ");
			sql.append(" and status not in('Concluído','Cancelada') ");
		} else { 
			sql.append("select a.* from atividade_short a where a.cdusuario =:usuario ");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) >= curdate() + interval 1 day and date(a.dtlimite) < curdate() + interval 7 day "); 
			sql.append(" and status not in('Concluído','Cancelada')");
		}

		if(importante.equals("S")) {
			sql.append(" and snimportante = 'S'");
		}

		if(urgente.equals("S")) {
			sql.append(" and snurgente = 'S'");
		}

		if(fatal.equals("S")) {
			sql.append(" and dtfatal is not null ");
		}

		sql.append(" order by dtlimite ");
		
		Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
		
		query.setParameter("usuario", usuario);
		if(pesquisa != "") {
			query.setParameter("pesquisa", "%" + pesquisa + "%");
		}
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaAtividadesTrintaResponsavel(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario =:usuario ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) >= curdate() + interval 7 day ");
			sql.append(" and date(dtlimite) < curdate() + interval 37 day ");
			sql.append(" and status not in('Concluído','Cancelada')");
		} else { 
			sql.append("select a.* from atividade_short a where a.cdusuario =:usuario ");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) >= curdate() + interval 7 day and date(a.dtlimite) < curdate() + interval 37 day "); 
			sql.append(" and status not in('Concluído','Cancelada')");
		}

		if(importante.equals("S")) {
			sql.append(" and snimportante = 'S'");
		}

		if(urgente.equals("S")) {
			sql.append(" and snurgente = 'S'");
		}

		if(fatal.equals("S")) {
			sql.append(" and dtfatal is not null ");
		}

		sql.append(" order by dtlimite ");
		
		Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
		
		query.setParameter("usuario", usuario);
		if(pesquisa != "") {
			query.setParameter("pesquisa", "%" + pesquisa + "%");
		}
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaAtividadesAtrasadasInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario_interessado =:usuario ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) < curdate() and status not in('Concluído','Cancelada') ");
		} else { 
			sql.append("select * from atividade_short a where a.cdusuario_interessado =:usuario ");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) < curdate() and status not in('Concluído','Cancelada') ");
		}

		if(importante.equals("S")) {
			sql.append(" and snimportante = 'S'");
		}

		if(urgente.equals("S")) {
			sql.append(" and snurgente = 'S'");
		}

		if(fatal.equals("S")) {
			sql.append(" and dtfatal is not null ");
		}

		sql.append(" order by dtlimite ");
		
		if(pesquisa != "") {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			query.setParameter("pesquisa", "%" + pesquisa + "%");
			return query.getResultList();
		} else {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaAtividadesHojeInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario_interessado =:usuario ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) = date(curdate()) and status not in('Concluído','Cancelada') ");
		} else { 
			sql.append("select * from atividade_short a where a.cdusuario_interessado =:usuario ");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) = date(curdate()) and status not in('Concluído','Cancelada') ");
		}

		if(importante.equals("S")) {
			sql.append(" and snimportante = 'S'");
		}

		if(urgente.equals("S")) {
			sql.append(" and snurgente = 'S'");
		}

		if(fatal.equals("S")) {
			sql.append(" and dtfatal is not null ");
		}

		sql.append(" order by dtlimite ");
		
		if(pesquisa != "") {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			query.setParameter("pesquisa", "%" + pesquisa + "%");
			return query.getResultList();
		} else {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaAtividadesSeteInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario_interessado =:usuario ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) >= curdate() + interval 1 day ");
			sql.append(" and date(dtlimite) < curdate() + interval 7 day ");
			sql.append(" and status not in('Concluído','Cancelada') ");
		} else { 
			sql.append("select a.* from atividade_short a where a.cdusuario_interessado =:usuario ");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) >= curdate() + interval 1 day and date(a.dtlimite) < curdate() + interval 7 day "); 
			sql.append(" and status not in('Concluído','Cancelada') ");
		}

		if(importante.equals("S")) {
			sql.append(" and snimportante = 'S'");
		}

		if(urgente.equals("S")) {
			sql.append(" and snurgente = 'S'");
		}

		if(fatal.equals("S")) {
			sql.append(" and dtfatal is not null ");
		}

		sql.append(" order by dtlimite ");
		
		Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
		
		query.setParameter("usuario", usuario);
		if(pesquisa != "") {
			query.setParameter("pesquisa", "%" + pesquisa + "%");
		}
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaAtividadesTrintaInteressado(long usuario, String pesquisa, String importante, String urgente, String fatal) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario_interessado =:usuario ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) >= curdate() + interval 7 day ");
			sql.append(" and date(dtlimite) < curdate() + interval 37 day ");
			sql.append(" and status not in('Concluído','Cancelada') ");
		} else { 
			sql.append("select a.* from atividade_short a where a.cdusuario_interessado =:usuario ");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) >= curdate() + interval 7 day and date(a.dtlimite) < curdate() + interval 37 day "); 
			sql.append(" and status not in('Concluído','Cancelada') ");
		}

		if(importante.equals("S")) {
			sql.append(" and snimportante = 'S'");
		}

		if(urgente.equals("S")) {
			sql.append(" and snurgente = 'S'");
		}

		if(fatal.equals("S")) {
			sql.append(" and dtfatal is not null ");
		}

		sql.append(" order by dtlimite ");
		
		Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
		
		query.setParameter("usuario", usuario);
		if(pesquisa != "") {
			query.setParameter("pesquisa", "%" + pesquisa + "%");
		}
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaCompromissosAtrasadosResponsavel(long usuario, String pesquisa) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario =:usuario and tpatividade = 'A' ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta  like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) < curdate() and status not in('Concluído','Cancelada') order by dtlimite ");
		} else { 
			sql.append("select * from atividade_short a where a.cdusuario =:usuario and a.tpatividade = 'A' ");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) < curdate() and status not in('Concluído','Cancelada') order by a.dtlimite ");
		}
		
		if(pesquisa != "") {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			query.setParameter("pesquisa", "%" + pesquisa + "%");
			return query.getResultList();
		} else {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaCompromissosHojeResponsavel(long usuario, String pesquisa) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario =:usuario and tpatividade = 'A' ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) = date(curdate()) and status not in('Concluído','Cancelada') order by dtlimite ");
		} else { 
			sql.append("select * from atividade_short a where a.cdusuario =:usuario and a.tpatividade = 'A' ");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) = date(curdate()) and status not in('Concluído','Cancelada') order by a.dtlimite ");
		}
		
		if(pesquisa != "") {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			query.setParameter("pesquisa", "%" + pesquisa + "%");
			return query.getResultList();
		} else {
			Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaCompromissosSeteResponsavel(long usuario, String pesquisa) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario =:usuario and tpatividade = 'A' ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) >= curdate() + interval 1 day ");
			sql.append(" and date(dtlimite) < curdate() + interval 7 day ");
			sql.append(" and status not in('Concluído','Cancelada') order by dtlimite ");
		} else { 
			sql.append("select a.* from atividade_short a where a.cdusuario =:usuario and a.tpatividade = 'A'");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) >= curdate() + interval 1 day and date(a.dtlimite) < curdate() + interval 7 day "); 
			sql.append(" and status not in('Concluído','Cancelada') order by a.dtlimite");
		}
		
		Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
		
		query.setParameter("usuario", usuario);
		if(pesquisa != "") {
			query.setParameter("pesquisa", "%" + pesquisa + "%");
		}
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtividadeShort> listaCompromissosTrintaResponsavel(long usuario, String pesquisa) {
		StringBuilder sql = new StringBuilder();
		if(pesquisa != "") {
			sql.append("select * from atividade_short where cdusuario =:usuario and tpatividade = 'A' ");
			sql.append(" and (dstitulo like :pesquisa or nrcnj like :pesquisa or nrpasta like :pesquisa or nrprocesso like :pesquisa or partes like :pesquisa or grupo like :pesquisa) ");
			sql.append(" and date(dtlimite) >= curdate() + interval 7 day ");
			sql.append(" and date(dtlimite) < curdate() + interval 37 day ");
			sql.append(" and status not in('Concluído','Cancelada') order by dtlimite ");
		} else { 
			sql.append("select a.* from atividade_short a where a.cdusuario =:usuario and a.tpatividade = 'A' ");
			sql.append(" and a.cdcontrole = (select max(a2.cdcontrole) from atividade_short a2 where a2.cdatividade = a.cdatividade and ");
            sql.append(" a2.cdusuario = a.cdusuario) ");
			sql.append(" and date(a.dtlimite) >= curdate() + interval 7 day and date(a.dtlimite) < curdate() + interval 37 day "); 
			sql.append(" and status not in('Concluído','Cancelada') order by a.dtlimite");
		}
		
		Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
		
		query.setParameter("usuario", usuario);
		if(pesquisa != "") {
			query.setParameter("pesquisa", "%" + pesquisa + "%");
		}
		
		return query.getResultList();
	}

	@Override
	public int atividadesTotalUsuario(long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*)  from atividade_short where cdusuario =:usuario ");
		sql.append(" and cdstatus not in(5,3) ");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("usuario", usuario);
		
		return query.getSingleResult().hashCode();
	}

	@Override
	public int atividadeUltimosTrintaDias(long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from atividade_short where cdusuario =:usuario ");
		sql.append(" and date(dtregistro) BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE() ");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("usuario", usuario);
		
		return query.getSingleResult().hashCode();
	}

	@Override
	public int atividadesConcluidasUltimasTrintaDias(long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from atividade_short where cdusuario =:usuario ");
		sql.append(" and date(dtconcluido) BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE() and cdstatus = 5 ");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("usuario", usuario);
		
		return query.getSingleResult().hashCode();
	}

	@Override
	public int atividadesConcluidasComAtrasoUltimosTrintaDias(long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from atividade_short where cdusuario =:usuario ");
		sql.append(" and date(dtconcluido) BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE() and date(dtconcluido) > date(dtlimite) ");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("usuario", usuario);
		
		return query.getSingleResult().hashCode();
	}

	@Override
	public int atividadesConsluidasNoPrazoUltimosTrintaDias(long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from atividade_short where cdusuario =:usuario ");
		sql.append(" and date(dtconcluido) BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE() and date(dtconcluido) <= date(dtlimite) ");
		
		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("usuario", usuario);
		
		return query.getSingleResult().hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agenda> agenda(FiltroAgenda filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from agenda where cdusuario in(" + filtro.getUsuarios() + ")");
		sql.append(" and cdgrupo in(" + filtro.getGrupoTrabalho() + ")");
		sql.append(" and cdstatus in(" + filtro.getStatus() + ")");
		sql.append(" and tpatividade in(" + filtro.getTipos() + ")");
		sql.append(" and cdgrupo in(select cdgrupo from usuario_grupo_trabalho where cdusuario =:usuario)");
		sql.append(" and date(dtcompromisso) >= '2024-10-01'");
		
		Query query = manager.createNativeQuery(sql.toString(), Agenda.class);
		
		query.setParameter("usuario", filtro.getUsuario());
		
		List<Agenda> lista = query.getResultList();
		return lista;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Agenda> agendaHome(FiltroAgenda filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from agenda where cdusuario in(" + filtro.getUsuarios() + ")");
		sql.append(" and cdgrupo in(" + filtro.getGrupoTrabalho() + ")");
		sql.append(" and cdstatus in(" + filtro.getStatus() + ")");
		sql.append(" and tpatividade in(" + filtro.getTipos() + ")");
		sql.append(" and cdgrupo in(select cdgrupo from usuario_grupo_trabalho where cdusuario =:usuario)");
		sql.append(" and cdstatus not in(3,5)");
		sql.append(" and date(dtcompromisso) >= '2022-12-01'");
		
		Query query = manager.createNativeQuery(sql.toString(), Agenda.class);
		
		query.setParameter("usuario", filtro.getUsuario());
		
		List<Agenda> lista = query.getResultList();
		return lista;
		
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<MiniAgendaDto> agendaPorMesUsuario(FiltroAgenda filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select CAST(YEAR(dtcompromisso) AS CHAR) AS ano, ");
		sql.append("	   CAST(MONTH(dtcompromisso) AS CHAR) AS mes, ");
		sql.append("       CAST(DAY(dtcompromisso) AS CHAR) AS dia, ");
		sql.append("       case dayname(dtcompromisso) ");
		sql.append("          when 'Monday' then 'Segunda-Feira' ");
		sql.append("          when 'Tuesday' then 'Terça-Feira' ");
		sql.append("          when 'Wednesday' then 'Quarta-Feira' ");
		sql.append("          when 'Thursday' then 'Quinta-Feira' ");
		sql.append("          when 'Friday' then 'Sexta-Feira' ");
		sql.append("          when 'Saturday' then 'Sábado' ");
		sql.append("          when 'Sunday' then 'Domingo' ");
		sql.append("       end diaSemana, ");
		sql.append("       date(dtcompromisso) data, ");
		sql.append("       count(cdagenda) quantidadeTarefas ");
		sql.append(" from agenda ");
		sql.append(" where cdusuario =:usuario ");
		sql.append("  and month(dtcompromisso) =:mesParam and year(dtcompromisso) =:anoParam ");
		sql.append(" group by CAST(YEAR(dtcompromisso) AS CHAR), ");
		sql.append("          CAST(MONTH(dtcompromisso) AS CHAR), ");
		sql.append("          CAST(DAY(dtcompromisso) AS CHAR), ");
		sql.append("          case dayname(dtcompromisso) ");
		sql.append("            when 'Monday' then 'Segunda-Feira' ");
		sql.append("            when 'Tuesday' then 'Terça-Feira' ");
		sql.append("            when 'Wednesday' then 'Quarta-Feira' ");
		sql.append("            when 'Thursday' then 'Quinta-Feira' ");
		sql.append("            when 'Friday' then 'Sexta-Feira' ");
		sql.append("            when 'Saturday' then 'Sábado' ");
		sql.append("            when 'Sunday' then 'Domingo' ");
		sql.append("          end, ");
		sql.append("          date(dtcompromisso) ");
		sql.append(" order by date(dtcompromisso) ");
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(MiniAgendaDto.class));
			
		query.setParameter("usuario", filtro.getUsuario());
		query.setParameter("mesParam", filtro.getMes());
		query.setParameter("anoParam", filtro.getAno());
		
		List<MiniAgendaDto> result = (List<MiniAgendaDto>) query.getResultList();
		return result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AtividadeShort> painelKanban(FiltroKanban filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.* from atividade_short a where a.status = '" + filtro.getStatus() + "'");
		sql.append(" and a.cdusuario in(select cdusuario from usuario where cdempresa = " + filtro.getEmpresa() + ")");
		sql.append(" and a.cdusuario in(" + filtro.getUsuario() + ")");
		sql.append(" and a.cdgrupo in(" + filtro.getGrupo() + ")");
		
		if(filtro.getDataLimiteInicial() != null && filtro.getDataLimiteFinal() != null) {
			sql.append(" and date(a.dtlimite) >= '" + filtro.getDataLimiteInicial() + "'");
			sql.append(" and date(a.dtlimite) <= '" + filtro.getDataLimiteFinal() + "'");
		}
		
		sql.append(" order by a.dtlimite desc");
		sql.append(" limit " + filtro.getLimite());
		
		Query query = manager.createNativeQuery(sql.toString(), AtividadeShort.class);
		return query.getResultList();
	}

	@Override
	public List<AtividadesPontosDto> consultarAtividadesPorUsuarioResponsavel(long usuario, long empresa, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.dstitulo atividade, ta.pontos pontos from atividade a, titulo_atividade ta ");
		sql.append("where ta.dstitulo = a.dstitulo and ta.cdempresa = " + empresa);
		sql.append("  and a.cdatividade in(select cdatividade from atividade_usuario where cdusuario = " + usuario + " and tpusuario = 'R')");
		sql.append("  and a.dtlimite >= '" + dataInicial + "'");
		sql.append("  and a.dtlimite <= '" + dataFinal + "'");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(AtividadesPontosDto.class));

		List<AtividadesPontosDto> result = (List<AtividadesPontosDto>) query.getResultList();
		return result;
	}

	@Override
	public AtividadeCustomDto consultarAtividadeCustom(long codigo,long empresa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.cdatividade codigo,\n" +
				"       a.dstitulo titulo,\n" +
				"       a.descricao descricao,\n" +
				"       DATE_FORMAT(a.dtlimite, '%Y-%m-%d') dataLimite,\n" +
				"       DATE_FORMAT(a.dtlimite, '%H:%i') hora,\n" +
				"       DATE_FORMAT(a.dtlida, '%Y-%m-%d') dataLida,\n" +
				"       (select DATE_FORMAT(dthistorico, '%d/%m/%Y') from historico_atividade h where h.cdatividade = a.cdatividade and dshistorico like 'Atividade incluída%') dataCriacao,\n" +
				"       DATE_FORMAT(a.dtfatal, '%Y-%m-%d') prazoFatal,\n" +
				"       a.cdstatus statusCodigo,\n" +
				"       a.tpatividade tipo,\n" +
				"       COALESCE(pi.cdpessoa,null) codigoPessoa,\n" +
				"       pi.nopessoa nomePessoa,\n" +
				"       gt.cdgrupo codigoGrupo, \n" +
				"       gt.nogrupo grupo,\n" +
				"       sgt.cdgrupo codigoSubGrupo,\n" +
				"       sgt.nogrupo subGrupo,\n" +
				"       a.snimportante importante,\n" +
				"       a.snurgente urgente,\n" +
				"       a.snprivado privado,\n" +
				"       u.nousuario usuarioCriou,\n" +
				"       case \n" +
				"         when a.tpatividade = 'L' then 50 \n" +
				"         else \n" +
				"       	(select distinct COALESCE(pontos, 0) from titulo_atividade ta where dstitulo = a.dstitulo and cdempresa = :codigoEmpresa) \n" +
				"       end pontos,\n" +
				"       COALESCE(p.cdprocesso,null) codigoProcesso,\n" +
				"       COALESCE(p.nrcnj, p.nrprocesso, p.pasta ) AS processo,\n" +
				"       tf.nofase fase,\n " +
				"       COALESCE(a.cdlancamento,null) lancamentoFinanceiro,\n" +
				"       GROUP_CONCAT(pe.nopessoa SEPARATOR ', ') partes \n" +
				"  from atividade a left join processo p on (p.cdprocesso = a.cdprocesso)\n" +
				"                   left join partes pp on (pp.cdprocesso = p.cdprocesso)\n" +
				"                   left join pessoa pe on(pe.cdpessoa = pp.cdpessoa)\n" +
				"                   left join tipo_fase tf on (tf.cdfase = p.cdfase)\n" +
				"                   inner join grupo_trabalho sgt on(sgt.cdgrupo = a.cdsubgrupo)\n" +
				"                   inner join grupo_trabalho gt on (gt.cdgrupo = sgt.cdgrupo_pai)\n" +
				"                   inner join usuario u on (u.cdusuario = a.cdusuario)\n" +
				"                   left join pessoa pi on (pi.cdpessoa = a.cdpessoa)\n" +
				"  where a.cdatividade = :codigoAtividade \n" +
				" group by a.cdatividade,\n" +
				"\t      a.dstitulo,\n" +
				"\t      a.descricao,\n" +
				"\t      a.dtlimite,\n" +
				"\t      DATE_FORMAT(a.dtlimite, '%H:%i'),\n" +
				"\t      a.dtlida,\n" +
				"\t      a.dtfatal,\n" +
				"\t      case \n" +
				"\t        when a.tpatividade = 'L' then 50 \n" +
				"\t        else \n" +
				"\t      	 (select DATE_FORMAT(dthistorico, '%d/%m/%Y') from historico_atividade h where h.cdatividade = a.cdatividade and dshistorico like 'Atividade incluída%')\n" +
				"\t      end, \n" +
				"\t      a.cdstatus,\n" +
				"\t      a.tpatividade,\n" +
				"\t      COALESCE(pi.cdpessoa,null),\n" +
				"\t      pi.nopessoa,\n" +
				"\t      gt.cdgrupo,\n" +
				"\t      gt.nogrupo,\n" +
				"\t      sgt.cdgrupo,\n" +
				"\t      sgt.nogrupo,\n" +
				"\t      COALESCE(p.nrcnj, p.nrprocesso, p.pasta),\n" +
				"\t      COALESCE(p.cdprocesso,null),\n" +
				"\t      tf.nofase,\n" +
				"\t      COALESCE(a.cdlancamento,null),\n" +
				"\t      a.snimportante,\n" +
				"\t      a.snurgente,\n" +
				"\t      a.snprivado,\n" +
				"\t      u.nousuario,\n" +
		        "\t      (select distinct pontos from titulo_atividade ta where dstitulo = a.dstitulo and cdempresa = :codigoEmpresa) ");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(AtividadeCustomDto.class));

		query.setParameter("codigoAtividade", codigo);
		query.setParameter("codigoEmpresa", empresa);

		List<AtividadeCustomDto> result = (List<AtividadeCustomDto>) query.getResultList();
		return result.get(0);
	}

	@Override
	public List<DiasCalendario> dias(long mes, long ano) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct day(dtlimite) dia\n" +
					"  from atividade\n" +
					" where MONTH(dtlimite) = " + mes + "\n" +
					"  and YEAR(dtlimite) = " + ano + "\n" +
					"  order by day(dtlimite)");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(DiasCalendario.class));

		List<DiasCalendario> result = (List<DiasCalendario>) query.getResultList();
		return result;
	}

	@Override
	public List<AtividadesGeralHomeDto> consultarAtividadesGeralHome(FiltroMiniAgenda filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT \n" +
				"    a.cdatividade AS codigo,\n" +
				"    a.dstitulo AS atividade,\n" +
				"    sa.nostatus AS status,\n" +
				"    a.snimportante importante,\n" +
				"    a.snurgente urgente,\n" +
				"    sa.cor AS corStatus,\n" +
				"    tf.nofase AS fase,\n" +
				"    tf.cor AS corfase,\n" +
				"    gts.nogrupo AS subGrupo,\n" +
				"    gtp.nogrupo AS grupo,\n" +
				"	 DATE_FORMAT(a.dtlimite, '%d/%m/%Y') AS dataLimite,\n" +
				"    DATE_FORMAT(a.dtfatal, '%d/%m/%Y') AS prazoFatal,\n" +
				"    DATE_FORMAT(a.dtconcluido, '%d/%m/%Y') AS dataConcluido,\n" +
				"    IFNULL(p.cdprocesso,0) codigoProcesso,\n" +
				"    CASE \n" +
				"        WHEN p.nrcnj IS NOT NULL THEN CONVERT(p.nrcnj USING utf8) COLLATE utf8_general_ci\n" +
				"        WHEN p.nrcnj IS NULL AND p.nrprocesso IS NULL THEN CONVERT(p.pasta USING utf8) COLLATE utf8_general_ci\n" +
				"        ELSE CONVERT(p.nrprocesso USING utf8) COLLATE utf8_general_ci\n" +
				"    END AS processo,\n" +
				"    GROUP_CONCAT(pe.nopessoa) AS partes\n" +
				"FROM \n" +
				"    atividade a \n" +
				"LEFT JOIN \n" +
				"    historico_fase_processual hfp ON a.cdatividade = hfp.cdatividade\n" +
				"LEFT JOIN \n" +
				"    processo p ON p.cdprocesso = hfp.cdprocesso\n" +
				"JOIN \n" +
				"    status_atividade sa ON sa.cdstatus = a.cdstatus\n" +
				"LEFT JOIN \n" +
				"    partes pp ON p.cdprocesso = pp.cdprocesso\n" +
				"LEFT JOIN \n" +
				"    pessoa pe ON pe.cdpessoa = pp.cdpessoa\n" +
				"LEFT JOIN \n" +
				"    tipo_fase tf ON tf.cdfase = hfp.cdfase\n" +
				"JOIN \n" +
				"    grupo_trabalho gts ON gts.cdgrupo = a.cdsubgrupo\n" +
				"JOIN \n" +
				"    grupo_trabalho gtp ON gtp.cdgrupo = gts.cdgrupo_pai\n" +
				"WHERE \n" +
				"    a.cdatividade in(select cdatividade from atividade_usuario where cdusuario = " + filtro.getUsuario() + ")\n");

		if(!filtro.getData().equals("0")) {
			sql.append(" and a.dtlimite = '" + filtro.getData() + "'\n");
		}

		//Filtra por status da atividade
		if(!filtro.getStatus().equals("0")) {
			sql.append(" and sa.cdstatus in(" + filtro.getStatus() + ")\n");
		}

		//Filtra por fase do processo
		if(!filtro.getFase().equals("0")) {
			sql.append(" and tf.nofase = '" + filtro.getFase() + "'\n");
		}

		//Filtra por intervalos de data limite
		if(!filtro.getDataLimiteInicial().equals("0") && !filtro.getDataLimiteFinal().equals("0")) {
			sql.append(" and a.dtlimite >= '" + filtro.getDataLimiteInicial() + "'\n");
			sql.append(" and a.dtlimite <= '" + filtro.getDataLimiteFinal() + "'\n");
		}

		//Filtra por prazo fatal
		if(!filtro.getPrazoFatalInicial().equals("0") && !filtro.getPrazoFatalFinal().equals("0")) {
			sql.append(" and a.dtfatal >= '" + filtro.getPrazoFatalInicial() + "'\n");
			sql.append(" and a.dtfatal <= '" + filtro.getPrazoFatalFinal() + "'\n");
		}

		//Apenas filtra com as atividade que tenha prazo fatal
		if(filtro.isPrazoFatal()) {
			sql.append(" and a.dtfatal is not null\n");
		}

		//Filtra por partes
		if(!filtro.getPartes().equals("0")) {
			sql.append(" and pe.nopessoa like '" + "%" + filtro.getPartes() + "%" + "'\n");
		}

		//Filtra pelo número do processo
		if(!filtro.getProcesso().equals("0")) {
			sql.append(" and p.nrcnj = '" + filtro.getProcesso() + "'" + " or p.nrprocesso = '" + filtro.getProcesso() + "'");
		}

		//Filtra por data de conclusão
		if(!filtro.getDataConclusaoInicial().equals("0") && !filtro.getDataConclusaoFinal().equals("0")) {
			sql.append(" and a.dtconcluido >= '" + filtro.getDataConclusaoInicial() + "'");
			sql.append(" and a.dtconcluido <= '" + filtro.getDataConclusaoInicial() + "'");
		}

		//Filtra por atividades importantes
		if(!filtro.getImportante().equals("T")) {
			sql.append(" and a.snimportante = '" + filtro.getImportante() + "'");
		}

		//Filtra por atividades urgentes
		if(!filtro.getUrgente().equals("T")) {
			sql.append(" and a.snurgente = '" + filtro.getUrgente() + "'");
		}

		//Filtra apenas as atividades com lançamentos financeiros
		if(filtro.isFinanceiro()) {
			sql.append(" and a.tpatividade = 'L'");
		}

		//Filtra por tipo de usuário
		if(!filtro.getTipoUsuario().equals("0")) {
			sql.append(" and cdatividade in(select cdatividade from atividade_usuario where tpusuario = '" + filtro.getTipoUsuario() + "')");
		}

		sql.append("GROUP BY \n" +
				   "a.cdatividade,\n" +
				   "a.dstitulo,\n" +
				   "sa.nostatus,\n" +
				   "a.snimportante,\n" +
				   "a.snurgente,\n" +
				   "sa.cor,\n" +
				   "tf.nofase,\n" +
				   "tf.cor,\n" +
				   "gts.nogrupo,\n" +
				   "gtp.nogrupo,\n" +
				   "a.dtlimite,\n" +
				   "a.dtfatal,\n" +
				   "a.dtconcluido,\n" +
				   "CASE\n" +
				   "  WHEN p.nrcnj IS NOT NULL THEN CONVERT(p.nrcnj USING utf8) COLLATE utf8_general_ci\n" +
				   "  WHEN p.nrcnj IS NULL AND p.nrprocesso IS NULL THEN CONVERT(p.pasta USING utf8) COLLATE utf8_general_ci\n" +
				   "  ELSE CONVERT(p.nrprocesso USING utf8) COLLATE utf8_general_ci\n" +
				   "END,\n" +
				   "p.cdprocesso\n");

		//Classificação e Ordenação
		switch (filtro.getClassificacao()) {
			case 1:
				sql.append(" order by a.dstitulo\n");
				break;
			case 2:
				sql.append(" order by a.dtlimite\n");
				break;
			case 3:
				sql.append(" order by a.dtfatal\n");
				break;
			case 4:
				sql.append(" order by tf.nofase\n");
			default:
				sql.append(" order by a.dtlimite\n");
				break;
		}

		if(filtro.getOrdenacao().equals("ASC")) {
			sql.append(" asc");
		} else {
			sql.append(" desc");
		}

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(AtividadesGeralHomeDto.class));

		List<AtividadesGeralHomeDto> result = (List<AtividadesGeralHomeDto>) query.getResultList();
		return result;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<AgendaCalendarioHomeDto> agendaHomeResumo(FiltroAgenda filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select date(dtcompromisso) dataCompromisso, count(*) atividades from agenda where cdusuario in(" + filtro.getUsuario() + ")");
		sql.append(" and cdgrupo in(" + filtro.getGrupoTrabalho() + ")");
		sql.append(" and cdstatus in(" + filtro.getStatus() + ")");
		sql.append(" and tpatividade in(" + filtro.getTipos() + ")");
		sql.append(" and cdgrupo in(select cdgrupo from usuario_grupo_trabalho where cdusuario =:usuario)");
		sql.append(" and date(dtcompromisso) >= '2022-12-01'");
		sql.append(" group by date(dtcompromisso) ");
		
		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(AgendaCalendarioHomeDto.class));
			
		query.setParameter("usuario", filtro.getUsuario());
		
		List<AgendaCalendarioHomeDto> result = (List<AgendaCalendarioHomeDto>) query.getResultList();
		return result;
	}
}
