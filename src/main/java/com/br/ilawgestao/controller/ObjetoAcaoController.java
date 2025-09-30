package com.br.ilawgestao.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

import com.br.ilawgestao.domains.dto.ObjetoAcaoDto;
import com.br.ilawgestao.domains.models.ObjetoAcao;
import com.br.ilawgestao.domains.service.ObjetoAcaoService;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/objetos")
public class ObjetoAcaoController {
	
	@Autowired
	private ObjetoAcaoService objetoAcaoService;
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@GetMapping
	public List<ObjetoAcaoDto> listarObjetosAcao(@RequestParam long empresa) {
		return objetoAcaoService.listarObjetosAcao(empresa);
	}
	
	@GetMapping("/subObjetos/{codigo}")
	public ResponseEntity<List<ObjetoAcaoDto>> listarSubObjetos(@PathVariable long codigo) {
		List<ObjetoAcaoDto> dtos = objetoAcaoService.listarSubObjetos(codigo);
		return new ResponseEntity<List<ObjetoAcaoDto>>(dtos, HttpStatus.OK);
	}
	
	@GetMapping("/foraProcesso")
	public List<ObjetoAcao> listaObjetosForaDoProcesso(@RequestParam long empresa, long processo) {
		return objetoAcaoService.listaObjetosAcaoForaDoProcesso(empresa, processo);
	}
	
	@GetMapping("/subObjetosForaProcesso")
	public List<ObjetoAcao> listaSubObjetosForaDoProcesso(@RequestParam long processo, long objetoPai) {
		return objetoAcaoService.listarSubObjetosAcaoForaDoProcesso(processo, objetoPai);
	}
	
	@GetMapping("/{codigo}")
	public ObjetoAcao consultarObjetoAcaoPorCodigo(@PathVariable long codigo) {
		return objetoAcaoService.consultarObjetoAcaoPorCodigo(codigo);
	}
	
	@PostMapping
	public ResponseEntity<ObjetoAcao> cadastrarObjetoAcao(@RequestBody ObjetoAcao objeto, HttpServletResponse response) {
		ObjetoAcao objetoSalvo = objetoAcaoService.cadastrarObjetoAcao(objeto);
		publish.publishEvent(new RecursoCriadoEvent(this, response, objeto.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(objetoSalvo);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<ObjetoAcao> alterarObjetoAcao(@PathVariable long codigo, @RequestBody ObjetoAcao objetoAcao) {
		ObjetoAcao objetoAcaoAlterado = objetoAcaoService.alterarObjetoAcao(codigo, objetoAcao);
		return ResponseEntity.ok(objetoAcaoAlterado);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{codigo}")
	public ResponseEntity excluirObjetoAcao(@PathVariable long codigo) {
		objetoAcaoService.excluirObjetosAcao(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/objetosPorCodigos")
	public ResponseEntity<List<ObjetoAcao>> consultarObjetosPorCodigos(@RequestParam String codigos) {
		List<ObjetoAcao> lista = objetoAcaoService.consultarObjetosPorCodigos(codigos);
		return new ResponseEntity<List<ObjetoAcao>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/consultaRelatorio/{empresa}")
	public ResponseEntity<List<ObjetoAcao>> listarObjetosConsultaRelatorio(@PathVariable long empresa) {
		List<ObjetoAcao> objetos = objetoAcaoService.listaObjetosConsultaRelatorio(empresa);
		return new ResponseEntity<List<ObjetoAcao>>(objetos, HttpStatus.OK);
	}
}
