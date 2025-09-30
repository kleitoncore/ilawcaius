package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.Processo;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class PessoasIndicadoresRepositoryCustomImpl implements PessoasIndicadoresRepositoryCustom {

    @Autowired
    private EntityManager manager;

    @Override
    public Integer totalPessoas(long empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from pessoa where cdempresa = :empresa ");

        Query query = manager.createNativeQuery(sql.toString());
        query.setParameter("empresa", empresa);

        return query.getSingleResult().hashCode();
    }

    @Override
    public int totalPessoasAtivas(long empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from pessoa where cdempresa = :empresa and cdstatus = 0");

        Query query = manager.createNativeQuery(sql.toString());
        query.setParameter("empresa", empresa);

        return query.getSingleResult().hashCode();
    }

    @Override
    public int totalInativos(long empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from pessoa where cdempresa = :empresa and cdstatus = 1");

        Query query = manager.createNativeQuery(sql.toString());
        query.setParameter("empresa", empresa);

        return query.getSingleResult().hashCode();
    }

    @Override
    public int totalComProcessos(long empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from pessoa where cdpessoa in(select cdpessoa from partes) and cdempresa = :empresa ");

        Query query = manager.createNativeQuery(sql.toString());
        query.setParameter("empresa", empresa);

        return query.getSingleResult().hashCode();
    }

    @Override
    public int totalSemProcessos(long empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from pessoa where cdpessoa not in(select cdpessoa from partes) and cdempresa = :empresa ");

        Query query = manager.createNativeQuery(sql.toString());
        query.setParameter("empresa", empresa);

        return query.getSingleResult().hashCode();
    }

    @Override
    public int totalCadastradosEsteMes(long empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from pessoa where MONTH(dtcadastro) = MONTH (curdate()) and YEAR(dtcadastro) = YEAR(curdate()) and cdempresa = :empresa");

        Query query = manager.createNativeQuery(sql.toString());
        query.setParameter("empresa", empresa);

        return query.getSingleResult().hashCode();
    }

    @Override
    public List<PessoasFaixaEtariaDto> faixaEtaria(long empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "    CASE\n" +
                "        WHEN TIMESTAMPDIFF(YEAR, dtnascimento, CURDATE()) BETWEEN 0 AND 18 THEN '0 a 18'\n" +
                "        WHEN TIMESTAMPDIFF(YEAR, dtnascimento, CURDATE()) BETWEEN 19 AND 24 THEN '19 a 24'\n" +
                "        WHEN TIMESTAMPDIFF(YEAR, dtnascimento, CURDATE()) BETWEEN 25 AND 34 THEN '25 a 34'\n" +
                "        WHEN TIMESTAMPDIFF(YEAR, dtnascimento, CURDATE()) BETWEEN 35 AND 44 THEN '35 a 44'\n" +
                "        WHEN TIMESTAMPDIFF(YEAR, dtnascimento, CURDATE()) BETWEEN 45 AND 54 THEN '45 a 54'\n" +
                "        WHEN TIMESTAMPDIFF(YEAR, dtnascimento, CURDATE()) BETWEEN 55 AND 64 THEN '55 a 64'\n" +
                "        WHEN TIMESTAMPDIFF(YEAR, dtnascimento, CURDATE()) > 64 THEN '65 ou mais'\n" +
                "        ELSE 'Sem informação'\n" +
                "    END AS faixa,\n" +
                "    COUNT(*) AS quantidade\n" +
                "FROM pessoa\n" +
                "WHERE cdempresa = :empresa\n" +
                "GROUP BY faixa\n" +
                "ORDER BY faixa");

        Query query = manager.createNativeQuery(sql.toString())
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(PessoasFaixaEtariaDto.class));

        query.setParameter("empresa", empresa);
        List<PessoasFaixaEtariaDto> result = (List<PessoasFaixaEtariaDto>) query.getResultList();
        return result;
    }

    @Override
    public List<PessoaSexoDto> sexo(long empresa) {
        return Collections.emptyList();
    }

    @Override
    public List<PessoaProfissaoDto> profissao(long empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n" +
                "    COALESCE(NULLIF(profissao, null), 'Sem Informação') AS profissao,\n" +
                "    COUNT(*) AS quantidade\n" +
                "FROM pessoa\n" +
                "WHERE cdempresa = :empresa\n" +
                "GROUP BY profissao\n" +
                "ORDER BY profissao");

        Query query = manager.createNativeQuery(sql.toString())
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(PessoaProfissaoDto.class));

        query.setParameter("empresa", empresa);
        List<PessoaProfissaoDto> result = (List<PessoaProfissaoDto>) query.getResultList();
        return result;
    }

    @Override
    public List<Pessoa> aniversariantes(FiltroAniversariantesDto filtro) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from pessoa where cdempresa = " + filtro.getEmpresa() + " and MONTH(dtnascimento) = " + filtro.getMes());

        if(filtro.getIdade() != 0) {
            sql.append(" and YEAR(CURDATE()) - YEAR(dtnascimento) = " + filtro.getIdade());
        }

        sql.append(" order by dtnascimento ");

        Query query = manager.createNativeQuery(sql.toString(), Pessoa.class);
        return query.getResultList();
    }
}
