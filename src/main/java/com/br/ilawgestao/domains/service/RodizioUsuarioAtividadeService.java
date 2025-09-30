package com.br.ilawgestao.domains.service;

import com.br.ilawgestao.domains.RodizioUsuarioAtividade;
import com.br.ilawgestao.domains.dto.CadastroRodizioDto;
import com.br.ilawgestao.domains.dto.RodizioUsuarioAtividadeDto;
import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.repository.RodizioUsuarioAtividadeRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RodizioUsuarioAtividadeService {

    @Autowired
    private RodizioUsuarioAtividadeRepository rodizioUsuarioAtividadeRepository;

    public List<RodizioUsuarioAtividadeDto> cadastrarRodizio(CadastroRodizioDto rodizio) {
        List<RodizioUsuarioAtividadeDto> rodiziosCadastrado = new ArrayList<>();
        if(rodizio.getRodizio() == null && rodizio.getRodizio().size() == 0) {
            throw new EntidadeNaoEncontradaException("Lista de rodízio com usuários vazia");
        }

        int posicao = 1;
        for(RodizioUsuarioAtividadeDto rod : rodizio.getRodizio()) {
            RodizioUsuarioAtividade rodi = RodizioUsuarioAtividade.builder()
                    .grupoAtividade(rod.getGrupoAtividade())
                    .atividade(rod.getAtividade())
                    .grupoTrabalho(rod.getGrupoTrabalho())
                    .usuarioAtividade(rod.getUsuarioAtividade())
                    .posicao(posicao)
                    .dataAlteracao(DatasUtil.getDataAtual())
                    .build();
            RodizioUsuarioAtividadeDto rodizioCadastrado = RodizioUsuarioAtividadeDto.buildConsulta(rodizioUsuarioAtividadeRepository.save(rodi));
            if(rodizioCadastrado != null) {
                posicao++;
                rodiziosCadastrado.add(rodizioCadastrado);
            }
        }

        return rodiziosCadastrado;
    }

    public List<RodizioUsuarioAtividadeDto> consultarRodizioPorGrupoAtividade(long grupoAtividade, long atividade) {
        List<RodizioUsuarioAtividadeDto> dtos = new ArrayList<>();
        List<RodizioUsuarioAtividade> entitys = rodizioUsuarioAtividadeRepository.findByGrupoAtividadeCodigoAndAtividadeCodigoOrderByPosicao(
                grupoAtividade,atividade);
        if(entitys != null && entitys.size() > 0) {
            for(RodizioUsuarioAtividade rod : entitys) {
                dtos.add(RodizioUsuarioAtividadeDto.buildConsulta(rod));
            }
        }

        return dtos;
    }

    public List<RodizioUsuarioAtividadeDto> incluirUsuarioRodizioExistente(CadastroRodizioDto dto) {
        List<RodizioUsuarioAtividadeDto> novosUsuarios = new ArrayList<>();
        //Pegar a última posição
        Optional<RodizioUsuarioAtividade> rodizioPosicao = rodizioUsuarioAtividadeRepository.encontrarRodizioComMaiorPosicao(dto.getGrupoAtividade().getCodigo(),
                dto.getAtividade().getCodigo());
        long posicao = rodizioPosicao.get().getPosicao() + 1;
        for(RodizioUsuarioAtividadeDto rod: dto.getRodizio()) {
            Optional<RodizioUsuarioAtividade> rodizio = rodizioUsuarioAtividadeRepository.
                    findByGrupoAtividadeCodigoAndGrupoTrabalhoCodigoAndAtividadeCodigoAndUsuarioAtividadeCodigo(rod.getGrupoAtividade().getCodigo(),
                            rod.getGrupoTrabalho().getCodigo(),rod.getAtividade().getCodigo(),rod.getUsuarioAtividade().getCodigo());
            if(!rodizio.isPresent()) {
                RodizioUsuarioAtividade rodizioAtividade = RodizioUsuarioAtividade.builder()
                        .grupoAtividade(rod.getGrupoAtividade())
                        .atividade(rod.getAtividade())
                        .grupoTrabalho(rod.getGrupoTrabalho())
                        .usuarioAtividade(rod.getUsuarioAtividade())
                        .dataAlteracao(DatasUtil.getDataAtual())
                        .posicao(posicao)
                        .build();
                RodizioUsuarioAtividadeDto rodizioCadastrado = RodizioUsuarioAtividadeDto.buildConsulta(
                        rodizioUsuarioAtividadeRepository.save(rodizioAtividade)
                );
                if(rodizioCadastrado != null) {
                    posicao++;
                    novosUsuarios.add(rodizioCadastrado);
                }
            }
        }

        return novosUsuarios;
    }

    public void excluirUsuario(long codigo) {
        Optional<RodizioUsuarioAtividade> rodizio = rodizioUsuarioAtividadeRepository.findById(codigo);
        rodizioUsuarioAtividadeRepository.deleteById(codigo);
        this.reorganizarRodizio(rodizio.get().getGrupoAtividade().getCodigo(), rodizio.get().getAtividade().getCodigo());
    }

    protected void reorganizarRodizio(long grupoAtividade, long atividade) {
        List<RodizioUsuarioAtividade> entitys = rodizioUsuarioAtividadeRepository.findByGrupoAtividadeCodigoAndAtividadeCodigoOrderByPosicao(grupoAtividade,
                atividade);
        for(RodizioUsuarioAtividade rod : entitys) {
            rodizioUsuarioAtividadeRepository.deleteById(rod.getCodigo());
        }
        if(entitys != null && entitys.size() > 0) {
            for (int i = 0; i < entitys.size(); i++) {
                RodizioUsuarioAtividade rodi = RodizioUsuarioAtividade.builder()
                        .grupoAtividade(entitys.get(i).getGrupoAtividade())
                        .atividade(entitys.get(i).getAtividade())
                        .grupoTrabalho(entitys.get(i).getGrupoTrabalho())
                        .usuarioAtividade(entitys.get(i).getUsuarioAtividade())
                        .posicao(i + 1)
                        .dataAlteracao(DatasUtil.getDataAtual())
                        .build();
                RodizioUsuarioAtividadeDto rodizioCadastrado = RodizioUsuarioAtividadeDto.buildConsulta(rodizioUsuarioAtividadeRepository.save(rodi));
            }
        }
    }

    public RodizioUsuarioAtividadeDto consultarRodizio(long grupoAtividade, long grupoTrabalho, long atividade, long usuario) {
        Optional<RodizioUsuarioAtividade> rodizio = rodizioUsuarioAtividadeRepository.findByGrupoAtividadeCodigoAndGrupoTrabalhoCodigoAndAtividadeCodigoAndUsuarioAtividadeCodigo(
                grupoAtividade,grupoTrabalho,atividade,usuario);
        if(rodizio.isPresent()) {
            throw new EntidadeEmUsoException("Atenção! O usuário " + rodizio.get().getUsuarioAtividade().getNome() + " já está cadastrado nesta configuração");
        }

        return null;
    }
}