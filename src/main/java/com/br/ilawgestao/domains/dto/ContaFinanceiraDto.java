package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.ContaFinanceira;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.utils.DatasUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaFinanceiraDto {
    private long codigo;
    private String nome;
    private String nomeFormat;
    private double saldo;
    private String saldoFormat;
    private String dtRegistro;
    private Empresa empresa;
    private String status;
    private String tipoConta;
    private AgenciaDto agencia;

    public static ContaFinanceiraDto build(ContaFinanceira conta) {
        ContaFinanceiraDto dto = new ContaFinanceiraDto();
        dto.setCodigo(conta.getCodigo());
        dto.setNome(conta.getNome());
        dto.setSaldo(conta.getSaldo());
        dto.setDtRegistro(conta.getDtRegistro());
        dto.setEmpresa(conta.getEmpresa());
        dto.setStatus(conta.getStatus());
        dto.setTipoConta(conta.getTipoConta());
        return dto;
    }

    public static ContaFinanceira build(ContaFinanceiraDto dto) {
        ContaFinanceira conta = new ContaFinanceira();
        conta.setCodigo(dto.getCodigo());
        conta.setNome(dto.getNome());
        conta.setSaldo(dto.getSaldo());
        conta.setDtRegistro(dto.getDtRegistro());
        conta.setEmpresa(dto.getEmpresa());
        conta.setStatus(dto.getStatus());
        conta.setTipoConta(dto.getTipoConta());
        return conta;
    }

    public static ContaFinanceiraDto buildConsulta(ContaFinanceira conta, AgenciaDto agencia) {
        ContaFinanceiraDto dto = new ContaFinanceiraDto();
        dto.setCodigo(conta.getCodigo());
        dto.setNome(conta.getNome());
        dto.setSaldo(conta.getSaldo());
        Locale ptBr = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
        BigDecimal valorSaldo = new BigDecimal(conta.getSaldo());
        String formato = nf.format(valorSaldo);
        dto.setSaldoFormat(formato);
        dto.setNomeFormat(conta.getNome() + " - Saldo: " + formato);
        dto.setDtRegistro(DatasUtil.formatarDataTela(conta.getDtRegistro()));
        dto.setEmpresa(conta.getEmpresa());
        dto.setStatus(conta.getStatus().equals("A") ? "Ativo" : "Inativo");
        dto.setTipoConta(conta.getTipoConta().equals("C") ? "Caixa" : "Banco");
        dto.setAgencia(agencia);
        return dto;
    }
}
