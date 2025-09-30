package com.br.ilawgestao.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.repository.filtros.FiltroHistoricoAtividadeFase;
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

import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.AtividadeShort;
import com.br.ilawgestao.domains.models.HistoricoAtividade;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.filtros.FiltroAgenda;
import com.br.ilawgestao.domains.repository.filtros.FiltroKanban;
import com.br.ilawgestao.domains.repository.filtros.FiltroMiniAgenda;
import com.br.ilawgestao.domains.service.AtividadeService;
import com.br.ilawgestao.domains.utils.DatasUtil;
import com.br.ilawgestao.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {
	
	@Autowired
	private ApplicationEventPublisher publish;
	
	@Autowired
	private AtividadeService atividadeService;
	
	@PostMapping
	public ResponseEntity<Atividade> incluirAtividade(@RequestBody AtividadeCadastroDTO dto, HttpServletResponse response) {
		Atividade atividadeSalva = atividadeService.incluirAtividade(dto);
		publish.publishEvent(new RecursoCriadoEvent(this, response, atividadeSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(atividadeSalva);
	}
	
	@GetMapping("/{codigo}/{usuario}")
	public Atividade consultarAtividade(@PathVariable long codigo, @PathVariable long usuario) {
		return atividadeService.consultarAtividade(codigo,usuario);
	}
	
	@PostMapping("/historico")
	public ResponseEntity<HistoricoAtividade> incluirHistoricoAtividade(@RequestBody HistoricoAtividadeDTO historicoDto, HttpServletResponse response) {
		historicoDto.setDtHistorico(DatasUtil.getDataAtual());
		HistoricoAtividade historicoSalvo = atividadeService.incluirHistorico(historicoDto.transformeParaObjeto());
		publish.publishEvent(new RecursoCriadoEvent(this, response, historicoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(historicoSalvo);
	}
	
	@GetMapping("/historico/{atividade}")
	public List<HistoricoAtividade> consultarHistorico(@PathVariable long atividade) {
		return atividadeService.consultarHistorico(atividade);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/historico")
	public ResponseEntity excluirHistórico(@RequestParam long historico, @RequestParam long usuarioCadastrou, @RequestParam long usuarioSolicitou) {
		atividadeService.excluirHistorico(historico, usuarioCadastrou, usuarioSolicitou);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/usuariosAtividade")
	public List<Usuario> consultarUsuariosAtividade(@RequestParam long atividade, @RequestParam String tipo) {
		return atividadeService.consultarUsuariosAtividade(atividade, tipo);
	}
	
	@GetMapping("/atividadesAtrasadasResponsavel/{usuario}")
	public List<AtividadeShort> listarAtividadesAtrasadasReponsavel(@PathVariable long usuario,
																	@RequestParam(required = false) String pesquisa,
																	@RequestParam(required = false) String importante,
																	@RequestParam(required = false) String urgente,
																	@RequestParam(required = false) String fatal) {
		return atividadeService.listarAtividadesAtrasadasResponsavel(usuario,pesquisa,importante,urgente,fatal);
	}
	
	@GetMapping("/atividadesHojeResponsavel/{usuario}")
	public List<AtividadeShort> listarAtividadesHojeResponsavel(@PathVariable long usuario,
																@RequestParam(required = false) String pesquisa,
																@RequestParam(required = false) String importante,
																@RequestParam(required = false) String urgente,
																@RequestParam(required = false) String fatal) {
		return atividadeService.listarAtividadesHojeResponsavel(usuario,pesquisa,importante,urgente,fatal);
	}
	
	@GetMapping("/atividadesSeteResponsavel/{usuario}")
	public List<AtividadeShort> listarAtividadesSeteResponsavel(@PathVariable long usuario,
																@RequestParam(required = false) String pesquisa,
																@RequestParam(required = false) String importante,
																@RequestParam(required = false) String urgente,
																@RequestParam(required = false) String fatal) {
		return atividadeService.listarAtividadesSeteResponsavel(usuario,pesquisa,importante,urgente,fatal);
	}
	
	@GetMapping("/atividadesTrintaResponsavel/{usuario}")
	public List<AtividadeShort> listarAtividadesTrintaResponsavel(@PathVariable long usuario,
																  @RequestParam(required = false) String pesquisa,
																  @RequestParam(required = false) String importante,
																  @RequestParam(required = false) String urgente,
																  @RequestParam(required = false) String fatal) {
		return atividadeService.listarAtividadesTrintaResponsavel(usuario, pesquisa, importante, urgente,fatal);
	}
	
	@GetMapping("/atividadesAtrasadasInteressados/{usuario}")
	public List<AtividadeShort> listaAtrasadasInteressados(@PathVariable long usuario,
														   @RequestParam(required = false) String pesquisa,
														   @RequestParam(required = false) String importante,
														   @RequestParam(required = false) String urgente,
														   @RequestParam(required = false) String fatal) {
		return atividadeService.listarAtividadesAtrasadasInteressado(usuario, pesquisa, importante, urgente,fatal);
	}
	
	@GetMapping("/atividadesHojeInteressados/{usuario}")
	public List<AtividadeShort> listaHojeInteressados(@PathVariable long usuario,
													  @RequestParam(required = false) String pesquisa,
													  @RequestParam(required = false) String importante,
													  @RequestParam(required = false) String urgente,
													  @RequestParam(required = false) String fatal) {
		return atividadeService.listarAtividadesHojeInteressado(usuario, pesquisa, importante, urgente, fatal);
	}
	

	@GetMapping("/atividadesSeteInteressados/{usuario}")
	public List<AtividadeShort> listaSeteInteressados(@PathVariable long usuario,
													  @RequestParam(required = false) String pesquisa,
													  @RequestParam(required = false) String importante,
													  @RequestParam(required = false) String urgente,
													  @RequestParam(required = false) String fatal) {
		return atividadeService.listarAtividadesSeteInteressado(usuario, pesquisa, importante, urgente, fatal);
	}
	
	@GetMapping("/atividadesTrintaInteressados/{usuario}")
	public List<AtividadeShort> listaTrintaInteressados(@PathVariable long usuario,
														@RequestParam(required = false) String pesquisa,
														@RequestParam(required = false) String importante,
														@RequestParam(required = false) String urgente,
														@RequestParam(required = false) String fatal) {
		return atividadeService.listarAtividadesTrintaInteressado(usuario, pesquisa, importante, urgente, fatal);
	}
	
	@GetMapping("/compromissosAtrasados/{usuario}")
	public List<AtividadeShort> listarCompromissosAtrasadosResponsavel(@PathVariable long usuario, @RequestParam(required = false) String pesquisa) {
		return atividadeService.listarCompromissosAtrasadasResponsavel(usuario, pesquisa);
	}
	
	@GetMapping("/compromissosHoje/{usuario}")
	public List<AtividadeShort> listarCompromissosHojeResponsavel(@PathVariable long usuario, @RequestParam(required = false) String pesquisa) {
		return atividadeService.listarCompromissosHojeResponsavel(usuario, pesquisa);
	}
	
	@GetMapping("/compromissosSete/{usuario}")
	public List<AtividadeShort> listarCompromissosSeteResponsavel(@PathVariable long usuario, @RequestParam(required = false) String pesquisa) {
		return atividadeService.listarCompromissosSeteResponsavel(usuario, pesquisa);
	}
	
	@GetMapping("/compromissosTrinta/{usuario}")
	public List<AtividadeShort> listarCompromissosTrintaResponsavel(@PathVariable long usuario, @RequestParam(required = false) String pesquisa) {
		return atividadeService.listarCompromissosTrintaResponsavel(usuario, pesquisa);
	}
	
	@PutMapping("/alterarAtividade/{codigo}/dataAnterior/{dataAntiga}/statusAnterior/{statusAntigo}/usuario/{usuario}")
	public Atividade alterarAtividade(@PathVariable long codigo, @PathVariable String dataAntiga, @PathVariable long statusAntigo,
			@PathVariable long usuario, @RequestBody AtividadeCadastroDTO atividade) {
		Atividade atividadeAlterada = atividadeService.alterarAtividade(codigo, dataAntiga, statusAntigo, usuario, atividade);
		return atividadeAlterada;
	}
	
	@GetMapping("/porProcesso/{processo}")
	public List<Atividade> consultarAtividadesPorProcesso(@PathVariable long processo) {
		return atividadeService.consultarAtividadesPorProcesso(processo);
	}
	
	@GetMapping("/atividadesCriadasUsuario/{usuario}")
	public int atividadesCriadasUsuario(@PathVariable long usuario) {
		return atividadeService.atividadesCriadasUsuario(usuario);
	}
	
	@GetMapping("/atividadesUltimosTrintaDiasUsuario/{usuario}")
	public int atividadesUltimosTrintaDiasUsuario(@PathVariable long usuario) {
		return atividadeService.atividadesUltimosTrintaDiasUsuario(usuario);
	}
	
	@GetMapping("/atividadesUltimosTrintaDiasConcluidasUsuario/{usuario}")
	public int atividadesUltimosTrintaDiasConcluidasUsuario(@PathVariable long usuario) {
		return atividadeService.atividadesConcluidasUltimosTrintaDiasUsuario(usuario);
	}
	
	@GetMapping("/atividadesUltimosTrintaDiasConcluidasAtrasadasUsuario/{usuario}")
	public int atividadesUltimosTrintaDiasConcluidasAtrasadasUsuario(@PathVariable long usuario) {
		return atividadeService.atividadesConcluidasAtrasdasUltimosTrintaDiasUsuario(usuario);
	}
	
	@GetMapping("/atividadesUltimosTrintaDiasConcluidasNoPrazoUsuario/{usuario}")
	public int atividadesUltimosTrintaDiasConcluidasNoPrazoUsuario(@PathVariable long usuario) {
		return atividadeService.atividadesConcluidasNoPrazoUltimosTrintaDiasUsuario(usuario);
	}
	
	@GetMapping("/pontuacao")
	public double pontuacao(@RequestParam long responsavel, @RequestParam long interesse, @RequestParam long agenda) {
		return atividadeService.retornaPontuacao(responsavel, interesse, agenda);
	}
	
	//Kanban
	@GetMapping("/kanban")
	public List<AtividadeShort> atividadesKanbanFazer(@RequestParam long empresa, @RequestParam String grupo, @RequestParam String usuario,
			@RequestParam(required = false) String dataInicial, @RequestParam(required = false) String dataFinal,
			@RequestParam String status, @RequestParam long limite) {
		
		FiltroKanban filtro = new FiltroKanban();
		filtro.setEmpresa(empresa);
		filtro.setGrupo(grupo);
		filtro.setUsuario(usuario);
		filtro.setDataLimiteInicial(dataInicial);
		filtro.setDataLimiteFinal(dataFinal);
		filtro.setStatus(status);
		filtro.setLimite(limite);
		return atividadeService.painelKanban(filtro);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("/alterarMassa")
	public ResponseEntity alterarEmMassa(@RequestBody AlterarAtividadeMassaDto dto) {
		atividadeService.alterarMassa(dto);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/atividadeAgrupada")
	public ResponseEntity cadastrarAtividadeAgrupada(@RequestBody AtividadeAgrupadaDto atividade) {
		atividadeService.cadastrarAtividadeAgrupada(atividade);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private String formatarCnj(String texto) {
		// Verifica se a string segue o formato esperado
		if (texto.matches("\\d{7}-\\d{2}\\.\\d{4}\\.\\d\\.\\d{2}\\.\\d{4}")) {
			// Remove todos os caracteres não numéricos
			return texto.replaceAll("\\D", "");
		} else {
			// Retorna a string original caso o formato não seja correspondente
			return texto;
		}
	}
	
	@GetMapping("/listaPorDataLimite")
	public ResponseEntity<List<AtividadesGeralHomeDto>> listarAtividadesPorDataLimite(@RequestParam(required = true) long empresa,
			                                                             @RequestParam(required = true) long usuario,
																		 @RequestParam(defaultValue = "0") String data,
																		 @RequestParam(defaultValue = "0") String dataLimiteInicial,
																		 @RequestParam(defaultValue = "0") String processo,
																		 @RequestParam(defaultValue = "0") String dataLimiteFinal,
																		 @RequestParam(defaultValue = "0") String prazoFatalInicial,
																		 @RequestParam(defaultValue = "0") String prazoFatalFinal,
																		 @RequestParam(defaultValue = "0") String dataConclusaoInicial,
																		 @RequestParam(defaultValue = "0") String dataConclusaoFinal,
																		 @RequestParam(defaultValue = "T") String importante,
																		 @RequestParam(defaultValue = "T") String urgente,
																		 @RequestParam boolean isFinanceiro,
																		 @RequestParam(defaultValue = "0") String tipoUsuario,
																		 @RequestParam boolean isPrazoFatal,
																		 @RequestParam(defaultValue = "0") String partes,
																		 @RequestParam(defaultValue = "0") String fase,
																		 @RequestParam(defaultValue = "0") String status,
																		 @RequestParam(defaultValue = "4") int classificacao,
																		 @RequestParam(defaultValue = "DESC") String ordenacao) {
		
		FiltroMiniAgenda filtro = new FiltroMiniAgenda();
		filtro.setEmpresa(empresa);
		filtro.setUsuario(usuario);
		filtro.setData(data);
		filtro.setDataLimiteInicial(dataLimiteInicial);
		filtro.setDataLimiteFinal(dataLimiteFinal);
		filtro.setPrazoFatalInicial(prazoFatalInicial);
		filtro.setPrazoFatalFinal(prazoFatalFinal);
		filtro.setDataConclusaoInicial(dataConclusaoInicial);
		filtro.setDataConclusaoFinal(dataConclusaoFinal);
		filtro.setImportante(importante);
		filtro.setUrgente(urgente);
		filtro.setFinanceiro(isFinanceiro);
		filtro.setTipoUsuario(tipoUsuario);
		filtro.setPrazoFatal(isPrazoFatal);
		filtro.setProcesso(this.formatarCnj(processo));
		filtro.setStatus(status);
		filtro.setPartes(partes);
		filtro.setFase(fase);
		filtro.setClassificacao(classificacao);
		filtro.setOrdenacao(ordenacao);
		List<AtividadesGeralHomeDto> atividades = atividadeService.consultarAtividadesGeralHome(filtro);
		return new ResponseEntity<List<AtividadesGeralHomeDto>>(atividades, HttpStatus.OK);
	}
	
	@GetMapping("/agenda-mes-usuario")
	public ResponseEntity<List<MiniAgendaDto>> consultarAgendaMesUsuario(@RequestParam long usuario, 
			@RequestParam String mes, @RequestParam String ano) {
		FiltroAgenda filtro = new FiltroAgenda();
		filtro.setUsuario(usuario);
		filtro.setMes(mes);
		filtro.setAno(ano);
		List<MiniAgendaDto> lista = atividadeService.consultarAgendaMesUsuario(filtro);
		return new ResponseEntity<List<MiniAgendaDto>>(lista, HttpStatus.OK);
	}

	@GetMapping("/consultar-historico-fase-atividade")
	public ResponseEntity<List<HistoricoFaseProcessualViewDto>> consultarHistoricoFaseAtividades(@RequestParam long processo,
																								 @RequestParam String fase,
																								 @RequestParam String status,
																								 @RequestParam String dataLimiteInicial,
																								 @RequestParam String dataLimiteFinal,
																								 @RequestParam String dataFatalInicial,
																								 @RequestParam String dataFatalFinal,
																								 @RequestParam String importante,
																								 @RequestParam String urgente,
																								 @RequestParam long classificacao,
																								 @RequestParam long ordem,
																								 @RequestParam long usuario) {

		FiltroHistoricoAtividadeFase filtro = new FiltroHistoricoAtividadeFase();
		filtro.setProcesso(processo);
		filtro.setFase(fase);
		filtro.setStatus(status);
		filtro.setDataLimiteInicial(dataLimiteInicial);
		filtro.setDataLimiteFinal(dataLimiteFinal);
		filtro.setDataFatalInicial(dataFatalInicial);
		filtro.setDataFatalFinal(dataFatalFinal);
		filtro.setImportante(importante);
		filtro.setUrgente(urgente);
		filtro.setClassificacao(classificacao);
		filtro.setOrdem(ordem);

		List<HistoricoFaseProcessualViewDto> dtos = atividadeService.consultarHistoricoFaseProcssual(filtro,usuario);
		return new ResponseEntity<List<HistoricoFaseProcessualViewDto>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/grafico-fase/{processo}")
	public ResponseEntity<List<GraficoAtividadeFaseDto>> graficoFase(@PathVariable long processo) {
		List<GraficoAtividadeFaseDto> dtos = atividadeService.graficoFase(processo);
		return new ResponseEntity<List<GraficoAtividadeFaseDto>>(dtos,HttpStatus.OK);
	}

	@GetMapping("/grafico-status/{processo}")
	public ResponseEntity<List<GraficoAtividadesStatusDto>> graficoStatus(@PathVariable long processo) {
		List<GraficoAtividadesStatusDto> dtos = atividadeService.graficoStatus(processo);
		return new ResponseEntity<List<GraficoAtividadesStatusDto>>(dtos,HttpStatus.OK);
	}

	@GetMapping("/dias-calendario")
	public ResponseEntity<List<DiasCalendario>> diasCalendario(@RequestParam long mes, @RequestParam long ano) {
		List<DiasCalendario> dias = atividadeService.diasCalendario(mes,ano);
		return new ResponseEntity<List<DiasCalendario>>(dias, HttpStatus.OK);
	}

	@GetMapping("/consultar-atividade-custom/{codigo}/{empresa}/{usuario}")
	public ResponseEntity<AtividadeCustomDto> consultarAtividadeCustom(@PathVariable long codigo, @PathVariable long empresa, @PathVariable long usuario) {
		AtividadeCustomDto dto = atividadeService.consultarAtiviadeCustom(codigo,empresa,usuario);
		return new ResponseEntity<AtividadeCustomDto>(dto, HttpStatus.OK);
	}
}