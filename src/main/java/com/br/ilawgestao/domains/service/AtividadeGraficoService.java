package com.br.ilawgestao.domains.service;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.HistoricoAtividade;
import com.br.ilawgestao.domains.models.TituloAtividade;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.HistoricoAtividadeRepository;
import com.br.ilawgestao.domains.repository.TituloAtividadeRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.repository.AtividadeRepository;

@Service
public class AtividadeGraficoService {
	
	@Autowired
	private AtividadeRepository atividadeRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TituloAtividadeRepository tituloAtividadeRepository;

	@Autowired
	private HistoricoAtividadeRepository historicoAtividadeRepository;
	
	public List<GraficoAtividadeDto> graficoAtividade(long empresa, String dataInicial, String dataFinal) {
		return atividadeRepository.graficoAtividades(empresa, dataInicial, dataFinal);
	}
	
	public List<GraficoAtividadeDto> graficoAtividadeDataLimite(long empresa, String dataInicial, String dataFinal) {
		return atividadeRepository.graficoAtividadesDataLimite(empresa, dataInicial, dataFinal);
	}
	
	public List<GraficoAtividadeDto> graficoAtividadeConcluido(long empresa, String dataInicial, String dataFinal) {
		return atividadeRepository.graficoAtividadesConcluidos(empresa, dataInicial, dataFinal);
	}
		
	public List<GraficoAtividadesStatusDto> graficoAtividadesStatus(long empresa, String dataInicial, String dataFinal) {
		return atividadeRepository.graficoAtividadesStatus(empresa, dataInicial, dataFinal);
	}
		
	public List<GraficoAtividadeSituacaoDto> graficoAtividadesSituacao(long empresa, String dataInicial, String dataFinal) {
		return atividadeRepository.graficoAtividadesSituacao(empresa, dataInicial, dataFinal);
	}
		
	public List<GraficoAtividadesStatusDto> graficoAtividadeStatusUsuarioPainel(long usuario) {
		return this.atividadeRepository.graficoAtividadesStatusUsuarioPainel(usuario);
	}
	
	public List<GraficoAtividadeGrupoDto> graficoAtividadeGrupo(long empresa, String dataInicial, String dataFinal) {
		List<GraficoAtividadeGrupoDto> grafico = atividadeRepository.graficoAtividadeGrupo(empresa, dataInicial, dataFinal);
		List<GraficoAtividadeGrupoDto> lista = new ArrayList<GraficoAtividadeGrupoDto>();
		
		if(grafico != null) {
			int contador = 0;
			String outros = "";
			for(GraficoAtividadeGrupoDto graf: grafico) {
				if(contador < grafico.size() -1) {
					outros = outros + "'" + graf.getGrupo() + "'" + ",";
				} else {
					outros = outros + "'" + graf.getGrupo() + "'";
				}
				
				contador++;
			}
			
			if(outros != "") {
				BigInteger quantidadeOutros = BigInteger.valueOf(atividadeRepository.graficoAtividadeGruposOutros(empresa, outros, dataInicial, dataFinal));
				GraficoAtividadeGrupoDto graficoOutro = new GraficoAtividadeGrupoDto();
				graficoOutro.setGrupo("Outros");
				graficoOutro.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				graficoOutro.setTotal(quantidadeOutros);
				lista.add(graficoOutro);
			}
			
			for(GraficoAtividadeGrupoDto graf: grafico) {
				GraficoAtividadeGrupoDto grafi = new GraficoAtividadeGrupoDto();
				grafi.setGrupo(graf.getGrupo());
				grafi.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				grafi.setTotal(graf.getTotal());
				lista.add(grafi);
			}
		}
		
		return lista;
	}
	
	public List<GraficoAtividadeFaseDto> graficoAtividadesFase(long empresa, String dataInicial, String dataFinal) {
		List<GraficoAtividadeFaseDto> entitys = atividadeRepository.graficoAtividadeFase(empresa, dataInicial, dataFinal);
		List<GraficoAtividadeFaseDto> dtos = new ArrayList<GraficoAtividadeFaseDto>();
		if(entitys != null && entitys.size() > 0) {
			for(GraficoAtividadeFaseDto graf : entitys) {
				GraficoAtividadeFaseDto dto = new GraficoAtividadeFaseDto();
				dto.setFase(graf.getFase());
				dto.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				dto.setTotal(graf.getTotal());
				dtos.add(dto);
			}
		}
		
		return dtos;
	}

	public List<GraficoFaseProcessoAtividadeDTO> graficoFaseProcessoAtividade(long empresa, String dataInicial, String dataFinal) {
		List<GraficoFaseProcessoAtividadeDTO> entitys = atividadeRepository.graficoProcessoFaseAtividade(empresa,dataInicial,dataFinal);
		List<GraficoFaseProcessoAtividadeDTO> dtos = new ArrayList<GraficoFaseProcessoAtividadeDTO>();
		if(entitys != null && entitys.size() > 0) {
			for(GraficoFaseProcessoAtividadeDTO graf : entitys) {
				GraficoFaseProcessoAtividadeDTO dto = new GraficoFaseProcessoAtividadeDTO();
				dto.setFase(graf.getFase());
				dto.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				dto.setAtividades(graf.getAtividades());
				dtos.add(dto);
			}
		}

		return dtos;
	}

	public GraficoPontuacaoTotalDto graficoPontuacaoTotal(long empresa, String dataInicial, String dataFinal) {
		List<GraficoPontuacaoTotalDto> entitys = atividadeRepository.graficoPontuacaoTotal(empresa,dataInicial,dataFinal);
		GraficoPontuacaoTotalDto dto = new GraficoPontuacaoTotalDto();
		double percentual = 0;
		int porcentagemArredondada = 0;
		BigDecimal pontosTotal = BigDecimal.ZERO;
		BigDecimal pontos = BigDecimal.ZERO;
		if(entitys != null && entitys.size() > 0) {
			for(GraficoPontuacaoTotalDto graf : entitys) {
				if(graf.getPontosTotal() != null) {
					pontosTotal = graf.getPontosTotal();
				}
				if(graf.getPontos() != null) {
					pontos = graf.getPontos();
				}
			}

			// Convertendo os BigIntegers em BigDecimal
			//BigDecimal pontosDecimal = new BigDecimal(pontos);
			//BigDecimal pontosTotalDecimal = new BigDecimal(pontosTotal);

			// Realizando a divisão com BigDecimal
			BigDecimal resultado = BigDecimal.ZERO;
			if(pontos != null && pontos != BigDecimal.ZERO) {
				resultado = pontos.divide(pontosTotal, 2, RoundingMode.HALF_UP);
			} else {
				resultado = BigDecimal.ZERO;
			}

			//BigDecimal resultado = pontos.divide(new BigDecimal(pontosTotal),2, RoundingMode.HALF_UP);
			percentual = resultado.doubleValue() * 100;
			porcentagemArredondada = (int) Math.round(percentual);
		}

		String strDataFinal = dataFinal;
		String strDataInicial = dataInicial;
		LocalDate hoje = LocalDate.now();

		// Convertendo as strings para LocalDate
		LocalDate dataDataFinal = LocalDate.parse(strDataFinal);
		LocalDate dataDataInicial = LocalDate.parse(dataInicial);
		LocalDate dataDataHoje = LocalDate.parse(hoje.toString());

		// Calculando a diferença em dias
		long diferencaEmDias = ChronoUnit.DAYS.between(dataDataInicial,dataDataFinal);
		long diferencaEmDiasAtual = ChronoUnit.DAYS.between(dataDataHoje,dataDataFinal);

		int quantidadeAtividadesPorDiaProjetada = (pontosTotal.intValue() / (int) diferencaEmDias);
		int dia = hoje.getDayOfMonth();
		int quantidadeAtividadesPorDiaUsuarios = (pontos.intValue() / dia);
		int diferencaMediasAtividades = (quantidadeAtividadesPorDiaProjetada - quantidadeAtividadesPorDiaUsuarios);
		int diferencaDiasProjetadoUsuarios = ((int)diferencaEmDias - dia);
		int quantoAlcancaraComAMedia = (diferencaDiasProjetadoUsuarios * quantidadeAtividadesPorDiaUsuarios) + pontos.intValue();

		String strMensagem1 = "";
		if(quantoAlcancaraComAMedia < pontosTotal.intValue()) {
			strMensagem1 = "Seguindo a projeção, os usuários deveriam está alcançando uma pontuação média de " + quantidadeAtividadesPorDiaProjetada + ". " +
					"Somando todos os usuários, a média de pontos alcançados por dia, até o momento, é de " + quantidadeAtividadesPorDiaUsuarios + ". " +
					"Neste rítimo, a meta não será batida, alcançando uma pontuação máxima de " + quantoAlcancaraComAMedia + ".";
		}

		//Projeção
		int[] diasProjecao = new int[(int)diferencaEmDias]; // Crie um array com os dias do mês
		int valorInicial = 1;
		int valorFinal = pontosTotal.intValue();
		int incremento = (valorFinal / diasProjecao.length);
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

		//Usuários
		List<PontuacaoProjecaoDatasDto> projecaoTotalDatasUsuarios = atividadeRepository.pontuacaoProjecaoDatas(empresa,dataInicial,dataFinal);
		List<PontuacaoUsuarioDto> pontuacaoUsuarios = new ArrayList<PontuacaoUsuarioDto>();
		int pontosAuxiliar = 0;
		boolean primeiro = true;
		int ultimo = 0;
		for(int i = 0; i < diasProjecao.length; i++) {
			PontuacaoUsuarioDto pontUsuario = new PontuacaoUsuarioDto();
			pontosAuxiliar = 0;
			for(int j = 0; j < projecaoTotalDatasUsuarios.size(); j++) {
				if(projecaoTotalDatasUsuarios.get(j).getDia() == (i+1)) {
					if(primeiro) {
						pontosAuxiliar = projecaoTotalDatasUsuarios.get(j).getPontos().intValue();
						primeiro = false;
						ultimo = pontosAuxiliar;
					} else {
						pontosAuxiliar = ultimo + projecaoTotalDatasUsuarios.get(j).getPontos().intValue(); //+ projecaoTotalDatasUsuarios.get(ultimo).getPontos().intValue();
						ultimo = pontosAuxiliar;
					}
				}
			}

			pontUsuario.setDia(i+1);
			pontUsuario.setPontos(new BigDecimal(pontosAuxiliar));
			pontuacaoUsuarios.add(pontUsuario);

		}
		List<PontosAcumuladosUsuariosDto> pontosAcumuadosTotal = new ArrayList<>();

		PontosAcumuladosUsuariosDto pontosAcumuladosUsuariosDtoUsuarios = new PontosAcumuladosUsuariosDto();
		pontosAcumuladosUsuariosDtoUsuarios.setNome("Usuários");
		pontosAcumuladosUsuariosDtoUsuarios.setPontos(pontos.intValue());
		pontosAcumuladosUsuariosDtoUsuarios.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
		pontosAcumuadosTotal.add(pontosAcumuladosUsuariosDtoUsuarios);

		PontosAcumuladosUsuariosDto pontosAcumuladosUsuariosDtoTotal = new PontosAcumuladosUsuariosDto();
		pontosAcumuladosUsuariosDtoTotal.setNome("Total");
		pontosAcumuladosUsuariosDtoTotal.setPontos(pontosTotal.intValue());
		pontosAcumuladosUsuariosDtoTotal.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
		pontosAcumuadosTotal.add(pontosAcumuladosUsuariosDtoTotal);

		dto.setPontosTotal(pontosTotal);
		dto.setPontos(pontos);
		dto.setPercentual(porcentagemArredondada);
		dto.setPercentual(porcentagemArredondada);
		dto.setMensagem1(strMensagem1);
		dto.setPontuacaoProjecao(projecao);
		dto.setPontuacaoUsuarios(pontuacaoUsuarios);
		dto.setAcumulado(pontosAcumuadosTotal);
		return dto;
	}

	public List<PontosUsuariosProjecaoDadosDto> consultaPontosUsuariosProjecaoDado(long empresa, String dataInicial, String dataFinal) {
		List<PontosUsuariosProjecaoDadosDto> pontos = atividadeRepository.pontosUsuariosProjecaoDados(empresa,dataInicial,dataFinal);
		List<PontosUsuariosProjecaoDadosDto> dtos = new ArrayList<PontosUsuariosProjecaoDadosDto>();
		if(pontos != null && pontos.size() > 0) {
			for(PontosUsuariosProjecaoDadosDto pont : pontos) {
				PontosUsuariosProjecaoDadosDto dto = new PontosUsuariosProjecaoDadosDto();
				dto.setCodigoUsuario(pont.getCodigoUsuario());
				dto.setUsuario(pont.getUsuario());
				dto.setPontos(pont.getPontos());
				dto.setProjecao(pont.getProjecao());
				dto.setPercentual(pont.getPercentual());
				dto.setPercentualTexto(pont.getPercentual() + "%");
				dto.setAtividadesConcluidas(pont.getAtividadesConcluidas());
                dto.setAtividadesCriadas(pont.getAtividadesCriadas());
				if(pont.getPercentual().intValue() >= 100) {
					dto.setStatus("Meta Alcançada");
				} else {
					LocalDate dataAtual = LocalDate.now();
					LocalDate dataInicialParse = LocalDate.parse(dataInicial);
					LocalDate dataFinalParse = LocalDate.parse(dataFinal);
					if (dataAtual.isAfter(dataInicialParse) && dataAtual.isBefore(dataFinalParse) ||
							dataAtual.isEqual(dataInicialParse) || dataAtual.isEqual(dataFinalParse)) {
						dto.setStatus("Em andamento");
					} else {
						dto.setStatus("Meta não atingida");
					}
					dto.setStatus("Em andamento");
				}
				dtos.add(dto);
			}
		}

		return dtos;
	}

	public List<AtividadesUsuariosDesempenhoDto> consultarAtividadesUsuario(long usuario,String dataInicial, String dataFinal, String tipoConsulta) {
		Optional<Usuario> usuarioConsulta = usuarioRepository.findById(usuario);
		if(!usuarioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário informado não encontrado");
		}
		List<Atividade> atividades = atividadeRepository.consultarAtividadesPorUsuario(usuario,dataInicial,dataFinal,tipoConsulta);
		List<AtividadesUsuariosDesempenhoDto> dtos = new ArrayList<AtividadesUsuariosDesempenhoDto>();
		if(atividades != null && atividades.size() > 0) {
			for(Atividade atividade : atividades) {
				TituloAtividade titulo = new TituloAtividade();
				Optional<TituloAtividade> tituloAtividade =
						tituloAtividadeRepository.findByTituloAndEmpresaCodigo(atividade.getTitulo(),usuarioConsulta.get().getEmpresa().getCodigo());
				if(!tituloAtividade.isPresent()) {
					titulo.setTitulo(atividade.getTitulo());
					titulo.setPontos(0);
				} else {
					titulo.setTitulo(tituloAtividade.get().getTitulo());
					titulo.setPontos(tituloAtividade.get().getPontos());
				}
				List<HistoricoAtividade> historico = historicoAtividadeRepository.findByAtividadeCodigoOrderByCodigoDesc(atividade.getCodigo());
				List<HistoricoAtividadeDTO> historicoDtos = new ArrayList<>();
				if(historico != null && historico.size() > 0) {
					for(HistoricoAtividade hist : historico) {
						historicoDtos.add(HistoricoAtividadeDTO.build(hist));
					}
				}
				AtividadesUsuariosDesempenhoDto atividadesUsuariosDesempenhoDto = AtividadesUsuariosDesempenhoDto.build(atividade,usuarioConsulta.get(),
						historicoDtos,titulo);
				dtos.add(atividadesUsuariosDesempenhoDto);
			}
		}

		return dtos;
	}

	public List<AtividadesRankingDto> consultarRankingAtividades(long empresa, String dataInicial, String dataFinal) {
		List<AtividadesRankingDto> ranking = atividadeRepository.consultarRankingAtividades(empresa,dataInicial,dataFinal);
		List<AtividadesRankingDto> dtos = new ArrayList<AtividadesRankingDto>();
		if(ranking != null && ranking.size() > 0) {
			String atividadesNot = "";
			int qtde = 1;
			for(AtividadesRankingDto rank : ranking) {
				if(qtde < ranking.size()) {
					atividadesNot = atividadesNot + rank.getCodigo() + ",";
				} else {
					atividadesNot = atividadesNot + rank.getCodigo();
				}
				qtde++;
			}

			int demaisAtividades = 0;
			int demaisPontos = 0;
			List<AtividadesRankingDto> rankingDemais = atividadeRepository.consultarRankingAtividadesDemis(empresa,dataInicial,dataFinal,atividadesNot);
			if(rankingDemais != null && rankingDemais.size() > 0) {
				for(AtividadesRankingDto demais : rankingDemais) {
					demaisAtividades = demaisAtividades + demais.getAtividades().intValue();
					demaisPontos = demaisPontos + demais.getPontuacao();
				}
			}
			for(AtividadesRankingDto ar : ranking) {
				AtividadesRankingDto dto = new AtividadesRankingDto();
				dto.setAtividade(ar.getAtividade());
				dto.setFase(ar.getFase());
				dto.setAtividades(ar.getAtividades());
				dto.setPontuacao(ar.getPontuacao());
				dto.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
				dtos.add(dto);
			}

			AtividadesRankingDto ard = new AtividadesRankingDto();
			ard.setAtividade("Demais Atividades");
			ard.setFase(null);
			ard.setAtividades(new BigInteger(String.valueOf(demaisAtividades)));
			ard.setPontuacao(demaisPontos);
			dtos.add(ard);
		}

		return dtos;
	}
	
	public List<GraficoAtividadeFaseDto> graficoAtividadeFasePorProcesso(long processo,long usuario) {
		return atividadeRepository.graficoAtividadeFasePorProcesso(processo,usuario);
	}
	
	public List<GraficoAtividadesStatusDto> graficoAtividadeStatusPorProcesso(long processo, long usuario) {
		return atividadeRepository.graficoAtividadesStatusPorProcesso(processo,usuario);
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
