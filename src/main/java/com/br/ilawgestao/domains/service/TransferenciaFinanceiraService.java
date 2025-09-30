package com.br.ilawgestao.domains.service;

import com.br.ilawgestao.domains.dto.ArquivoTransferenciaDto;
import com.br.ilawgestao.domains.dto.ContaFinanceiraDto;
import com.br.ilawgestao.domains.dto.TransferenciaFinanceiraDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.exception.SaldoInsuficienteException;
import com.br.ilawgestao.domains.models.ContaFinanceira;
import com.br.ilawgestao.domains.models.TransferenciaFinanceira;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.ContaFinanceiraRepository;
import com.br.ilawgestao.domains.repository.TransferenciaFinanceiraRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.repository.filtros.FiltroTranferenciaFinanceira;
import com.br.ilawgestao.domains.utils.DatasUtil;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransferenciaFinanceiraService {

    @Autowired
    private TransferenciaFinanceiraRepository transferenciaFinanceiraRepository;

    @Autowired
    private ContaFinanceiraRepository contaFinanceiraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaFinanceiraService contaFinanceiraService;

    private final String DEBITO = "D";
    private final String CREDITO = "C";

    @Transactional
    public TransferenciaFinanceiraDto cadastrarTransferenciaFinanceira(TransferenciaFinanceiraDto trans) {
        //Verificando as contas informadas
        Optional<ContaFinanceira> contaOrigem = contaFinanceiraRepository.findById(trans.getOrigem().getCodigo());
        if(!contaOrigem.isPresent()) {
            throw new EntidadeNaoEncontradaException("Conta de origem não localizada");
        }

        Optional<ContaFinanceira> contaDestino = contaFinanceiraRepository.findById(trans.getDestino().getCodigo());
        if(!contaOrigem.isPresent()) {
            throw new EntidadeNaoEncontradaException("Conta de destino não localizada");
        }

        //Verificando o usuário
        Optional<Usuario> usuario = usuarioRepository.findById(trans.getUsuario().getCodigo());
        if(!usuario.isPresent()) {
            throw new EntidadeNaoEncontradaException("Usuário informado não localizado");
        }

        //Verifica se a conta de origim tem saldo o suficiente para fazer a tranferência solicitada
        if(trans.getValor() > contaOrigem.get().getSaldo()) {
            throw new SaldoInsuficienteException("A conta de origem não possui saldo suficiente para a transferência");
        }

        //Registra Traferencia
        TransferenciaFinanceira transferencia = TransferenciaFinanceira.builder()
                .origem(contaOrigem.get())
                .destino(contaDestino.get())
                .valor(trans.getValor())
                .dtRegistro(DatasUtil.getDataAtual())
                .usuario(usuario.get())
                .observacao(trans.getObservacao())
                .file(trans.getFile())
                .tipoFile(trans.getTipoFile())
                .fileName(trans.getFileName())
                .build();

        TransferenciaFinanceiraDto transferenciaSalva =
                TransferenciaFinanceiraDto.buildConsulta(transferenciaFinanceiraRepository.save(transferencia));

        if(transferenciaSalva != null) {
            //Debitar da Origem
            contaFinanceiraService.debitarCreditarDaConta(ContaFinanceiraDto.build(contaOrigem.get()),trans.getValor(),this.DEBITO);
            //Creditar da Origem
            contaFinanceiraService.debitarCreditarDaConta(ContaFinanceiraDto.build(contaDestino.get()),trans.getValor(), this.CREDITO);
        }

        return transferenciaSalva;
    }

    public List<TransferenciaFinanceiraDto> consultarTransferencias(FiltroTranferenciaFinanceira filtro) {
        List<TransferenciaFinanceira> entitys = transferenciaFinanceiraRepository.consultarTransferencias(filtro);
        List<TransferenciaFinanceiraDto> dtos = new ArrayList<>();
        if(entitys != null && entitys.size() > 0) {
            for(TransferenciaFinanceira trans : entitys) {
                dtos.add(TransferenciaFinanceiraDto.buildConsulta(trans));
            }
        }

        return dtos;
    }

    public ArquivoTransferenciaDto processarArquivo(MultipartFile file) {
        ArquivoTransferenciaDto dto = new ArquivoTransferenciaDto();
        if(file != null) {
            dto.setTipo(file.getContentType());
            dto.setFileName(file.getOriginalFilename());
            try {
                dto.setFile(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dto;
    }
}
