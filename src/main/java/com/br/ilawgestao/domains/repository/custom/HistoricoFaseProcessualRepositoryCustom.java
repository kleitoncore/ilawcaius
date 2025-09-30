package com.br.ilawgestao.domains.repository.custom;

import com.br.ilawgestao.domains.dto.GraficoAtividadeFaseDto;
import com.br.ilawgestao.domains.dto.GraficoAtividadesStatusDto;
import com.br.ilawgestao.domains.dto.HistoricoFaseProcessualViewDto;
import com.br.ilawgestao.domains.repository.filtros.FiltroHistoricoAtividadeFase;

import java.util.List;

public interface HistoricoFaseProcessualRepositoryCustom {
    List<HistoricoFaseProcessualViewDto> consultarAtividadesFases(FiltroHistoricoAtividadeFase filltro);
    List<GraficoAtividadeFaseDto> graficoFaseProcessualAtividade(long processo);
    List<GraficoAtividadesStatusDto> graficoStatusAtividade(long processo);
}
