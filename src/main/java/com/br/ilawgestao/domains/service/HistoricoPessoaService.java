package com.br.ilawgestao.domains.service;

import com.br.ilawgestao.domains.dto.HistoricoPessoaDto;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.HistoricoPessoa;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.HistoricoPessoaRepository;
import com.br.ilawgestao.domains.repository.PessoaRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HistoricoPessoaService {
    @Autowired
    private HistoricoPessoaRepository historicoPessoaRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<HistoricoPessoaDto> consultarHistoricoPessoa(String dataInicial, String dataFinal) {
        List<HistoricoPessoa> historicos = historicoPessoaRepository.consultarHistoricoPessoa(dataInicial,dataFinal);
        List<HistoricoPessoaDto> dtos = new ArrayList<>();
        if(historicos != null && historicos.size() > 0) {
            for (HistoricoPessoa hist : historicos) {
                dtos.add(HistoricoPessoaDto.build(hist));
            }
        }
        return dtos;
    }

    public List<HistoricoPessoaDto> listarTodos(long codigoPessoa) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(codigoPessoa);
        if(!pessoa.isPresent()) {
            throw new EntidadeNaoEncontradaException("Pessoa não encontrada");
        }

        List<HistoricoPessoa> historicos = historicoPessoaRepository.findByPessoaCodigoOrderByDataHistoricoDesc(codigoPessoa);
        List<HistoricoPessoaDto> dtos = new ArrayList<>();
        if(historicos != null && historicos.size() > 0) {
            for(HistoricoPessoa hist : historicos) {
                dtos.add(HistoricoPessoaDto.buildConsulta(hist));
            }
        }
        return dtos;
    }

    public HistoricoPessoaDto cadastrarHistorico(HistoricoPessoaDto dto) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(dto.getPessoa().getCodigo());
        if(!pessoa.isPresent()) {
            throw new EntidadeNaoEncontradaException("Pessoa não encontrada");
        }

        Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuario().getCodigo());
        if(!usuario.isPresent()) {
            throw new EntidadeNaoEncontradaException("Usuário não encontrado");
        }

        HistoricoPessoa historico = HistoricoPessoa.builder()
                .pessoa(pessoa.get())
                .dsHistorico(dto.getDsHistorico())
                .dataHistorico(DatasUtil.getDataAtual())
                .usuario(usuario.get())
                .tipo("M")
                .build();

        HistoricoPessoaDto historicoCadastrado = HistoricoPessoaDto.buildConsulta(historicoPessoaRepository.save(historico));
        return historicoCadastrado;
    }

    public void excluirHistorico(long codigo) {
        Optional<HistoricoPessoa> historico = historicoPessoaRepository.findById(codigo);
        if(!historico.isPresent()) {
            throw new EntidadeNaoEncontradaException("Histórico de pessoa não encontrado");
        }

        if(!historico.get().getTipo().equals("M")) {
            throw new EntidadeEmUsoException("Este tipo de histórico não pode ser excluído");
        }

        historicoPessoaRepository.deleteById(codigo);
    }
}
