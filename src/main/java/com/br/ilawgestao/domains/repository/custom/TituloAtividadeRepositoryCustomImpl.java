package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.models.ProjecaoUsuario;
import com.br.ilawgestao.domains.models.TituloAtividade;
import com.br.ilawgestao.domains.repository.filtros.FiltroTituloAtividade;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class TituloAtividadeRepositoryCustomImpl implements TituloAtividadeRepositoryCustom {

    @Autowired
    private EntityManager manager;

    @Override
    public List<TituloAtividade> consultarTituloAtividade(FiltroTituloAtividade filtro) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t from TituloAtividade t where t.empresa.codigo =:empresa ");

        if(filtro.getFase() != 0) {
            sql.append(" and t.fase.codigo = " + filtro.getFase());
        }

        if(filtro.getTitulo() != "" && filtro.getTitulo() != null) {
            sql.append(" and t.titulo like '" + "%" + filtro.getTitulo() + "%" + "'");
        }

        sql.append(" and t.status = '" + filtro.getStatus() + "'");

        //Classificação
        if(filtro.getClassificacao() == 1) {
            sql.append(" order by t.titulo ");
        } else if(filtro.getClassificacao() == 2) {
            sql.append(" order by t.fase.nome ");
        } else if(filtro.getClassificacao() == 3) {
            sql.append(" order by t.pontos ");
        }

        //Ordenação
        if(filtro.getOrdenacao() == 1) {
            sql.append(" asc");
        } else {
            sql.append(" desc");
        }

        TypedQuery<TituloAtividade> query = manager.createQuery(sql.toString(), TituloAtividade.class);
        query.setParameter("empresa", filtro.getEmpresa());
        return query.getResultList();
    }
}
