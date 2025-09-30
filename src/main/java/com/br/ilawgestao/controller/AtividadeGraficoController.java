package com.br.ilawgestao.controller;

import java.util.List;

import com.br.ilawgestao.domains.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.service.AtividadeGraficoService;

@RestController
@RequestMapping("/atividadeGraficos")
public class AtividadeGraficoController {
	
	@Autowired
	private AtividadeGraficoService service;
	
	@GetMapping("/total")
	public List<GraficoAtividadeDto> graficoAtividade(@RequestParam long empresa, @RequestParam String dataInicial, @RequestParam String dataFinal) {
		return service.graficoAtividade(empresa, dataInicial, dataFinal);
	}
	
	@GetMapping("/dataLimite")
	public List<GraficoAtividadeDto> graficoAtividadeDataLimite(@RequestParam long empresa, @RequestParam String dataInicial, @RequestParam String dataFinal) {
		return service.graficoAtividadeDataLimite(empresa, dataInicial, dataFinal);
	}
	
	@GetMapping("/concluido")
	public List<GraficoAtividadeDto> graficoAtividadeConcluido(@RequestParam long empresa, @RequestParam String dataInicial, @RequestParam String dataFinal) {
		return service.graficoAtividadeConcluido(empresa, dataInicial, dataFinal);
	}
		
	@GetMapping("/status")
	public List<GraficoAtividadesStatusDto> graficoAtividadesStatus(@RequestParam long empresa, @RequestParam String dataInicial, @RequestParam String dataFinal) {
		return service.graficoAtividadesStatus(empresa, dataInicial, dataFinal);
	}
	
	
	@GetMapping("/situacao")
	public List<GraficoAtividadeSituacaoDto> graficoAtividadesSituacao(@RequestParam long empresa, @RequestParam String dataInicial, @RequestParam String dataFinal) {
		return service.graficoAtividadesSituacao(empresa, dataInicial, dataFinal);
	}
	
	
	@GetMapping("/graficoStatusUsuarioPainel")
	public List<GraficoAtividadesStatusDto> graficoAtividadeStatusUsuarioPainel(@RequestParam long usuario) {
		return service.graficoAtividadeStatusUsuarioPainel(usuario);
	}
	
	@GetMapping("/graficoAtividadeGrupo")
	public List<GraficoAtividadeGrupoDto> graficoAtividadeGrupo(@RequestParam long empresa, String dataInicial, String dataFinal) {
		return service.graficoAtividadeGrupo(empresa, dataInicial, dataFinal);
	}
	
	@GetMapping("/graficoAtividadeFase")
	public ResponseEntity<List<GraficoAtividadeFaseDto>> graficoAtividadeFase(@RequestParam long empresa, String dataInicial, String dataFinal) {
		List<GraficoAtividadeFaseDto> dtos = service.graficoAtividadesFase(empresa, dataInicial, dataFinal);
		return new ResponseEntity<List<GraficoAtividadeFaseDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/graficoFasePorProcesso/{processo}/{usuario}")
	public ResponseEntity<List<GraficoAtividadeFaseDto>> graficoAtividadeFasePorProcesso(@PathVariable long processo, @PathVariable long usuario) {
		List<GraficoAtividadeFaseDto> dtos = service.graficoAtividadeFasePorProcesso(processo, usuario);
		return new ResponseEntity<List<GraficoAtividadeFaseDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/graficoStatusPorProcesso/{processo}/{usuario}")
	public ResponseEntity<List<GraficoAtividadesStatusDto>> graficoAtividadeStatusPorProcesso(@PathVariable long processo, @PathVariable long usuario) {
		List<GraficoAtividadesStatusDto> dtos = service.graficoAtividadeStatusPorProcesso(processo, usuario);
		return new ResponseEntity<List<GraficoAtividadesStatusDto>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/grafico-fase-processo-atividade")
	public ResponseEntity<List<GraficoFaseProcessoAtividadeDTO>> graficoFaseProcessoAtividade(@RequestParam long empresa,
																							  @RequestParam String dataInicial,
																							  @RequestParam String dataFinal) {
		List<GraficoFaseProcessoAtividadeDTO> dtos = service.graficoFaseProcessoAtividade(empresa,dataInicial,dataFinal);
		return new ResponseEntity<List<GraficoFaseProcessoAtividadeDTO>>(dtos,HttpStatus.OK);
	}

	@GetMapping("/grafico-pontuacao-total")
	public ResponseEntity<GraficoPontuacaoTotalDto> graficoPontuacaoTotal(@RequestParam long empresa,
																		  @RequestParam String dataInicial,
																		  @RequestParam String dataFinal) {
		GraficoPontuacaoTotalDto dto = service.graficoPontuacaoTotal(empresa,dataInicial,dataFinal);
		return new ResponseEntity<GraficoPontuacaoTotalDto>(dto,HttpStatus.OK);
	}

	@GetMapping("/pontos-usuarios-projecao-dados")
	public ResponseEntity<List<PontosUsuariosProjecaoDadosDto>> consultarPontosUsuariosProjecaoDados(@RequestParam long empresa,
																									 @RequestParam String dataInicial,
																									 @RequestParam String dataFinal) {
		List<PontosUsuariosProjecaoDadosDto> dtos = service.consultaPontosUsuariosProjecaoDado(empresa,dataInicial,dataFinal);
		return new ResponseEntity<List<PontosUsuariosProjecaoDadosDto>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/atividades-detalhe-usuario")
	public ResponseEntity<List<AtividadesUsuariosDesempenhoDto>> consultarAtividadesPorusuario(@RequestParam long usuario,
																							   @RequestParam String dataInicial,
																							   @RequestParam String dataFinal,
																							   @RequestParam String tipoConsulta) {
		List<AtividadesUsuariosDesempenhoDto> dtos = service.consultarAtividadesUsuario(usuario,dataInicial,dataFinal,tipoConsulta);
		return new ResponseEntity<List<AtividadesUsuariosDesempenhoDto>>(dtos,HttpStatus.OK);
	}

	@GetMapping("/ranking-atividades")
	public ResponseEntity<List<AtividadesRankingDto>> consultarAtividadesRanking(@RequestParam long empresa,
																				 @RequestParam String dataInicial,
																				 @RequestParam String dataFinal) {
		List<AtividadesRankingDto> dtos = service.consultarRankingAtividades(empresa,dataInicial,dataFinal);
		return new ResponseEntity<List<AtividadesRankingDto>>(dtos,HttpStatus.OK);
	}
}
