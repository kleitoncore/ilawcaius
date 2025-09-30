package com.br.ilawgestao.controller;

import java.util.List;

import com.br.ilawgestao.domains.dto.ProcessoDto;
import com.br.ilawgestao.domains.repository.filtros.FiltroProcesso;
import com.br.ilawgestao.domains.service.ProcessosParadadosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.GraficoProcessoDto;
import com.br.ilawgestao.domains.dto.GraficoProcessoMesDto;
import com.br.ilawgestao.domains.dto.ProcessoArquivadoDto;
import com.br.ilawgestao.domains.service.GraficoProcessoService;
import com.br.ilawgestao.domains.service.ProcessoPainelIndicadoresService;

@RestController
@RequestMapping("/graficoProcesso")
public class GraficoProcessoController {
	
	@Autowired
	private GraficoProcessoService service;

	@Autowired
	private ProcessosParadadosService processosParadadosService;
	
	@Autowired
	private ProcessoPainelIndicadoresService processoPainelService;
	
	@GetMapping("/statusProcessual/{empresa}")
	public List<GraficoProcessoDto> graficoStatusProcessual(@PathVariable long empresa) {
		return service.graficoProcessoStatusProcessual(empresa);
	}
	
	@GetMapping("/grupoTrabalho/{empresa}")
	public List<GraficoProcessoDto> graficoGrupoTrabalho(@PathVariable long empresa) {
		return service.graficoProcessoGrupoTrabalho(empresa);
	}
	
	@GetMapping("/areaAtuacao/{empresa}")
	public List<GraficoProcessoDto> graficoAreaAtuacao(@PathVariable long empresa) {
		return service.graficoProcessoAreaAtuacao(empresa);
	}
	
	@GetMapping("/tipoAcao/{empresa}")
	public List<GraficoProcessoDto> graficoTipoAcao(@PathVariable long empresa) {
		return service.graficoProcessoTipoAcao(empresa);
	}
	
	@GetMapping("/objetosAcao/{empresa}")
	public List<GraficoProcessoDto> graficoObjetosAcao(@PathVariable long empresa) {
		return service.graficoProcessoObjetosAcao(empresa);
	}
	
	@GetMapping("/graficoProcessoArquivado")
	public List<ProcessoArquivadoDto> graficoProcessoArquivado(@RequestParam long empresa, @RequestParam String dataInicial, @RequestParam String dataFinal) {
		return service.graficoProcessoArquivado(empresa, dataInicial, dataFinal);
	}
	
	@GetMapping("/graficoProcessoMes")
	public List<GraficoProcessoMesDto> graficoProcessoMes(@RequestParam long empresa, @RequestParam String dataInicial, @RequestParam String dataFinal) {
		return service.graficoProcessoMes(empresa, dataInicial, dataFinal);
	}
	
	@GetMapping("/totalProcessos/{empresa}")
	public int totalProcessos(@PathVariable long empresa) {
		return processoPainelService.totalProcessos(empresa);
	}
	
	@GetMapping("/totalAtivos/{empresa}")
	public int totalAtivos(@PathVariable long empresa) {
		return processoPainelService.totalAtivos(empresa);
	}
	
	@GetMapping("/processosParados/{empresa}")
	public int processosParados(@PathVariable long empresa) {
		return processoPainelService.parados(empresa);
	}
	
	@GetMapping("/processosArquivados/{empresa}")
	public int processosArquivados(@PathVariable long empresa) {
		return processoPainelService.arquivados(empresa);
	}
	
	@GetMapping("/processosExcluidos/{empresa}")
	public int processosExcluidos(@PathVariable long empresa) {
		return processoPainelService.excluidos(empresa);
	}
	
	@GetMapping("/processosPush/{empresa}")
	public int processosNoPush(@PathVariable long empresa) {
		return processoPainelService.processosPush(empresa);
	}

	@GetMapping("/processos-parado")
	public ResponseEntity<List<ProcessoDto>> consultarProcesosParados(@RequestParam long empresa,
																	  @RequestParam (required = false, defaultValue = "") String numeroCnj,
																	  @RequestParam (required = false, defaultValue = "") String numeroProcesso,
																	  @RequestParam (required = false, defaultValue = "") String parte) {
		FiltroProcesso filtro = new FiltroProcesso();
		filtro.setNumeroCnj(numeroCnj);
		filtro.setNumeroProcesso(numeroProcesso);
		filtro.setPessoa(parte);
		List<ProcessoDto> dtos = processosParadadosService.consultarProcessosParados(empresa,filtro);
		return new ResponseEntity<List<ProcessoDto>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/processos-cadastrados-este-mes/{empresa}")
	public ResponseEntity<List<ProcessoDto>> consultarProcessosCadastradosEsteMes(@PathVariable long empresa) {
		List<ProcessoDto> dtos = service.consultarProcessosCadastradosEsteMes(empresa);
		return new ResponseEntity<List<ProcessoDto>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/consulta-processos-arquivados/{empresa}")
	public ResponseEntity<List<ProcessoDto>> consultarProcessosArquivados(@PathVariable long empresa) {
		List<ProcessoDto> dtos = service.consultarProcessosArquivados(empresa);
		return new ResponseEntity<List<ProcessoDto>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/consulta-processos-lixeira/{empresa}")
	public ResponseEntity<List<ProcessoDto>> consultarProcessosLixeira(@PathVariable long empresa) {
		List<ProcessoDto> dtos = service.consultarProcessosLixeira(empresa);
		return new ResponseEntity<List<ProcessoDto>>(dtos,HttpStatus.OK);
	}
}
