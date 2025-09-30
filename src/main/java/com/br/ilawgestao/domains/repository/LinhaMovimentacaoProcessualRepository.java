package com.br.ilawgestao.domains.repository;

import com.br.ilawgestao.domains.models.HistoricoPessoa;
import com.br.ilawgestao.domains.models.LinhaMovimentacaoProcessual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinhaMovimentacaoProcessualRepository extends JpaRepository<LinhaMovimentacaoProcessual, Long> {
    List<LinhaMovimentacaoProcessual> findByIdArquivoMovimentoProcessual(String arquivo);
}
