package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.dto.FiltroAniversariantesDto;
import com.br.ilawgestao.domains.dto.PessoaProfissaoDto;
import com.br.ilawgestao.domains.dto.PessoaSexoDto;
import com.br.ilawgestao.domains.dto.PessoasFaixaEtariaDto;
import com.br.ilawgestao.domains.models.Pessoa;

import java.util.List;

public interface PessoasIndicadoresRepositoryCustom {
    Integer totalPessoas(long empresa);
    int totalPessoasAtivas(long empresa);
    int totalInativos(long empresa);
    int totalComProcessos(long empresa);
    int totalSemProcessos(long empresa);
    int totalCadastradosEsteMes(long empresa);
    List<PessoasFaixaEtariaDto> faixaEtaria(long empresa);
    List<PessoaSexoDto> sexo(long empresa);
    List<PessoaProfissaoDto> profissao(long empresa);
    List<Pessoa> aniversariantes(FiltroAniversariantesDto filtro);
}
