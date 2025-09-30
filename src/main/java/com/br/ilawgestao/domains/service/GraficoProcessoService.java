package com.br.ilawgestao.domains.service;

import java.awt.Color;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.models.*;
import com.br.ilawgestao.domains.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;

@Service
public class GraficoProcessoService {
	
	@Autowired
	private ProcessoRepository graficoProcessoRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private ProcessoArquivadoRepository processoArquivadoRepository;

	@Autowired
	private PartesRepository partesRepository;

	@Autowired
	private ProcessoExcluidoRepository processoExcluidoRepository;
	
	public List<GraficoProcessoDto> graficoProcessoStatusProcessual(long empresa) {
		Optional<Empresa> consultaEmpresa = empresaRepository.findById(empresa);
		if(!consultaEmpresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		List<GraficoProcessoDto> statusProcessuais = graficoProcessoRepository.graficoStatus(empresa);
		List<GraficoProcessoDto> lista = new ArrayList<GraficoProcessoDto>();
		if(statusProcessuais != null) {
			for(GraficoProcessoDto graf : statusProcessuais) {
				GraficoProcessoDto grafico = new GraficoProcessoDto();
				grafico.setNome(graf.getNome());
				grafico.setQuantidade(graf.getQuantidade());
				grafico.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				lista.add(grafico);
			}
		}
		
		return lista;
	}
	
	public List<GraficoProcessoDto> graficoProcessoGrupoTrabalho(long empresa) {
		Optional<Empresa> consultaEmpresa = empresaRepository.findById(empresa);
		if(!consultaEmpresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		List<GraficoProcessoDto> grupos = graficoProcessoRepository.graficoGrupoTrabalho(empresa);
		List<GraficoProcessoDto> lista = new ArrayList<GraficoProcessoDto>();
		
		String outros = "";
		int contador = 0;
		for(GraficoProcessoDto out : grupos) {
			if(contador < grupos.size() -1) {
				outros = outros + "'" + out.getNome() + "'" + ",";
			} else {
				outros = outros + "'" + out.getNome() + "'";
			}
			contador++;
		}
		
		BigInteger gruposOutros = BigInteger.valueOf(graficoProcessoRepository.graficoGrupoTrabalhoOutros(outros, empresa));
		GraficoProcessoDto graficoOutros = new GraficoProcessoDto();
		graficoOutros.setNome("Outros");
		graficoOutros.setQuantidade(gruposOutros);
		graficoOutros.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
		lista.add(graficoOutros);
		
		if(grupos != null) {
			for(GraficoProcessoDto graf : grupos) {
				GraficoProcessoDto grafico = new GraficoProcessoDto();
				grafico.setNome(graf.getNome());
				grafico.setQuantidade(graf.getQuantidade());
				grafico.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				lista.add(grafico);
			}
		}
		
		return lista;
	}
	
	public List<GraficoProcessoDto> graficoProcessoAreaAtuacao(long empresa) {
		Optional<Empresa> consultaEmpresa = empresaRepository.findById(empresa);
		if(!consultaEmpresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		List<GraficoProcessoDto> areas = graficoProcessoRepository.graficoAreaAtuacao(empresa);
		List<GraficoProcessoDto> lista = new ArrayList<GraficoProcessoDto>();
		
		if(areas != null) {
			for(GraficoProcessoDto graf : areas) {
				GraficoProcessoDto grafico = new GraficoProcessoDto();
				grafico.setNome(graf.getNome());
				grafico.setQuantidade(graf.getQuantidade());
				grafico.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				lista.add(grafico);
			}
		}
		
		return lista;
	}
	
	public List<GraficoProcessoDto> graficoProcessoTipoAcao(long empresa) {
		Optional<Empresa> consultaEmpresa = empresaRepository.findById(empresa);
		if(!consultaEmpresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		List<GraficoProcessoDto> tipos = graficoProcessoRepository.graficoTipoAcao(empresa);
		List<GraficoProcessoDto> lista = new ArrayList<GraficoProcessoDto>();
		
		if(tipos != null) {
			for(GraficoProcessoDto graf : tipos) {
				GraficoProcessoDto grafico = new GraficoProcessoDto();
				grafico.setNome(graf.getNome());
				grafico.setQuantidade(graf.getQuantidade());
				grafico.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				lista.add(grafico);
			}
		}
		
		return lista;
	}
	
	public List<GraficoProcessoDto> graficoProcessoObjetosAcao(long empresa) {
		Optional<Empresa> consultaEmpresa = empresaRepository.findById(empresa);
		if(!consultaEmpresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		List<GraficoProcessoDto> objetos = graficoProcessoRepository.graficoObjetos(empresa);
		List<GraficoProcessoDto> lista = new ArrayList<GraficoProcessoDto>();
		
		if(objetos != null) {
			for(GraficoProcessoDto graf : objetos) {
				GraficoProcessoDto grafico = new GraficoProcessoDto();
				grafico.setNome(graf.getNome());
				grafico.setQuantidade(graf.getQuantidade());
				grafico.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				lista.add(grafico);
			}
		}
		
		return lista;
	}
	
	public List<GraficoProcessoMesDto> graficoProcessoMes(long empresa, String dataInicial, String dataFinal) {
		return graficoProcessoRepository.graficoProcessoMes(empresa, dataInicial, dataFinal);
	}
	
	public List<ProcessoArquivadoDto> graficoProcessoArquivado(long empresa, String dataInicial, String dataFinal) {
		return processoArquivadoRepository.processosArquivadosMes(empresa, dataInicial, dataFinal);
	}

	public List<ProcessoDto> consultarProcessosCadastradosEsteMes(long empresa) {
		List<Processo> processos = graficoProcessoRepository.consultarProcessosCadastradosEsseMes(empresa);
		List<ProcessoDto> dtos = new ArrayList<>();
		if(processos != null && processos.size() > 0) {
			for(Processo processo : processos) {
				dtos.add(ProcessoDto.buildShort(processo, listarPartes(processo.getCodigo()),null,null));
			}
		}
		return dtos;
	}

	public List<ProcessoDto> consultarProcessosArquivados(long empresa) {
		List<ProcessoArquivado> processos = processoArquivadoRepository.consultarProcessosArquivados(empresa);
		List<ProcessoDto> dtos = new ArrayList<>();
		if(processos != null && processos.size() > 0) {
			for(ProcessoArquivado processo : processos) {
				dtos.add(ProcessoDto.buildArquivamento(processo.getProcesso(),listarPartes(processo.getProcesso().getCodigo()),
						processo.getDataArquivado()));
			}
		}

		return dtos;
	}

	public List<ProcessoDto> consultarProcessosLixeira(long empresa) {
		List<ProcessoExcluido> processos = processoExcluidoRepository.findByProcessoEmpresaCodigoOrderByDataExcluidoDesc(empresa);
		List<ProcessoDto> dtos = new ArrayList<>();
		if(processos != null && processos.size() > 0) {
			for(ProcessoExcluido processo : processos) {
				dtos.add(ProcessoDto.buildLixeira(processo.getProcesso(), listarPartes(processo.getProcesso().getCodigo()),processo.getDataExcluido()));
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
	
	private Color gerarCorAleatoriamente(){  
       Random randColor = new Random();   
       int r = randColor.nextInt(256);  
       int g = randColor.nextInt(256);  
       int b = randColor.nextInt(256);  
       return new Color(r, g, b);  
	}
			
	private String gerarCorHexadecimal(Color color){  
       return '#'+  
        this.tratarHexString(Integer.toHexString(color.getRed()))+  
        this.tratarHexString(Integer.toHexString(color.getGreen()))+  
        this.tratarHexString(Integer.toHexString(color.getBlue()));  
	}  
					
	private String tratarHexString(String hexString){  
      String hex = null;  
       if(hexString.length() == 1){  
            hex = '0'+hexString;  
       }else{  
            hex = hexString;  
       }  
       return hex;  
	}  
}
