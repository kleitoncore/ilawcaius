package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.*;
import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.Data;

import java.util.List;

@Data
public class AtividadesUsuariosDesempenhoDto {
    private long codigoAtividade;
    private long codigoUsuario;
    private String usuario;
    private String dataRegistro;
    private String dataConcluido;
    private String dataLimite;
    private String dataFatal;
    private long codigoProcesso;
    private String processo;
    private String fase;
    private String status;
    private String atividade;
    private long pontos;
    private List<HistoricoAtividadeDTO> historico;

    public static AtividadesUsuariosDesempenhoDto build(Atividade atividade, Usuario usuario, List<HistoricoAtividadeDTO> historico,
                                                        TituloAtividade titulo) {
        AtividadesUsuariosDesempenhoDto dto = new AtividadesUsuariosDesempenhoDto();
        dto.setCodigoAtividade(atividade.getCodigo());
        dto.setCodigoUsuario(usuario.getCodigo());
        dto.setUsuario(usuario.getNome());
        dto.setDataRegistro(DatasUtil.formatarDataTela(atividade.getDtRegistro()));
        dto.setDataLimite(DatasUtil.formatarDataTela(atividade.getDtLimite()));
        if(atividade.getDtFatal() != null) {
            dto.setDataFatal(DatasUtil.formatarDataTela(atividade.getDtFatal()));
        } else {
            dto.setDataFatal(null);
        }
        if(atividade.getDtConcluido() != null) {
            dto.setDataConcluido(DatasUtil.formatarDataTela(atividade.getDtConcluido()));
        } else {
            dto.setDataConcluido(null);
        }

        if(atividade.getProcesso() != null) {
            dto.setCodigoProcesso(atividade.getProcesso().getCodigo());
            dto.setProcesso(atividade.getProcesso().getNrCnj());
            if(atividade.getProcesso().getFase() != null) {
                dto.setFase(atividade.getProcesso().getFase().getNome());
            } else {
                dto.setFase(null);
            }
        }

        dto.setStatus(atividade.getStatus().getStatus());
        dto.setAtividade(titulo.getTitulo());
        dto.setPontos(titulo.getPontos());
        return dto;
    }
}
