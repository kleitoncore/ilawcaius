package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.br.ilawgestao.domains.dto.MovimentoProcessualEmpresaDTO;
import com.br.ilawgestao.domains.dto.RelatorioProcessoDto;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.ilawgestao.domains.models.HistoricoProcesso;
import com.br.ilawgestao.domains.models.MovimentoProcessualRelatorio;
import com.br.ilawgestao.domains.repository.filtros.FiltroMovimentacaoProcessualRelatorio;
import com.br.ilawgestao.domains.utils.DatasUtil;

public class MovimentoProcessualRelatorioRepositoryCustomImpl implements MovimentoProcessualRelatorioRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<MovimentoProcessualRelatorio> consultarMovimentoProcessual(
			FiltroMovimentacaoProcessualRelatorio filtro) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select m from MovimentoProcessualRelatorio m ");
		
		sql.append(" where cdempresa = " + filtro.getEmpresa());
		
		//Data de Carregamento dos arquivos
		if(filtro.getDtCarregamento() != null) {
			sql.append(" and dtCarregamento = '" + filtro.getDtCarregamento() + "'");
		}
		
		//Data de movimento processual
		if(filtro.getDtMovimentoInicial() != null && filtro.getDtMovimentacaoFinal() != null) {
			sql.append(" and dtMovimentacao >= '" + filtro.getDtMovimentoInicial() + "'");
			sql.append(" and dtMovimentacao <= '" + filtro.getDtMovimentacaoFinal() + "'");
		}
		
		//Grupo de Trabalho
		if(filtro.getGrupo() != null) {
			sql.append(" and cdGrupo in(" + filtro.getGrupo() + ")");
		}
		
		//Status Processual
		if(filtro.getStatusProcessual() != null) {
			sql.append(" and cdprocesso in(select codigo from Processo where statusProcessual.codigo in(" + filtro.getStatusProcessual() + "))");
		}
		
		//Número do processo
		if(filtro.getNumeroPrcesso() != null) {
			sql.append(" and nrcnj = '" + filtro.getNumeroPrcesso());
		}
		
		//Pasta
		if(filtro.getNumeroPasta() != null) {
			sql.append(" and nrpasta = '" + filtro.getNumeroPasta());
		}
		
		//Movimentação
		if(filtro.getMovimentacao() != null) {
			sql.append(" and movimentacao like " + "%" + filtro.getMovimentacao() + "%");
		}
		
		//Oculto sim ou não
		if(filtro.getSnOculto() != null) {
			sql.append(" and snOculto = '" + filtro.getSnOculto());
		}
		
		//Classificação e ordenação
		if(filtro.getClassificacao() != null) {
			if(filtro.getClassificacao().equals("1")) {
				sql.append(" order by dtMovimentacao");
			} else if(filtro.getClassificacao().equals("2")) {
				sql.append(" order by dsmovimentacao ");
			} else if(filtro.getClassificacao().equals("3")) {
				sql.append(" order by autor ");
			} else if(filtro.getClassificacao().equals("4")) {
				sql.append(" order by reu ");
			} else if(filtro.getClassificacao().equals("5")) {
				sql.append(" order by nosituacao ");
			} else if(filtro.getClassificacao().equals("6")) {
				sql.append(" order by nogrupo ");
			}
		}
		
		//Ordenação
		if(filtro.getOrdenacao() != null) {
			if(filtro.getOrdenacao().equals("1")) {
				sql.append(" asc");
			} else {
				sql.append(" desc");
			}
		}
		
		TypedQuery<MovimentoProcessualRelatorio> query = manager.createQuery(sql.toString(), MovimentoProcessualRelatorio.class);
		
		return query.getResultList();
	}

	@Override
	public HistoricoProcesso consultaUltimoHistorico(long processo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select h from HistoricoProcesso h where h.processo.codigo =:processo and h.dataOcorrencia = (select max(h2.dataOcorrencia) ");
		sql.append(" from HistoricoProcesso h2 where h2.processo.codigo = h.processo.codigo and h2.dataHistorico <> :dataHistorico) ");
		
		TypedQuery<HistoricoProcesso> query = manager.createQuery(sql.toString(), HistoricoProcesso.class);
		query.setParameter("processo", processo);
		query.setParameter("dataHistorico", DatasUtil.getDataAtual());
		query.setMaxResults(1);
		HistoricoProcesso historico = null;
		try {
			historico = query.getSingleResult();
			return historico;
		} catch ( NoResultException e ) {
			return historico;
		}
	}

	@Override
	public List<MovimentoProcessualEmpresaDTO> processosMovimentosPorGrupoEmpresa(String dataMovvimentacao) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.cdprocesso codigoProcesso, p.pasta nrPasta, p.nrcnj nrCnj, group_concat(distinct aut.nopessoa) autor, ");
		sql.append(" group_concat(distinct reu.nopessoa) reu, ts.descricao statusProcessual, date(mpe.dtmovimentacao) dtMovimentacao, ");
		sql.append(" mpe.dsmovimentacao movimentacao, gt.cdgrupo codigoGrupo, gt.nogrupo nomeGrupo, ");
		sql.append(" p.cdempresa codEmpresa, p.snhistorico snHistorico ");
		sql.append(" from processo p left join partes ppa on p.cdprocesso = ppa.cdprocesso ");
		sql.append("  left join pessoa aut on aut.cdpessoa = ppa.cdpessoa and ppa.tpparte = 'A' ");
		sql.append("  left join partes ppr on p.cdprocesso = ppr.cdprocesso ");
		sql.append("  left join pessoa reu on reu.cdpessoa = ppr.cdpessoa and ppr.tpparte = 'R', ");
		sql.append(" grupo_trabalho gt, status_processual ts, movimento_push mpe ");
		sql.append("  where gt.cdgrupo = p.cdgrupo ");
		sql.append("    and ts.cdstatus = p.cdstatus_processual and ts.cdempresa = p.cdempresa and p.nrcnj <> '' ");
		sql.append("    and p.cdempresa = mpe.cdempresa and p.nrcnj = mpe.nrprocessocnj ");
		sql.append("    and p.snpush = 'S' and p.cdstatus = 0  and date(mpe.dtmovimentacao) =:data ");
		sql.append("  group by p.cdprocesso, p.pasta, p.nrcnj,ts.descricao, mpe.dtmovimentacao, mpe.dsmovimentacao ");

		Query query = manager.createNativeQuery(sql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(MovimentoProcessualEmpresaDTO.class));

		query.setParameter("data", dataMovvimentacao);

		List<MovimentoProcessualEmpresaDTO> result = (List<MovimentoProcessualEmpresaDTO>) query.getResultList();

		return result;
	}
}
