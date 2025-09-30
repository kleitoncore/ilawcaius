package com.br.ilawgestao.domains.service;

import com.br.ilawgestao.domains.dto.ConfiguracaoProcessoParadoDto;
import com.br.ilawgestao.domains.dto.PartesDto;
import com.br.ilawgestao.domains.dto.ProcessoDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.ConfiguracaoProcessoParado;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.Partes;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.repository.ConfiguracaoProcessoParadoRepository;
import com.br.ilawgestao.domains.repository.EmpresaRepository;
import com.br.ilawgestao.domains.repository.PartesRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.repository.filtros.FiltroProcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessosParadadosService {

    @Autowired
    private ConfiguracaoProcessoParadoRepository configuracaoProcessoParadoRepository;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PartesRepository partesRepository;

    public ConfiguracaoProcessoParadoDto altrerar(ConfiguracaoProcessoParadoDto conf) {
        ConfiguracaoProcessoParado configuracaoProcessoParado = ConfiguracaoProcessoParado.builder()
                .codigo(conf.getCodigo())
                .empresa(conf.getEmpresa())
                .dias(conf.getDias())
                .build();
        return ConfiguracaoProcessoParadoDto.build(configuracaoProcessoParadoRepository.save(configuracaoProcessoParado));
    }

    public ConfiguracaoProcessoParadoDto consultar(long empresaCodigo) {
        Optional<Empresa> empresa = empresaRepository.findById(empresaCodigo);
        if(!empresa.isPresent()) {
            throw new EntidadeNaoEncontradaException("Empresa não encontrada");
        }
        return ConfiguracaoProcessoParadoDto.build(configuracaoProcessoParadoRepository.findByEmpresaCodigo(empresa.get().getCodigo()).get());
    }

    public List<ProcessoDto> consultarProcessosParados(long empresaCodigo, FiltroProcesso filtro) {
        Optional<Empresa> empresa = empresaRepository.findById(empresaCodigo);
        if(!empresa.isPresent()) {
            throw new EntidadeNaoEncontradaException("Empresa não encontrada");
        }

        Optional<ConfiguracaoProcessoParado> conf = configuracaoProcessoParadoRepository.findByEmpresaCodigo(empresa.get().getCodigo());
        List<Processo> processos = processoRepository.consultarProcessosParados(empresa.get().getCodigo(), conf.get().getDias(), filtro);
        List<ProcessoDto> dtos = new ArrayList<>();
        if(processos != null && processos.size() > 0) {
            for(Processo processo: processos) {
                dtos.add(ProcessoDto.buildShort(processo, this.listarPartes(processo.getCodigo()),null,null));
            }
        }
        return dtos;
    }

    private List<PartesDto> listarPartes(long processo) {
        List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo);
        List<PartesDto> dtos = new ArrayList<>();
        for(Partes parte : partes) {
            dtos.add(PartesDto.build(parte));
        }

        return dtos;
    }
}
