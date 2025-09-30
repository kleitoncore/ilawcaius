package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.models.TituloAtividade;
import com.br.ilawgestao.domains.models.TransacaoFinanceira;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

public class TransacaoFinanceiraReposioryCustomImpl implements TransacaoFinanceiraReposioryCustom{

    @Autowired
    private EntityManager manager;

    @Override
    public List<TransacaoFinanceira> consultarTransacoesPorData(long empresa, String dataInicial, String dataFinal) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from transacao_financeira where dtregistro BETWEEN :dtini and :dtfim ");
        sql.append(" and cdlancamento in(select cdlancamento from lancamento where cdusuario_criou in(");
        sql.append("select cdusuario from usuario where cdempresa =:empresa)) ");

        TypedQuery<TransacaoFinanceira> query = manager.createQuery(sql.toString(), TransacaoFinanceira.class);
        query.setParameter("dtini", dataInicial);
        query.setParameter("dtfim", dataFinal);
        query.setParameter("empresa", empresa);

        return query.getResultList();
    }
}
