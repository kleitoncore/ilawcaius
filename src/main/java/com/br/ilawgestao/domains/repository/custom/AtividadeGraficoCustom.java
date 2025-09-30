package com.br.ilawgestao.domains.repository.custom;

import java.util.List;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.models.Atividade;

public interface AtividadeGraficoCustom {
	
	List<GraficoAtividadeDto> graficoAtividades(long empresa, String dataInicial, String dataFinal);
	List<GraficoAtividadeDto> graficoAtividadesDataLimite(long empresa, String dataInicial, String dataFinal);
	List<GraficoAtividadeDto> graficoAtividadesConcluidos(long empresa, String dataInicial, String dataFinal);
	List<GraficoAtividadesStatusDto> graficoAtividadesStatus(long empresa, String dataInicial, String dataFinal);
	List<GraficoAtividadeSituacaoDto> graficoAtividadesSituacao(long empresa, String dataInicial, String dataFinal);
	List<GraficoAtividadesStatusDto> graficoAtividadesStatusUsuarioPainel(long usuario);
	List<GraficoAtividadeGrupoDto> graficoAtividadeGrupo(long empresa, String dataInicial, String dataFinal);
	int graficoAtividadeGruposOutros(long empresa, String grupos, String dataInicial, String dataFinal);
	List<GraficoAtividadeFaseDto> graficoAtividadeFase(long empresa, String dataInicial, String dataFinal);
	List<GraficoAtividadeFaseDto> graficoAtividadeFasePorProcesso(long processo,long usuario);
	List<GraficoAtividadesStatusDto> graficoAtividadesStatusPorProcesso(long processo,long usuario);
	List<GraficoFaseProcessoAtividadeDTO> graficoProcessoFaseAtividade(long empresa, String dataInicial, String dataFinal);
	List<GraficoPontuacaoTotalDto> graficoPontuacaoTotal(long empresa, String dataInicial, String dataFinal);
	List<PontosUsuariosProjecaoDadosDto> pontosUsuariosProjecaoDados(long empresa, String dataInicial, String dataFinal);
	List<Atividade> consultarAtividadesPorUsuario(long usuario, String dataInicial, String dataFinal, String tipoConsulta);
	List<AtividadesRankingDto> consultarRankingAtividades(long empresa,String dataInicial, String dataFinal);
	List<AtividadesRankingDto> consultarRankingAtividadesDemis(long empresa,String dataInicial, String dataFinal,String atividades);

}
