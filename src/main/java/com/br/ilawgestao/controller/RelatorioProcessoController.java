package com.br.ilawgestao.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import com.br.ilawgestao.domains.dto.RelatorioProcessoViewDto;
import com.br.ilawgestao.domains.models.RelatorioGerado;
import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioProcesso;
import com.br.ilawgestao.domains.service.RelatorioProcessoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/relatorioProcesso")
public class RelatorioProcessoController {
	
	@Autowired
	private RelatorioProcessoService service;
	
	@Autowired
	private ApplicationEventPublisher publish;
		
	@GetMapping
	public List<RelatorioProcessoViewDto> emitirRelatorioProcesso(@RequestParam long empresa,
																  @RequestParam long usuario,
																  @RequestParam(required = false) String pessoa,
																  @RequestParam(required = false) String grupoTrabalho,
																  @RequestParam(required = false) String tipoAcao,
																  @RequestParam(required = false) String areaAtuacao,
																  @RequestParam(required = false) String tipoDecisao,
																  @RequestParam(required = false) String dataDecisaoInicial,
																  @RequestParam(required = false) String dataDecisaoFinal,
																  @RequestParam(required = false) String dataDistribuicaoInicial,
																  @RequestParam(required = false) String dataDistribuicaoFinal,
																  @RequestParam(required = false) String statusProcessual,
																  @RequestParam(required = false) String excetoArquivados,
																  @RequestParam(required = false) String dataSentencaInicial,
																  @RequestParam(required = false) String dataSentencaFinal,
																  @RequestParam(required = false) String objetoAcao,
																  @RequestParam(required = false) String pagamento,
																  @RequestParam(required = false) String operadorLogicoPagamento,
																  @RequestParam(required = false) String valorPagamento,
																  @RequestParam(required = false) String custas,
																  @RequestParam(required = false) String operadorLogicoCustas,
																  @RequestParam(required = false) String valorCustas,
																  @RequestParam(required = false) String dataCadastroInicial,
																  @RequestParam(required = false) String dataCadastroFinal,
																  @RequestParam(required = false) String dataAlteracaoInicial,
																  @RequestParam(required = false) String dataAlteracaoFinal,
																  @RequestParam(required = false) String importanteEmpresa,
																  @RequestParam(required = false) String importanteParaMim,
																  @RequestParam(required = false) String estrategico,
																  @RequestParam(required = false) String pedidos,
																  @RequestParam(required = false) String fase,
																  @RequestParam(required = false) String rito,
																  @RequestParam(required = false) String motivoResultado,
																  @RequestParam(required = false) String uf,
																  @RequestParam(required = false) String dtArquivamentoInicial,
																  @RequestParam(required = false) String dtArquivamentoFinal,
																  @RequestParam(required = false) String responsavel,
																  @RequestParam(required = false) String banca
																  
															  ) {
		FiltroRelatorioProcesso filtro = new FiltroRelatorioProcesso();
		filtro.setEmpresa(empresa);
		filtro.setUsuario(usuario);
		filtro.setPessoa(pessoa);
		filtro.setGrupoTrabalho(grupoTrabalho);
		filtro.setTipoAcao(Long.parseLong(tipoAcao));
		filtro.setAreaAtuacao(Long.parseLong(areaAtuacao));
		
		filtro.setTipoDecisao(Long.parseLong(tipoDecisao));
		
		filtro.setDataInicialDecisao(dataDecisaoInicial);
		filtro.setDataFinalDecisao(dataDecisaoFinal);
		filtro.setDataInicialDistribuicao(dataDistribuicaoInicial);
		filtro.setDataFinalDistribuicao(dataDistribuicaoFinal);
		
		filtro.setStatusProcessual(Long.parseLong(statusProcessual));
		
		if(excetoArquivados == null) {
			filtro.setExcetoProcessosArquivados(false);
		} else {
			filtro.setExcetoProcessosArquivados(true);
		}
		filtro.setDataInicialSentenca(dataSentencaInicial);
		filtro.setDataFinalSentenca(dataSentencaFinal);
		filtro.setObjetoAcao(objetoAcao);
		filtro.setPagamentos(pagamento);
		filtro.setOperadorLogicoPagamento(operadorLogicoPagamento);
		filtro.setValorPagamento(Double.parseDouble(valorPagamento));
		
		filtro.setCustas(custas);
		filtro.setOperadorLogicoCustas(operadorLogicoCustas);
		filtro.setValorCustas(Double.parseDouble(valorCustas));
		
		filtro.setDataInicialCadastro(dataCadastroInicial);
		filtro.setDataFinalCadastro(dataCadastroFinal);
		filtro.setDataInicialAlteracao(dataAlteracaoInicial);
		filtro.setDataFinalAlteracao(dataAlteracaoFinal);
		filtro.setImportanteEmpresa(importanteEmpresa);
		filtro.setImportanteParaMim(importanteParaMim);
		filtro.setEstrategico(estrategico);
		filtro.setPedidos(pedidos);
		filtro.setFase(Long.parseLong(fase));
		filtro.setRito(Long.parseLong(rito));
		filtro.setMotivoResultado(Long.parseLong(motivoResultado));
		filtro.setUf(uf);
		filtro.setDataArquivamentoInicial(dtArquivamentoInicial);
		filtro.setDataArquivamentoFinal(dtArquivamentoFinal);
		filtro.setResponsavel(Long.parseLong(responsavel));
		filtro.setBanca(Long.parseLong(banca));
		List<RelatorioProcessoViewDto> relatorio = service.consultarRelatorioProcesso(filtro);
		return relatorio;
	}
	
	@PostMapping("/exportar")
	public ResponseEntity<RelatorioGerado> exportarRelatorio(@RequestBody RelatorioGerado relatorioProcesso, HttpServletResponse response) {
		RelatorioGerado relatorio = service.exportarRelatorio(relatorioProcesso);
		publish.publishEvent(new RecursoCriadoEvent(this, response, relatorio.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(relatorio);
	}
	
	@GetMapping("/relatoriosExportados/{usuario}")
	public List<RelatorioGerado> listarRelatorioPorUsuario(@PathVariable long usuario) {
		return service.listaRelatorioPorUsuario(usuario);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/excluirRelatorio")
	public ResponseEntity excluirRelatorio(@RequestParam long relatorio, @RequestParam long usuario) {
		service.excluirRelatorio(relatorio, usuario);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/consultarRelatorio/{codigo}")
	public ResponseEntity<RelatorioGerado> consultarRelatorio(@PathVariable long codigo) {
		RelatorioGerado relatorio = service.consultarRelatorio(codigo);
		return new ResponseEntity<RelatorioGerado>(relatorio, HttpStatus.OK);
	}
	
	@GetMapping("/download/{codigo}")
	public ResponseEntity<ByteArrayResource> baixarArquivoTemp(@PathVariable long codigo) {
		RelatorioGerado relatorio = null;
		relatorio = service.consultarRelatorio(codigo);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.header(HttpHeaders.CONTENT_DISPOSITION, 
						"attachment; filename=\"" + relatorio.getRelatorio() + "\"")
				.body(new ByteArrayResource(relatorio.getFile()));
	}
}
