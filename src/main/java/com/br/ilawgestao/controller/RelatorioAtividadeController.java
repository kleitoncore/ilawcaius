package com.br.ilawgestao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.RelatorioAtividadesDto;
import com.br.ilawgestao.domains.models.RelatorioGerado;
import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioAtividades;
import com.br.ilawgestao.domains.service.RelatorioAtividadeService;

@RestController
@RequestMapping("/relatorioAtividade")
public class RelatorioAtividadeController {
	
	@Autowired
	private RelatorioAtividadeService service;
	
	@GetMapping("/relatorio")
	public ResponseEntity<List<RelatorioAtividadesDto>> relatorioAtividade(@RequestParam long empresa,
			                                                               @RequestParam long usuario,
			                                                               @RequestParam(required = false) String dtLimiteInicial,
			                                                               @RequestParam(required = false) String dtLimiteFinal,
			                                                               @RequestParam(required = false) String dtConcluidoInicial,
			                                                               @RequestParam(required = false) String dtConcluidoFinal,
			                                                               @RequestParam(required = false) String dtCriacaoInicial,
			                                                               @RequestParam(required = false) String dtCriacaoFinal,
			                                                               @RequestParam(required = false) String dtAlteracaoInicial,
			                                                               @RequestParam(required = false) String dtAlteracaoFinal,
			                                                               @RequestParam(required = false) String dtFatalInicial,
			                                                               @RequestParam(required = false) String dtFatalFinal,
			                                                               @RequestParam(required = false) String responsavel,
			                                                               @RequestParam(required = false) String interessado,
			                                                               @RequestParam(required = false) String grupoTrabalho,
			                                                               @RequestParam(required = false) String status,
			                                                               @RequestParam(required = false) String tipoAtividade,
			                                                               @RequestParam long classificacao,
			                                                               @RequestParam long ordenacao) {
		
		FiltroRelatorioAtividades filtro = new FiltroRelatorioAtividades();
		filtro.setEmpresa(empresa);
		filtro.setUsuario(usuario);
		filtro.setDataLimiteInicial(dtLimiteInicial);
		filtro.setDataLimiteFinal(dtLimiteFinal);
		filtro.setDataConcluidoInicial(dtConcluidoInicial);
		filtro.setDataConcluidoFinal(dtConcluidoFinal);
		filtro.setDataCriacaoInicial(dtCriacaoInicial);
		filtro.setDataCriacaoFinal(dtCriacaoFinal);
		filtro.setDataAlteracaoInicial(dtAlteracaoInicial);
		filtro.setDataAlteracaoFinal(dtAlteracaoFinal);
		filtro.setDataFatalInicial(dtFatalInicial);
		filtro.setDataFatalFinal(dtFatalFinal);
		filtro.setResponsavel(Long.parseLong(responsavel));
		filtro.setInteressado(Long.parseLong(interessado));
		filtro.setGrupoTrabalho(Long.parseLong(grupoTrabalho));
		filtro.setStatus(status);
		filtro.setTipoAtividade(tipoAtividade);
		filtro.setClassificacao((int) classificacao);
		filtro.setOrdenacao((int) ordenacao);
		List<RelatorioAtividadesDto> dtos = service.relatorioAtividade(filtro);
		return new ResponseEntity<List<RelatorioAtividadesDto>>(dtos, HttpStatus.OK);
	}
	
	@PostMapping("/exportar")
	public ResponseEntity<RelatorioGerado> exportarRelatorio(@RequestBody FiltroRelatorioAtividades filtro) {
		RelatorioGerado relatorio = service.exportarRelatorioAtividade(filtro);
		return ResponseEntity.status(HttpStatus.CREATED).body(relatorio);
	}
	
	@GetMapping("/download/{codigo}")
	public ResponseEntity<ByteArrayResource> baixarArquivo(@PathVariable long codigo) {
		RelatorioGerado relatorio = null;
		relatorio = service.consultarRelatorio(codigo);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.header(HttpHeaders.CONTENT_DISPOSITION, 
						"attachment; filename=\"" + relatorio.getRelatorio() + "\"")
				.body(new ByteArrayResource(relatorio.getFile()));
	}
	
	@GetMapping("/consultarRelatorio/{codigo}")
	public ResponseEntity<RelatorioGerado> consultarRelatorio(@PathVariable long codigo) {
		RelatorioGerado relatorio = service.consultarRelatorio(codigo);
		return new ResponseEntity<RelatorioGerado>(relatorio, HttpStatus.OK);
	}
	
	@GetMapping("/relatorioGerado/{usuario}")
	public ResponseEntity<List<RelatorioGerado>> listarRelatoriosGerados(@PathVariable long usuario) {
		List<RelatorioGerado> lista = service.listaRelatorioPorUsuario(usuario);
		return new ResponseEntity<List<RelatorioGerado>>(lista, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirRelatorio(@PathVariable long codigo) {
		service.excluirRelatorio(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
