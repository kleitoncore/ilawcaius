package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.models.AtividadeShort;
import com.br.ilawgestao.domains.models.TituloAtividade;
import com.br.ilawgestao.domains.models.TransferenciaFinanceira;
import com.br.ilawgestao.domains.repository.filtros.FiltroTranferenciaFinanceira;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

public class TransferenciaFinanceiraRepositoryCustomImpl implements TransferenciaFinanceiraRepositoryCustom {

    @Autowired
    private EntityManager manager;

    @Override
    public List<TransferenciaFinanceira> consultarTransferencias(FiltroTranferenciaFinanceira filtro) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from transferencia_financeira where cdusuario in(select cdusuario from usuario where cdempresa = :empresa) ");

        if(!filtro.getDataInicial().equals("N") && !filtro.getDataFinal().equals("N")) {
            sql.append(" and dtregistro >= " + "'" + filtro.getDataInicial() + "'");
            sql.append(" and dtregistro <= " + "'" + filtro.getDataFinal() + "'");
        }

        if(filtro.getConta() != 0) {
            if(filtro.getTipo().equals("O")) {
                sql.append(" and cdorigem = " + filtro.getConta());
            } else if(filtro.getTipo().equals("D")) {
                sql.append(" and cddestino = " + + filtro.getConta());
            } else {
                sql.append(" and (cdorigem = " + + filtro.getConta() + " + or cddestino = " + filtro.getConta());
            }
        }

        if(filtro.getCodigo() == 0) {
            sql.append(" and 2 = 1 ");
        }

        sql.append(" order by dtregistro desc ");

        Query query = manager.createNativeQuery(sql.toString(), TransferenciaFinanceira.class);
        query.setParameter("empresa", filtro.getEmpresa());

        return query.getResultList();
    }
}
