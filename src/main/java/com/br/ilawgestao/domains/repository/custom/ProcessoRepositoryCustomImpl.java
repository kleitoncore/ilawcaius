package com.br.ilawgestao.domains.repository.custom;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.br.ilawgestao.domains.models.AtividadeShort;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.repository.filtros.FiltroProcesso;

public class ProcessoRepositoryCustomImpl implements ProcessoRepositoryCustom {
	
	@Autowired
	private EntityManager manager;

	@Override
	public List<Processo> consultarProcessos(FiltroProcesso filtro) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct p from Processo p ");
		sql.append("         left join Partes pa on p.codigo = pa.processo.codigo left join Pessoa ca on ca.codigo = pa.pessoa.codigo ");
		sql.append("         left join Partes pr on p.codigo = pr.processo.codigo left join Pessoa cr on cr.codigo = pr.pessoa.codigo ");
		sql.append(" where p.empresa.codigo = :empresa and p.status <> 3 ");
		sql.append("   and p.grupoTrabalho.codigo in(select grupo.codigo from UsuarioGrupoTrabalho where usuario.codigo =:usuario) ");
		
		//Pesquisa por nome de pessoa, sendo autor ou réu
		if(filtro.getPessoa() != null) {
			sql.append(" and (ca.nome like :nomePessoa or cr.nome like :nomePessoa) ");
		}
		
		//Consulta por Grupo de Trabalho
		if(filtro.getGrupoTrabalho() != null) {
			sql.append(" and p.grupoTrabalho.codigo =:grupoTrabalho ");
		}
		
		//Pesquisa por Tipo de Ação
		if(filtro.getTipoAcao() != null) {
			sql.append(" and p.tipoAcao.codigo =:tipoAcao ");
		}
		
		//Pesquisa por Status Processual
		if(filtro.getStatusProcessual() != null) {
			sql.append(" and p.statusProcessual.codigo =:statusProcessual ");
		}
		
		//Pesauisa por Tipo de Decisão
		if(filtro.getTipoDecisao() != null) {
			sql.append(" and p.tipoDecisao.codigo =:tipoDecisao ");
		}
		
		//Pesquisa por Pasta
		if(filtro.getPasta() != null) {
			sql.append(" and p.pasta =:pasta ");
		}
		
		//Pesuisa por número de Processo
		if(filtro.getNumeroProcesso() != null) {
			sql.append(" and p.nrProcesso =:numeroProcesso ");
		}
		
		//Pesquisa por Número de CNJ
		if(filtro.getNumeroCnj() != null) {
			sql.append(" and p.nrCnj =:numeroCnj ");
		}
		
		//Importante para mim
		if(filtro.getImportanciaParaMim() != null) {
			if(filtro.getImportanciaParaMim().equals("SIM")) {
					sql.append(" and p.codigo in(select pp.processo.codigo from ProcessoImportancia pp where pp.processo.codigo = p.codigo and pp.usuario.codigo =:codigoUsuario)");
			}
		}

		//Importante para empresa
		if(filtro.getImportanciaParaEmpresa() != null) {
			if(filtro.getImportanciaParaEmpresa().equals("SIM")) {
				sql.append(" and p.snImportante = 'S' ");
			} else {
				sql.append(" and p.snImportante = 'N' ");
			}
		}

		//Estratégico
		if(filtro.getEstrategico() != null) {
			if(filtro.getEstrategico().equals("SIM")) {
				sql.append(" and p.estrategico = 'S' ");
			} else {
				sql.append(" and p.estrategico = 'N' ");
			}
		}
		
		TypedQuery<Processo> query = manager.createQuery(sql.toString(), Processo.class);
		
		query.setParameter("empresa", filtro.getEmpresa().getCodigo());
		query.setParameter("usuario", Long.parseLong(filtro.getUsuario()));
		
		if(filtro.getPessoa() != null) {
			query.setParameter("nomePessoa", "%" + filtro.getPessoa() + "%");
		}
		
		if(filtro.getGrupoTrabalho() != null) {
			query.setParameter("grupoTrabalho", filtro.getGrupoTrabalho().getCodigo());
		}
		
		if(filtro.getTipoAcao() != null) {
			query.setParameter("tipoAcao", filtro.getTipoAcao().getCodigo());
		}
		
		if(filtro.getStatusProcessual() != null) {
			query.setParameter("statusProcessual", filtro.getStatusProcessual().getCodigo());
		}
		
		if(filtro.getTipoDecisao() != null) {
			query.setParameter("tipoDecisao", filtro.getTipoDecisao().getCodigo());
		}
		
		if(filtro.getPasta() != null) {
			query.setParameter("pasta", filtro.getPasta());
		}
		
		if(filtro.getNumeroProcesso() != null) {
			query.setParameter("numeroProcesso", filtro.getNumeroProcesso());
		}
		
		if(filtro.getNumeroCnj() != null) {
			query.setParameter("numeroCnj", filtro.getNumeroCnj());
		}
		
		if(filtro.getImportanciaParaMim() != null) {			
			if(filtro.getImportanciaParaMim().equals("SIM")) {
				query.setParameter("codigoUsuario", Long.parseLong(filtro.getUsuario()));
			}
		}
		
		return query.getResultList();
	}

	@Override
	public List<Processo> consultaProcessoPorIndice(String indice, long empresa, long usuario) {
		StringBuilder sqlProcesso = new StringBuilder();
		sqlProcesso.append("select p from Processo p where p.codigo in(select processo.codigo from IndiceProcesso where indice like :indice and empresa.codigo =:empresa) and p.status <> 3");
		sqlProcesso.append(" and p.grupoTrabalho.codigo in(select grupo.codigo from UsuarioGrupoTrabalho where usuario.codigo =:usuario) ");

		TypedQuery<Processo> queryProcesso = manager.createQuery(sqlProcesso.toString(), Processo.class);
		queryProcesso.setParameter("indice", "%" + indice + "%");
		queryProcesso.setParameter("empresa", empresa);
		queryProcesso.setParameter("usuario", usuario);
		return queryProcesso.getResultList();
	}

	@Override
	public List<Processo> consultarProcessosParados(long empresa, long dias, FiltroProcesso filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from processo\n" +
				"where cdempresa =:empresa\n" +
				"  -- Com base em alguma alteração nos registros principais do processo\n" +
				"  and (dtultima_movimentacao < DATE_SUB(CURDATE(),INTERVAL :dias DAY) or dtcadastro < DATE_SUB(CURDATE(),INTERVAL :dias DAY))\n" +
				"  -- Andamentos processuais\n" +
				"  and (cdprocesso not in(select cdprocesso from historico_processo where dthistorico > DATE_SUB(CURDATE(),INTERVAL :dias DAY)) or\n" +
				"       cdprocesso not in(select cdprocesso from historico_processo))\n" +
				"  -- Atividades\n" +
				"  and cdprocesso not in(select cdprocesso from atividade where cdprocesso is not null and cdatividade in(select cdatividade from historico_atividade where dthistorico > DATE_SUB(CURDATE(),INTERVAL :dias DAY)))  \n" +
				"  -- Arquivos\n" +
				"  and cdprocesso not in(select cdprocesso from arquivo_processo where dtarquivo > DATE_SUB(CURDATE(),INTERVAL :dias DAY)) \n" +
				"  -- Lançamentos financeiros\n" +
				"  and cdprocesso not in(select cdprocesso from lancamento where dtlancamento > DATE_SUB(CURDATE(),INTERVAL :dias DAY) and cdprocesso is not null) \n" +
				"  -- Pagamentos\n" +
				"  and cdprocesso not in(select cdprocesso from pagamento_processo where dtpagamento > DATE_SUB(CURDATE(),INTERVAL :dias DAY)) \n" +
				"  -- Custas\n" +
				"  and cdprocesso not in(select cdprocesso from custas_processo where dtpagamento > DATE_SUB(CURDATE(),INTERVAL :dias DAY))");

		//Filtros
		//Filtra por número de cnj
		if(!filtro.getNumeroCnj().equals("")) {
			sql.append(" and nrcnj = '" + filtro.getNumeroCnj() + "'");
		}

		//Filtra por número de processo
		if(!filtro.getNumeroProcesso().equals("")) {
			sql.append(" and nrprocesso = '" + filtro.getNumeroProcesso() + "'");
		}

		//Filtra por pessoa
		if(!filtro.getPessoa().equals("")) {
			sql.append(" and cdprocesso in(select cdprocesso from partes where cdpessoa in(select cdpessoa from pessoa where nopessoa like '" + "%" + filtro.getPessoa() + "%" + "'))");
		}

		Query query = manager.createNativeQuery(sql.toString(), Processo.class);
		query.setParameter("empresa", empresa);
		query.setParameter("dias", dias);
		return query.getResultList();
	}

	@Override
	public List<Processo> consultarProcessosPorPessoa(long pessoa) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct p from Processo p ");
		sql.append("         left join Partes pa on p.codigo = pa.processo.codigo left join Pessoa ca on ca.codigo = pa.pessoa.codigo ");
		sql.append("         left join Partes pr on p.codigo = pr.processo.codigo left join Pessoa cr on cr.codigo = pr.pessoa.codigo ");
		sql.append(" where (ca.codigo =:pessoa or cr.codigo =:pessoa) and p.status <> 3 ");
		
		TypedQuery<Processo> query = manager.createQuery(sql.toString(), Processo.class);
		
		query.setParameter("pessoa", pessoa);
		
		return query.getResultList();
	}
}
