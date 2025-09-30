package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Agencia;
import com.br.ilawgestao.domains.models.ContaFinanceira;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgenciaDto {
    private long codigo;
    private long codigoBanco;
    private String numeroAgencia;
    private String numeroConta;
    private String tipoConta;
    private ContaFinanceira contaFinanceira;

    public static AgenciaDto build(Agencia agencia) {
        AgenciaDto dto = new AgenciaDto();
        dto.setCodigo(agencia.getCodigo());
        dto.setNumeroAgencia(agencia.getNumeroAgencia());
        dto.setCodigoBanco(agencia.getCodigoBanco());
        dto.setNumeroConta(agencia.getNumeroConta());
        dto.setTipoConta(agencia.getTipoConta());
        dto.setContaFinanceira(agencia.getContaFinanceira());
        return dto;
    }
}
