package com.br.ilawgestao.domains.service;

import com.br.ilawgestao.domains.dto.AgenciaDto;
import com.br.ilawgestao.domains.dto.ContaFinanceiraDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Agencia;
import com.br.ilawgestao.domains.models.ContaFinanceira;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.repository.AgenciaRepository;
import com.br.ilawgestao.domains.repository.ContaFinanceiraRepository;
import com.br.ilawgestao.domains.repository.EmpresaRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContaFinanceiraService {

    @Autowired
    private ContaFinanceiraRepository contaFinanceiraRepository;

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public ContaFinanceiraDto cadastrar(ContaFinanceiraDto dto) {
        Optional<Empresa> empresa = empresaRepository.findById(dto.getEmpresa().getCodigo());
        if(!empresa.isPresent()) {
            throw new EntidadeNaoEncontradaException("Empresa informada não encontrada");
        }

        ContaFinanceira conta = ContaFinanceira.builder()
                .nome(dto.getNome())
                .tipoConta(dto.getTipoConta())
                .saldo(dto.getSaldo())
                .status("A")
                .dtRegistro(DatasUtil.getDataAtual())
                .empresa(dto.getEmpresa())
                .build();

        ContaFinanceira contaSalva = contaFinanceiraRepository.save(conta);
        ContaFinanceiraDto contaFinanceiraDto = ContaFinanceiraDto.buildConsulta(contaSalva,null);
        if(contaSalva != null && contaSalva.getTipoConta().equals("B")) {
            Agencia agencia = Agencia.builder()
                    .codigoBanco(dto.getAgencia().getCodigoBanco())
                    .numeroAgencia(dto.getAgencia().getNumeroAgencia())
                    .numeroConta(dto.getAgencia().getNumeroConta())
                    .tipoConta(dto.getAgencia().getTipoConta())
                    .contaFinanceira(contaSalva)
                    .build();

            AgenciaDto agenciaSalva = AgenciaDto.build(agenciaRepository.save(agencia));
            contaFinanceiraDto.setAgencia(agenciaSalva);
        }

        return contaFinanceiraDto;
    }

    public List<ContaFinanceiraDto> listar(long empresa) {
        Optional<Empresa> empresaConsulta = empresaRepository.findById(empresa);
        if(!empresaConsulta.isPresent()) {
            throw new EntidadeNaoEncontradaException("Empresa informada não encontrada");
        }

        List<ContaFinanceira> contas = contaFinanceiraRepository.findByEmpresaCodigo(empresaConsulta.get().getCodigo());
        List<ContaFinanceiraDto> dtos = new ArrayList<ContaFinanceiraDto>();
        if(contas != null && contas.size() > 0) {
            for(ContaFinanceira conta : contas) {
                AgenciaDto agenciaDto = null;
                if(conta.getTipoConta().equals("B")) {
                    Optional<Agencia> agencia = agenciaRepository.findByContaFinanceiraCodigo(conta.getCodigo());
                    if(agencia.isPresent()) {
                        agenciaDto = AgenciaDto.build(agencia.get());
                    }
                }

                dtos.add(ContaFinanceiraDto.buildConsulta(conta,agenciaDto));
            }
        }

        return dtos;
    }

    public List<ContaFinanceiraDto> listarAtivos(long empresa) {
        Optional<Empresa> empresaConsulta = empresaRepository.findById(empresa);
        if(!empresaConsulta.isPresent()) {
            throw new EntidadeNaoEncontradaException("Empresa informada não encontrada");
        }

        List<ContaFinanceira> contas = contaFinanceiraRepository.findByEmpresaCodigoAndStatus(empresa,"A");
        List<ContaFinanceiraDto> dtos = new ArrayList<ContaFinanceiraDto>();
        if(contas != null && contas.size() > 0) {
            for(ContaFinanceira conta : contas) {
                AgenciaDto agenciaDto = null;
                if(conta.getTipoConta().equals("B")) {
                    Optional<Agencia> agencia = agenciaRepository.findByContaFinanceiraCodigo(conta.getCodigo());
                    if(agencia.isPresent()) {
                        agenciaDto = AgenciaDto.build(agencia.get());
                    }
                }

                dtos.add(ContaFinanceiraDto.buildConsulta(conta,agenciaDto));
            }
        }

        return dtos;
    }

    public ContaFinanceiraDto alterar(ContaFinanceiraDto dto) {
        Optional<ContaFinanceira> contaEntity = contaFinanceiraRepository.findById(dto.getCodigo());
        if(!contaEntity.isPresent()) {
            throw new EntidadeNaoEncontradaException("Conta financeira não localizada");
        }

        ContaFinanceira conta = ContaFinanceira.builder()
                .codigo(dto.getCodigo())
                .nome(dto.getNome())
                .tipoConta(contaEntity.get().getTipoConta())
                .saldo(dto.getSaldo())
                .status(dto.getStatus())
                .dtRegistro(contaEntity.get().getDtRegistro())
                .empresa(dto.getEmpresa())
                .build();

        ContaFinanceira contaAlterada = contaFinanceiraRepository.save(conta);
        ContaFinanceiraDto contaFinanceiraDto = ContaFinanceiraDto.buildConsulta(conta,null);
        if(contaAlterada != null) {
            if(contaEntity.get().getTipoConta().equals("B")) {
                //É um banco, pode alterar a Agência se for necessário
                Optional<Agencia> agencia = agenciaRepository.findByContaFinanceiraCodigo(dto.getCodigo());
                if(agencia.isPresent()) {
                    Agencia ag = Agencia.builder()
                            .codigo(agencia.get().getCodigo())
                            .codigoBanco(dto.getAgencia().getCodigoBanco())
                            .numeroAgencia(dto.getAgencia().getNumeroAgencia())
                            .numeroConta(dto.getAgencia().getNumeroConta())
                            .tipoConta(dto.getAgencia().getTipoConta())
                            .contaFinanceira(agencia.get().getContaFinanceira())
                            .build();

                    AgenciaDto agenciaSalva = AgenciaDto.build(agenciaRepository.save(ag));
                    contaFinanceiraDto.setAgencia(agenciaSalva);
                }
            }
        }

        return contaFinanceiraDto;
    }

    public void excluirContaFinanceira(long codigo) {
        Optional<ContaFinanceira> contaEntity = contaFinanceiraRepository.findById(codigo);
        if(!contaEntity.isPresent()) {
            throw new EntidadeNaoEncontradaException("Conta financeira não localizada");
        }

        if(contaEntity.get().getTipoConta().equals("B")) {
            Optional<Agencia> agencia = agenciaRepository.findByContaFinanceiraCodigo(contaEntity.get().getCodigo());
            if(agencia.isPresent()) {
                agenciaRepository.deleteById(agencia.get().getCodigo());
            }
        }

        contaFinanceiraRepository.deleteById(codigo);
    }

    public ContaFinanceiraDto consultar(long codigo) {
        Optional<ContaFinanceira> contaEntity = contaFinanceiraRepository.findById(codigo);
        if(!contaEntity.isPresent()) {
            throw new EntidadeNaoEncontradaException("Conta financeira não localizada");
        }

        ContaFinanceiraDto dto = ContaFinanceiraDto.buildConsulta(contaEntity.get(),null);
        if(contaEntity.get().getTipoConta().equals("B")) {
            Optional<Agencia> agencia = agenciaRepository.findByContaFinanceiraCodigo(contaEntity.get().getCodigo());
            if(agencia.isPresent()) {
                AgenciaDto agenciaDto = AgenciaDto.build(agencia.get());
                dto.setAgencia(agenciaDto);
            }
        }

        return dto;
    }

    public void debitarCreditarDaConta(ContaFinanceiraDto conta, double valor, String tipo) {
        Optional<ContaFinanceira> contaEntity = contaFinanceiraRepository.findById(conta.getCodigo());
        if(!contaEntity.isPresent()) {
            throw new EntidadeNaoEncontradaException("Conta financeira não encontrada");
        }

        ContaFinanceiraDto contaFinanceira = ContaFinanceiraDto.build(contaEntity.get());
        BeanUtils.copyProperties(conta, contaFinanceira,"codigo","nome","dtRegistro","empresa","status","tipoConta");
        double novoSaldo = 0;
        if(tipo.equals("D")) {
            novoSaldo = (contaFinanceira.getSaldo() - valor);
        } else {
            novoSaldo = (contaFinanceira.getSaldo() + valor);
        }
        contaFinanceira.setSaldo(novoSaldo);
        contaFinanceiraRepository.save(ContaFinanceiraDto.build(contaFinanceira));
    }
}
