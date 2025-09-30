package com.br.ilawgestao.controller;

import java.util.List;

import com.br.ilawgestao.domains.dto.ControleArquivoPushDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.br.ilawgestao.domains.dto.MovimentoProcessualRelatorioDto;
import com.br.ilawgestao.domains.models.MovimentoProcessualRelatorio;
import com.br.ilawgestao.domains.repository.filtros.FiltroMovimentacaoProcessualRelatorio;
import com.br.ilawgestao.domains.service.PushService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/push")
public class PushController {
	
	@Autowired
	private PushService pushService;
	
	@GetMapping("/quantidadePush")
	public int quantidadePushDia(@RequestParam long empresa, @RequestParam String dataCarregamento) {
		return pushService.quantidadePushDia(empresa, dataCarregamento);
	}

	@GetMapping("/movimentos")
	public List<MovimentoProcessualRelatorioDto> consultarMovimentos(@RequestParam long empresa,
																  @RequestParam(required = false) String grupos,
																  @RequestParam(required = false) String dataInicial,
																  @RequestParam(required = false) String dataFinal,
																  @RequestParam(required = false) String dataCarregamento,
																  @RequestParam(required = false) String status,
																  @RequestParam(required = false) String processo,
																  @RequestParam(required = false) String pasta,
																  @RequestParam(required = false) String movimentacao,
																  @RequestParam(required = false) String classificacao,
																  @RequestParam(required = false) String ordem) {
		
		FiltroMovimentacaoProcessualRelatorio filtro = new FiltroMovimentacaoProcessualRelatorio();
		filtro.setEmpresa(empresa);
		filtro.setGrupo(grupos);
		filtro.setDtMovimentoInicial(dataInicial);
		filtro.setDtMovimentacaoFinal(dataFinal);
		filtro.setDtCarregamento(dataCarregamento);
		filtro.setStatusProcessual(status);
		filtro.setNumeroPrcesso(processo);
		filtro.setNumeroPasta(pasta);
		filtro.setMovimentacao(movimentacao);
		filtro.setClassificacao(classificacao);
		filtro.setOrdenacao(ordem);
		
		return pushService.consultarMovimentos(filtro);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<MovimentoProcessualRelatorio> atualizarMovimento(@PathVariable long codigo, @RequestBody MovimentoProcessualRelatorio movimento) {
		MovimentoProcessualRelatorio response = pushService.atualizaMovimento(codigo, movimento);
		return new ResponseEntity<MovimentoProcessualRelatorio>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/incluirArquivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity incluirArquivosPush(@RequestParam MultipartFile file) {
		pushService.incluirArquivosPush(file);
		return new ResponseEntity(HttpStatus.OK);
	}

	@GetMapping("/listarArquivosTj")
	public ResponseEntity<List<ControleArquivoPushDTO>> listarArquivosTj() {
		List<ControleArquivoPushDTO> dtos = pushService.listarArquivosNaoProcessados("N");
		return new ResponseEntity<List<ControleArquivoPushDTO>>(dtos, HttpStatus.OK);
	}

	@PostMapping("/processarArquivos")
	public ResponseEntity processarArquivosTjPb(@RequestParam String dataMovimento) throws Exception {
		pushService.processarArquivoTJPB(dataMovimento);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
