package com.br.ilawgestao.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.br.ilawgestao.domains.dto.ProcessoExcluidoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.ilawgestao.domains.dto.PartesDto;
import com.br.ilawgestao.domains.dto.ProcessoDto;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Lancamento;
import com.br.ilawgestao.domains.models.Partes;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.ProcessoExcluido;
import com.br.ilawgestao.domains.models.ProcessoImportancia;
import com.br.ilawgestao.domains.models.StatusProcessual;
import com.br.ilawgestao.domains.models.TipoAcao;
import com.br.ilawgestao.domains.models.TipoDecisao;
import com.br.ilawgestao.domains.repository.filtros.FiltroProcesso;
import com.br.ilawgestao.domains.service.ProcessoImportanciaService;
import com.br.ilawgestao.domains.service.ProcessoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/processos")
public class ProcessoController {
	
	@Autowired
	private ProcessoService processoService;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@Autowired
	private ProcessoImportanciaService processoImportanciaService;
	
	@PostMapping
	public ResponseEntity<ProcessoDto> cadastrarProcesso(@RequestBody ProcessoDto processoDto) {
		ProcessoDto dto = processoService.cadastrarProcesso(processoDto);
		return new ResponseEntity<ProcessoDto>(dto, HttpStatus.OK);
	}
	
	@PostMapping("/partes")
	public ResponseEntity<Partes> cadastrarPartes(@RequestBody Partes parte, HttpServletResponse response) {
		Partes parteSalva = processoService.cadastrarPartes(parte);
		publish.publishEvent(new RecursoCriadoEvent(this, response, parteSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(parteSalva);
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/indice")
	public ResponseEntity incluirIndiceProcesso(@RequestBody ProcessoDto processo) {
		processoService.cadastrarIndiceProcesso(processo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	private String consultarImportancia(long usuario, long processo) {
		String retorno = "";
		ProcessoImportancia importancia = processoImportanciaService.consultarProcessoImportancia(usuario, processo);
		if(importancia != null) {
			retorno = "p-button-rounded p-button-warning";
		} else {
			retorno = "p-button-rounded p-button-secondary";
		}
		
		return retorno;
	}
	
	@PutMapping("/importancia/{codigo}")
	public ResponseEntity<ProcessoDto> alterarImportanciaParaEmpresa(@PathVariable long codigo, @RequestBody ProcessoDto processoDto) {
		ProcessoDto dto = processoService.alterarImportanteParaEmpresa(codigo, processoDto);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@PutMapping("/estrategico/{codigo}")
	public ResponseEntity<ProcessoDto> alterarEstrategicoEmpresa(@PathVariable long codigo, @RequestBody ProcessoDto processoDto) {
		ProcessoDto dto = processoService.alterarProcessoEstrategico(codigo, processoDto);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@PutMapping("/excluirProcesso/{codigo}")
	public ResponseEntity<ProcessoDto> enviarProcessoLixeira(@PathVariable long codigo, @RequestBody ProcessoDto processoDto) {
		ProcessoDto dto = processoService.excluirProcesso(codigo, processoDto, processoDto.getUsuario().getCodigo());
		return new ResponseEntity<ProcessoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/porIndice")
	public List<Processo> consultarProcessoPorIndice(@RequestParam String indice, long empresa, long usuario) {
		String strIndice = indice.replace("-", "");
		       strIndice = strIndice.replace(".", "");
		List<Processo> processos = processoService.consultarProcessosPorIndice(strIndice,empresa,usuario);
		List<Processo> processosLista = new ArrayList<Processo>();
		for(Processo p : processos) {
			Processo pro = new Processo();
			pro.setCodigo(p.getCodigo());
			pro.setNrCnj(p.getNrCnj());
			pro.setNrProcesso(p.getNrProcesso());
			pro.setPasta(p.getPasta());
			pro.setAutor(processoService.retornaPartes(p.getCodigo()));
			pro.setGrupoTrabalho(p.getGrupoTrabalho());
			pro.setStatusProcessual(p.getStatusProcessual());
			pro.setImportanteParaMim(this.consultarImportancia(usuario, pro.getCodigo()));
			pro.setSnImportante(p.getSnImportante());
			pro.setFase(p.getFase());
			if(pro.getSnImportante() != null) {
				if(pro.getSnImportante().equals("S")) {
					pro.setImportanteParaEmpresa("p-button-rounded p-button-danger");
				} else {
					pro.setImportanteParaEmpresa("p-button-rounded p-button-secondary");
				}
			} else {
				pro.setImportanteParaEmpresa("p-button-rounded p-button-secondary");
			}
			//pro.setDataUltimaMovimentacao(DatasUtil.formatarDataTela(p.getDataUltimaMovimentacao()));
			processosLista.add(pro);
		}
		
		return processosLista;
	}
	
	@GetMapping("/porIndice2")
	public ResponseEntity<List<ProcessoDto>> consultarProcessoPorIndice2(@RequestParam String indice, long empresa, long usuario) {
		List<ProcessoDto> dtos = processoService.consultarProcessoShort(indice, empresa, usuario);
		return new ResponseEntity<List<ProcessoDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<ProcessoDto>> consultarProcessos(
			@RequestParam long empresa,
			@RequestParam(required = false) String pessoa,
			@RequestParam(required = false) String grupoTrabalho,
			@RequestParam(required = false) String tipoAcao,
			@RequestParam(required = false) String statusProcessual,
			@RequestParam(required = false) String tipoDecisao,
			@RequestParam(required = false) String pasta,
			@RequestParam(required = false) String numeroProcesso,
			@RequestParam(required = false) String numeroCnj,
			@RequestParam(required = false) String importanteParaMim,
			@RequestParam(required = false) String importanteParaEmpresa,
			@RequestParam(required = false) String estrategico,
			@RequestParam(required = false) String usuario) {
		
		Empresa empresaObj = new Empresa();
		empresaObj.setCodigo(empresa);
		
		GrupoTrabalho grupoObj = null;
		if(grupoTrabalho != null) {
			grupoObj = new GrupoTrabalho();
			grupoObj.setCodigo(Long.parseLong(grupoTrabalho));
		}
		
		TipoAcao tipoAcaoObj = null;
		if(tipoAcao != null) {
			tipoAcaoObj = new TipoAcao();
			tipoAcaoObj.setCodigo(Long.parseLong(tipoAcao));
		}
		
		StatusProcessual statusProcessualObj = null;
		if(statusProcessual != null) {
			statusProcessualObj = new StatusProcessual();
			statusProcessualObj.setCodigo(Long.parseLong(statusProcessual));
		}
		
		TipoDecisao tipoDecisaoObj = null;
		if(tipoDecisao != null) {
			tipoDecisaoObj = new TipoDecisao();
			tipoDecisaoObj.setCodigo(Long.parseLong(tipoDecisao));
		}
		
		FiltroProcesso filtro = new FiltroProcesso(pessoa, grupoObj, tipoAcaoObj, statusProcessualObj, 
				tipoDecisaoObj, empresaObj, pasta, numeroProcesso, numeroCnj, importanteParaMim, importanteParaEmpresa, estrategico, usuario);
		List<ProcessoDto> dtos = processoService.consultarProcessos(filtro);
		return new ResponseEntity<List<ProcessoDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/{codigo}/{usuario}")
	public ResponseEntity<ProcessoDto> consultarProcessoPorCodigo(@PathVariable long codigo, @PathVariable long usuario) {
		ProcessoDto dto = processoService.consultarProcesso(codigo,usuario);
		return new ResponseEntity<ProcessoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/partes/{processo}")
	public List<Partes> consultarPartesPorProcesso(@PathVariable long processo) {
		return processoService.consultarPartes(processo);
	}
	
	@GetMapping("/partesTipo")
	public List<Partes> consultarPartesPorProcessoTipo(@RequestParam long processo, String tipo) {
		return processoService.listarPartesPorTipo(processo, tipo);
	}
	
	@GetMapping("/partesPorProcesso/{processo}")
	public ResponseEntity<List<PartesDto>> consultarPartesPorProcessoCompleto(@PathVariable long processo) {
		List<PartesDto> dtos = processoService.listarPartesPorProcesso(processo);
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<ProcessoDto> alterarProcesso(@PathVariable long codigo, @RequestBody ProcessoDto processoDto) {
		ProcessoDto dto = processoService.alterarProcesso(codigo, processoDto);
		return new ResponseEntity<ProcessoDto>(dto, HttpStatus.OK);
	}
	
	@GetMapping("/autores")
	public String retornaAutor(@PathVariable long processo) {
		return processoService.retornaAutor(processo);
	}
	
	@GetMapping("/reus")
	public String retornaReu(@PathVariable long processo) {
		return processoService.retornaReu(processo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/partes/{codigo}")
	public ResponseEntity excluirParte(@PathVariable long codigo) {
		this.processoService.excluirParte(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/lancamentosFinanceiros/{processo}")
	public List<Lancamento> consultarLancamentosPorProcesso(@PathVariable long processo) {
		return this.processoService.consultarLancamentosPorProcesso(processo);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/indice/{processo}")
	public ResponseEntity excluirIndiceProcesso(@PathVariable long processo) {
		this.processoService.excluirIndiceProcesso(processo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/processosExcluidos/{empresa}")
	public List<ProcessoExcluidoDto> listarProcessosExcluidos(@PathVariable long empresa) {
		return processoService.listarProcessosExcluidos(empresa);
	}
	
	@PutMapping("/ativarProcesso/{codigo}")
	public ResponseEntity<ProcessoDto> ativarProceso(@PathVariable long codigo, @RequestBody ProcessoDto processoDto) {
		ProcessoDto dto = processoService.ativarProcesso(codigo, processoDto, processoDto.getUsuario().getCodigo());
		return new ResponseEntity<ProcessoDto>(dto, HttpStatus.OK);
	}

	@DeleteMapping("excluir-definitivamente/{processo}")
	public ResponseEntity excluirProcessoDefinitivamente(@PathVariable long processo) {
		processoService.excluirProcessoDefinitivamente(processo);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
