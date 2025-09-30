package com.br.ilawgestao.domains.service;

import com.br.ilawgestao.domains.dto.TransacaoFinanceiraDto;
import com.br.ilawgestao.domains.dto.TransferenciaFinanceiraDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.exception.SaldoInsuficienteException;
import com.br.ilawgestao.domains.models.ContaFinanceira;
import com.br.ilawgestao.domains.models.Lancamento;
import com.br.ilawgestao.domains.models.TransacaoFinanceira;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.ContaFinanceiraRepository;
import com.br.ilawgestao.domains.repository.LancamentoRepostory;
import com.br.ilawgestao.domains.repository.TransacaoFinanceiraRepoository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class TransacaoFinanceiraService {

    @Autowired
    private TransacaoFinanceiraRepoository transacaoFinanceiraRepoository;

    @Autowired
    private LancamentoRepostory lancamentoRepostory;

    @Autowired
    private ContaFinanceiraRepository contaFinanceiraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void incluirTransacao(TransacaoFinanceiraDto transacao) {
        Optional<Lancamento> lancamento = lancamentoRepostory.findById(transacao.getLancamento().getCodigo());
        if(!lancamento.isPresent()) {
            throw new EntidadeNaoEncontradaException("Lançamento financeiro não localizado");
        }

        Optional<ContaFinanceira> conta = contaFinanceiraRepository.findById(transacao.getContaFinanceira().getCodigo());
        if(!conta.isPresent()) {
            throw new EntidadeNaoEncontradaException("Conta financeira não localizada");
        }

        Optional<Usuario> usuario = usuarioRepository.findById(transacao.getUsuario().getCodigo());
        if(!usuario.isPresent()) {
            throw new EntidadeNaoEncontradaException("Usuário informado não encontrado");
        }

        //Inclui registro na Transação
        TransacaoFinanceira trans = TransacaoFinanceira.builder()
                .lancamento(lancamento.get())
                .contaFinanceira(conta.get())
                .usuario(usuario.get())
                .dtRegistro(DatasUtil.getDataAtual())
                .observacao(transacao.getObservacao())
                .build();

        TransacaoFinanceira transacaoSalva = transacaoFinanceiraRepoository.save(trans);

        if(transacaoSalva != null) {
            double saldo = 0;
            if(lancamento.get().getTipo().getTipo().equals("D")) {
                //Lancamento de despesa, debita da conta selecionada para o pagamento
                saldo = (conta.get().getSaldo() - lancamento.get().getVlPago());
            } else {
                //Lançamento de Receita, credita na conta selecionada
                saldo = (conta.get().getSaldo() + lancamento.get().getVlPago());
            }
            ContaFinanceira contaAlterar = ContaFinanceira.builder()
                    .codigo(conta.get().getCodigo())
                    .nome(conta.get().getNome())
                    .saldo(saldo)
                    .tipoConta(conta.get().getTipoConta())
                    .dtRegistro(conta.get().getDtRegistro())
                    .empresa(conta.get().getEmpresa())
                    .status(conta.get().getStatus())
                    .build();
            contaFinanceiraRepository.save(contaAlterar);
        }
    }

    public TransacaoFinanceiraDto consultarPorLancamento(long lancamento) {
        Optional<TransacaoFinanceira> transacao = transacaoFinanceiraRepoository.findByLancamentoCodigo(lancamento);
        TransacaoFinanceiraDto dto = null;
        if(transacao.isPresent()) {
            dto = TransacaoFinanceiraDto.buildConsulta(transacao.get());
        }

        return dto;
    }

    public void excluirTransacaoFinanceira(long codigo) {
        transacaoFinanceiraRepoository.deleteById(codigo);
    }
}
