package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.dto.GraficoAtividadeFaseDto;
import com.br.ilawgestao.domains.dto.GraficoAtividadesStatusDto;
import com.br.ilawgestao.domains.dto.HistoricoFaseProcessualViewDto;
import com.br.ilawgestao.domains.repository.filtros.FiltroHistoricoAtividadeFase;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class HistoricoFaseProcessualRepositoryCustomImpl implements HistoricoFaseProcessualRepositoryCustom {

    @Autowired
    EntityManager manager;

    @Override
    public List<HistoricoFaseProcessualViewDto> consultarAtividadesFases(FiltroHistoricoAtividadeFase filtro) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.cdatividade codigo,\n" +
                "       a.dstitulo titulo, \n" +
                "       a.tpatividade tipo,\n" +
                "       gt.nogrupo subGrupo,\n" +
                "       f.nofase fase,\n" +
                "       sa.nostatus status,\n" +
                "       sa.cor corStatus, \n" +
                "       GROUP_CONCAT(DISTINCT u.nousuario ORDER BY u.nousuario ASC SEPARATOR ', ') responsaveis,\n" +
                "       a.dtlimite dataLimite,\n" +
                "       a.dtfatal dataFatal, \n" +
                "       f.cor, \n" +
                "       a.snimportante importante,\n" +
                "       a.snurgente urgente \n" +
                "  from historico_fase_processual hf,\n" +
                "       tipo_fase f,\n" +
                "       status_atividade sa,\n" +
                "       atividade a,\n" +
                "       atividade_usuario au,\n" +
                "       grupo_trabalho gt,\n" +
                "       usuario u\n" +
                " where a.cdatividade = hf.cdatividade\n" +
                "   and f.cdfase = hf.cdfase \n" +
                "   and sa.cdstatus = a.cdstatus \n" +
                "   and gt.cdgrupo = a.cdsubgrupo \n" +
                "   and u.cdusuario = au.cdusuario\n" +
                "   and au.tpusuario = 'R'\n" +
                "   and a.cdatividade = au.cdatividade \n" +
                "   and hf.cdprocesso = " + filtro.getProcesso() + "\n");

        if(!filtro.getFase().equals("0")) {
            sql.append(" and f.nofase = '" + filtro.getFase() + "'\n");
        }

        if(!filtro.getStatus().equals("0")) {
            sql.append(" and sa.cdstatus in(" + filtro.getStatus() + ")" + "\n");
        }

        if(!filtro.getDataLimiteInicial().equals("0") && !filtro.getDataLimiteFinal().equals("0")) {
            sql.append(" and a.dtlimite >= '" + filtro.getDataLimiteInicial() + "'\n");
            sql.append(" and a.dtlimite <= '" + filtro.getDataLimiteFinal() + "'\n");
        }

        if(!filtro.getDataFatalInicial().equals("0") && !filtro.getDataFatalFinal().equals("0")) {
            sql.append(" and a.dtfatal >= '" + filtro.getDataFatalInicial() + "'\n");
            sql.append(" and a.dtfatal <= '" + filtro.getDataFatalFinal() + "'\n");
        }

        if(!filtro.getImportante().equals("T")) {
            sql.append(" and a.snimportante = '" + filtro.getImportante() + "'");
        }

        if(!filtro.getUrgente().equals("T")) {
            sql.append(" and a.snurgente = '" + filtro.getUrgente() + "'");
        }

        sql.append(" group by a.cdatividade,\n" +
                "          f.nofase,\n" +
                "          sa.nostatus,\n" +
                "          a.dtlimite,\n" +
                "          a.dtfatal, \n" +
                "          a.dstitulo, \n" +
                "          f.cor \n");

        if(filtro.getClassificacao() == 1) {
            sql.append(" order by f.nofase \n");
        } else if(filtro.getClassificacao() == 2) {
            sql.append(" order by a.dtlimite \n");
        } else {
            sql.append(" order by a.dtfatal \n");
        }

        if(filtro.getOrdem() == 1) {
            sql.append(" asc ");
        } else {
            sql.append(" desc ");
        }

        Query query = manager.createNativeQuery(sql.toString())
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(HistoricoFaseProcessualViewDto.class));

        List<HistoricoFaseProcessualViewDto> result = (List<HistoricoFaseProcessualViewDto>) query.getResultList();

        return result;
    }

    @Override
    public List<GraficoAtividadeFaseDto> graficoFaseProcessualAtividade(long processo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select tf.nofase fase,\n" +
                "          tf.cor, \n" +
                "          count(hfp.cdhistorico) total\n" +
                "  from historico_fase_processual hfp,\n" +
                "       tipo_fase tf,\n" +
                "       status_atividade sa, \n" +
                "       atividade a \n" +
                " where tf.cdfase = hfp.cdfase\n" +
                "   and sa.cdstatus = a.cdstatus \n" +
                "   and a.cdatividade = hfp.cdatividade \n" +
                "   and hfp.cdprocesso = " + processo + "\n");
        sql.append(" group by tf.nofase, tf.cor");

        Query query = manager.createNativeQuery(sql.toString())
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadeFaseDto.class));

        List<GraficoAtividadeFaseDto> result = (List<GraficoAtividadeFaseDto>) query.getResultList();
        return result;
    }

    @Override
    public List<GraficoAtividadesStatusDto> graficoStatusAtividade(long processo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sa.nostatus status,\n" +
                "          sa.cor, \n" +
                "          count(hfp.cdhistorico) total\n" +
                "  from historico_fase_processual hfp,\n" +
                "       tipo_fase tf,\n" +
                "       status_atividade sa, \n" +
                "       atividade a \n" +
                " where tf.cdfase = hfp.cdfase\n" +
                "   and sa.cdstatus = a.cdstatus \n" +
                "   and a.cdatividade = hfp.cdatividade \n" +
                "   and hfp.cdprocesso = " + processo + "\n");
        sql.append(" group by sa.nostatus, sa.cor");

        Query query = manager.createNativeQuery(sql.toString())
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(GraficoAtividadesStatusDto.class));

        List<GraficoAtividadesStatusDto> result = (List<GraficoAtividadesStatusDto>) query.getResultList();
        return result;
    }
}
