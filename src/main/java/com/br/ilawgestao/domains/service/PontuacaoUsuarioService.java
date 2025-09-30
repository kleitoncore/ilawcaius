package com.br.ilawgestao.domains.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.br.ilawgestao.domains.dto.AtividadesPontosDto;
import com.br.ilawgestao.domains.models.Atividade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.GraficoPontuacaoUsuarioDto;
import com.br.ilawgestao.domains.dto.PontuacaoUsuarioDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.ProjecaoUsuario;
import com.br.ilawgestao.domains.repository.AtividadeRepository;
import com.br.ilawgestao.domains.repository.ProjecaoUsuarioRepository;

@Service
public class PontuacaoUsuarioService {
	
	@Autowired
	AtividadeRepository atividadeRepository;
	
	@Autowired
	private ProjecaoUsuarioRepository projecaoUsuarioRepository;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GraficoPontuacaoUsuarioDto gerarGraficoPontuacaoUsuario(long ano, long mesAtual, long mesAnterior, 
			long usuario, long empresa) {
		//Recupera a pontuação do mês anterior;
		List<PontuacaoUsuarioDto> pontuacaoAnterior = atividadeRepository.pontuacaoUsuario(ano, mesAnterior, usuario, empresa);
		List<PontuacaoUsuarioDto> pontuacaoAnteriorFinal = new ArrayList<PontuacaoUsuarioDto>();
		//Recupera a pontuação do mês atual
		List<PontuacaoUsuarioDto> pontuacaoAtual = atividadeRepository.pontuacaoUsuario(ano, mesAtual, usuario, empresa);
		List<PontuacaoUsuarioDto> pontuacaoAtualFinal = new ArrayList<PontuacaoUsuarioDto>();
		
		//Manipulação mês atual
		LocalDate dataAtual = LocalDate.now();
        int diaDoMes = dataAtual.getDayOfMonth();
		int[] diasAtuais = new int[diaDoMes];
		int pontosTotalAtual = 0;
		for(PontuacaoUsuarioDto pa : pontuacaoAtual) {
			pontosTotalAtual = pontosTotalAtual + pa.getPontos().intValue();
		}
	
		BigDecimal pontosAcumulados = BigDecimal.ZERO;
		for(int j = 0; j < diasAtuais.length; j++) {
			boolean passou = false;
			for(int i = 0; i < pontuacaoAtual.size(); i++) {
				PontuacaoUsuarioDto dto = new PontuacaoUsuarioDto();
				if((j+1) == pontuacaoAtual.get(i).getDia()) {
					if(i == 0) {
						dto.setDia(j+1);
						dto.setPontos(pontuacaoAtual.get(i).getPontos());
						pontosAcumulados = pontosAcumulados.add(pontuacaoAtual.get(i).getPontos());
					} else {
						pontosAcumulados = pontosAcumulados.add(pontuacaoAtual.get(i).getPontos());
						dto.setDia(j+1);
						dto.setPontos(pontosAcumulados);
						
					}
					pontuacaoAtualFinal.add(dto);
				} else {
					if(!passou) {
						dto.setDia(j+1);
						dto.setPontos(new BigDecimal(0));
						pontuacaoAtualFinal.add(dto);
					}
					passou = true;
				}
			}
		}
		
		Set<Integer> diasSet = new HashSet();
		int[]pontuacaoAtualAuxiliar = new int[pontuacaoAtualFinal.size()];    
        for(int i = 0; i < pontuacaoAtualFinal.size(); i++) {
        	int dias = (int)pontuacaoAtualFinal.get(i).getDia();

            // Verifique se o valor de "dias" já está no conjunto
            if (!diasSet.add(dias)) {
            	pontuacaoAtualAuxiliar[i] = dias;
            }
        }
        
        for(int i = 0; i < pontuacaoAtualAuxiliar.length; i++) {
        	for(int j = 0; j < pontuacaoAtualFinal.size(); j++) {
        		if(pontuacaoAtualAuxiliar[i] == pontuacaoAtualFinal.get(j).getDia() && pontuacaoAtualFinal.get(j).getPontos().equals(new BigDecimal(0))) {
        			pontuacaoAtualFinal.remove(j);
        		}
        	}
        }
        
        //Manipulação mês anterior
    	int[] diasAnteriores = new int[31];
		int pontosTotalAnterior = 0;
		for(PontuacaoUsuarioDto pa : pontuacaoAnterior) {
			pontosTotalAnterior = pontosTotalAnterior + pa.getPontos().intValue();
		}
		
		BigDecimal pontosAcumuladosAnterior = BigDecimal.ZERO;
		for(int j = 0; j < diasAnteriores.length; j++) {
			boolean passou = false;
			for(int i = 0; i < pontuacaoAnterior.size(); i++) {
				PontuacaoUsuarioDto dto = new PontuacaoUsuarioDto();
				if((j+1) == pontuacaoAnterior.get(i).getDia()) {
					if(i == 0) {
						dto.setDia(j+1);
						dto.setPontos(pontuacaoAnterior.get(i).getPontos());
						pontosAcumuladosAnterior = pontosAcumuladosAnterior.add(pontuacaoAnterior.get(i).getPontos());
					} else {
						dto.setDia(j+1);
						dto.setPontos(pontosAcumuladosAnterior);
						
					}
					pontuacaoAnteriorFinal.add(dto);
				} else {
					if(!passou) {
						dto.setDia(j+1);
						dto.setPontos(new BigDecimal(0));
						pontuacaoAnteriorFinal.add(dto);
					}
					passou = true;
				}
			}
		}
		
		Set<Integer> diasSetAnterior = new HashSet();
		int[]pontuacaoAnteriorAuxiliar = new int[pontuacaoAnteriorFinal.size()];     
	      for(int i = 0; i < pontuacaoAnteriorFinal.size(); i++) {
	      	int dias = (int)pontuacaoAnteriorFinal.get(i).getDia();
	
	          // Verifique se o valor de "dias" já está no conjunto
	          if (!diasSetAnterior.add(dias)) {
	          	pontuacaoAnteriorAuxiliar[i] = dias;
	          }
	     }
	      
	     for(int i = 0; i < pontuacaoAnteriorAuxiliar.length; i++) {
	      for(int j = 0; j < pontuacaoAnteriorFinal.size(); j++) {
	      	if(pontuacaoAnteriorAuxiliar[i] == pontuacaoAnteriorFinal.get(j).getDia() && pontuacaoAnteriorFinal.get(j).getPontos().equals(new BigDecimal(0))) {
	      		pontuacaoAnteriorFinal.remove(j);
	      	}
	      }
	     }
		
		//Projeção
		LocalDate dataAtualParaProjecao = LocalDate.now();
		// Obter a primeira data do mês atual
		LocalDate primeiraDataDoMes = dataAtualParaProjecao.withDayOfMonth(1);
		// Obter a última data do mês atual
		LocalDate ultimaDataDoMes = dataAtualParaProjecao.withDayOfMonth(dataAtual.lengthOfMonth());
		// Formatar as datas
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String primeiraDataFormatada = primeiraDataDoMes.format(formatter);
		String ultimaDataFormatada = ultimaDataDoMes.format(formatter);

		List<AtividadesPontosDto> atividadesProjecao = atividadeRepository.consultarAtividadesPorUsuarioResponsavel(usuario,empresa,
				primeiraDataFormatada,ultimaDataFormatada);
		int[] diasProjecao = new int[31]; // Crie um array de 30 elementos
		int valorInicial = 1;
        int valorFinal = 0;
		if(atividadesProjecao != null && atividadesProjecao.size() > 0) {
			for(AtividadesPontosDto pont : atividadesProjecao) {
				valorFinal = valorFinal + pont.getPontos();
			}
		}
        int incremento = (valorFinal / diasProjecao.length);
		if(incremento == 0) {
			incremento = 1;
		}
		for (int i = 0; i < diasProjecao.length; i++) {
			diasProjecao[i] = (valorInicial + i) * incremento;
	    }
		
		Arrays.sort(diasProjecao);
		List<PontuacaoProjecaoDto> projecao = new ArrayList<PontuacaoProjecaoDto>();
		for (int i = 0; i < diasProjecao.length; i++) {
			PontuacaoProjecaoDto pp = new PontuacaoProjecaoDto();
			pp.setDias(i+1);
			pp.setPontos(diasProjecao[i]);
			projecao.add(pp);
		}
		
		GraficoPontuacaoUsuarioDto dto = new GraficoPontuacaoUsuarioDto();
		dto.setProjecao(projecao);
		dto.setMesAnterior(pontuacaoAnteriorFinal);
		dto.setMesAtual(pontuacaoAtualFinal);
		
		return dto;
	}
}
