package com.br.ilawgestao.domains.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.br.ilawgestao.domains.dto.*;
import com.br.ilawgestao.domains.models.*;
import com.br.ilawgestao.domains.repository.*;
import com.br.ilawgestao.domains.tipb.TextFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.repository.filtros.FiltroMovimentacaoProcessualRelatorio;
import com.br.ilawgestao.domains.utils.DatasUtil;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class PushService {
	
	@Autowired
	private MovimentoProcessualRelatorioRepository movimentoProcessualRepository;

	@Autowired
	private ControleArquivoPushRepository controleArquivoPushRepository;

	@Autowired
	private LinhaMovimentacaoProcessualRepository linhaMovimentacaoProcessualRepository;

	@Autowired
	private ProcessoRepository processoRepository;

	@Autowired
	private MovimentoPushRepository movimentoPushRepository;

	@Autowired
	private MovimentoPushFimRepository movimentoPushFimRepository;

	@Autowired
	private HistoricoProcessoService historicoProcessoService;

	@Autowired
	private TipoAndamentoProcessualRepository tipoAndamentoProcessualRepository;

	private final String DIR_ARQUIVOS_TJPB = "/usr/local/arquivos_tjpb_ilawnovo/";
	private final String DIR_ARQUIVOS_TJPB_SAIDA = "/usr/local/arquivos_tjpb_ilawnovo/saida/";
	
	public int quantidadePushDia(long empresa, String dataCarregamento) {
		List<MovimentoProcessualRelatorio> movimentacoes = movimentoProcessualRepository.findByCdempresaAndDtCarregamentoOrderByCodigoDesc(empresa, dataCarregamento);
		return movimentacoes.size();
	}
	
	public List<MovimentoProcessualRelatorioDto> consultarMovimentos(FiltroMovimentacaoProcessualRelatorio filtro) {
		List<MovimentoProcessualRelatorioDto> movimentos = new ArrayList<MovimentoProcessualRelatorioDto>();
		List<MovimentoProcessualRelatorio> lista = movimentoProcessualRepository.consultarMovimentoProcessual(filtro);
		if(lista != null) {
			for(MovimentoProcessualRelatorio mov : lista) {
				MovimentoProcessualRelatorioDto dto = new MovimentoProcessualRelatorioDto();
				dto.setCodigo(mov.getCodigo());
				dto.setCdprocesso(mov.getCdprocesso());
				dto.setNrcnj(mov.getNrcnj());
				dto.setNrpasta(mov.getNrpasta());
				dto.setAutor(mov.getAutor());
				dto.setReu(mov.getReu());
				dto.setNosituacao(mov.getNosituacao());
				dto.setDtMovimentacao(DatasUtil.formatarDataTela(mov.getDtMovimentacao()));
				dto.setDsmovimentacao(mov.getDsmovimentacao());
				dto.setCdgrupo(mov.getCdgrupo());
				dto.setNogrupo(mov.getNogrupo());
				dto.setCdempresa(mov.getCdempresa());
				dto.setSnEnviou(mov.getSnEnviou());
				dto.setSnOculto(mov.getSnOculto());
				dto.setDtCarregamento(mov.getDtCarregamento());
				dto.setTemHistorico(mov.getTemHistorico());
				try {
					HistoricoProcesso historico = movimentoProcessualRepository.consultaUltimoHistorico(mov.getCdprocesso());
					dto.setUltimaMovimentacao(historico.getHistorico());
					dto.setDtUltimaMovimentacao(DatasUtil.formatarDataTela(historico.getDataHistorico()));
				} catch (Exception e) {
					dto.setUltimaMovimentacao(null);
					dto.setDtUltimaMovimentacao(null);
				}
				dto.setDataRegistroHistorico(mov.getDtMovimentacao());
				movimentos.add(dto);
			}
		}
		
		return movimentos;
	}
	public MovimentoProcessualRelatorio atualizaMovimento(long codigo, MovimentoProcessualRelatorio movimento) {
		Optional<MovimentoProcessualRelatorio> movimentoConsulta = movimentoProcessualRepository.findById(codigo);
		if(!movimentoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Movimento não encontrado"); 
		}
		
		BeanUtils.copyProperties(movimento, movimentoConsulta.get(),"codigo","cdprocesso","nrcnj","nrpasta","autor","reu",
				"nosituacao","dtMovimentacao","dsmovimentacao","cdgrupo","nogrupo","cdempresa","snEnviou",
				"dtCarregamento");
		
		return movimentoProcessualRepository.save(movimentoConsulta.get());
	}
	public void incluirArquivosPush(MultipartFile file) {
		try {
			// Verifica se o diretório de upload existe, senão, cria
			Path uploadPath = Paths.get(this.DIR_ARQUIVOS_TJPB);
			// Salva o arquivo no diretório de upload
			String fileName = file.getOriginalFilename();
			assert fileName != null;
			Path filePath = Paths.get(this.DIR_ARQUIVOS_TJPB, fileName);
			File dest = filePath.toFile();
			file.transferTo(dest);

			ControleArquivoPush push = ControleArquivoPush.builder()
					.arquivo(file.getOriginalFilename())
					.dataRegistro(DatasUtil.getDataAtual())
					.status("N")
					.build();
			controleArquivoPushRepository.save(push);
		} catch (IOException e) {
			e.printStackTrace();
			throw new EntidadeNaoEncontradaException("Falha ao fazer o upload de arquivo.");
		}
	}

	public List<ControleArquivoPushDTO> listarArquivosNaoProcessados(String status) {
		List<ControleArquivoPush> entitys = controleArquivoPushRepository.findByStatus("N");
		List<ControleArquivoPushDTO> dtos = new ArrayList<ControleArquivoPushDTO>();
		if(entitys != null && entitys.size() > 0) {
			for(ControleArquivoPush arq : entitys) {
				dtos.add(ControleArquivoPushDTO.build(arq));
			}
		}

		return dtos;
	}

	public void processarArquivoTJPB(String dataProcessamento) throws Exception {
		log.info("Carregando os arquivos para serem lidos e processados");
		List<ControleArquivoPush> arquivos = controleArquivoPushRepository.findByStatus("N");
		if(arquivos != null && arquivos.size() > 0) {
			String strArquivo = "";
			for(ControleArquivoPush arquivo: arquivos) {
				strArquivo = this.DIR_ARQUIVOS_TJPB + arquivo.getArquivo();
				log.info("Iniciando o processamento do arquivo: " + arquivo.getArquivo());
				this.lerArquivo(strArquivo,arquivo.getArquivo());
				log.info("Finalizou o processamento do arquivo");
				this.alterarStatusArquivo(arquivo.getArquivo());
				log.info("Alterou o status");
				List<LinhaMovimentacaoProcessual> movimentos = linhaMovimentacaoProcessualRepository.findByIdArquivoMovimentoProcessual(arquivo.getArquivo());
				log.info("Iniciando distribuição dos processos por empresa");
				this.distribuicaoProcessoEmpresa(movimentos,DatasUtil.formatarDataBanco(dataProcessamento));
				log.info("Finalizou processamento do arquivo " + arquivo.getArquivo());
			}
		}

		log.info("Preparando consulta para incluir os movimentos já processados e organizados");
		List<MovimentoProcessualEmpresaDTO> movimentosEmpresa =
				movimentoProcessualRepository.processosMovimentosPorGrupoEmpresa(DatasUtil.formatarDataBanco(dataProcessamento));

		if(movimentosEmpresa != null && movimentosEmpresa.size() > 0) {
			for(MovimentoProcessualEmpresaDTO movimento : movimentosEmpresa) {
				MovimentoPushFim movPushFim = MovimentoPushFim.builder()
						.codigoProcesso(movimento.getCodigoProcesso())
						.nrCnj(movimento.getNrCnj())
						.pasta(movimento.getNrPasta())
						.autor(movimento.getAutor())
						.reu(movimento.getReu())
						.statusProcessual(movimento.getStatusProcessual())
						.dtMovimentacao(movimento.getDtMovimentacao().toString())
						.movimentacao(movimento.getMovimentacao())
						.codigoGrupo(movimento.getCodigoGrupo())
						.nomeGrupo(movimento.getNomeGrupo())
						.codigoEmpresa(movimento.getCodEmpresa())
						.snEnviou("N")
						.dataCarregamento(DatasUtil.getDataAtual())
						.build();
				MovimentoPushFim movimentoSalvo = movimentoPushFimRepository.save(movPushFim);
				log.info("Incluiu movimento para empresa " + movimento.getCodEmpresa() + " processo: " + movimento.getNrCnj());
				//Incluir no Relatório para o Usuário
				if(movimentoSalvo != null) {
					MovimentoProcessualRelatorio mpr = MovimentoProcessualRelatorio.builder()
							.cdprocesso(movimento.getCodigoProcesso())
							.nrcnj(movimento.getNrCnj())
							.nrpasta(movimento.getNrPasta())
							.autor(movimento.getAutor())
							.reu(movimento.getReu())
							.nosituacao(movimento.getStatusProcessual())
							.dtMovimentacao(movimento.getDtMovimentacao().toString())
							.dsmovimentacao(movimento.getMovimentacao())
							.cdgrupo(movimento.getCodigoGrupo())
							.nogrupo(movimento.getNomeGrupo())
							.cdempresa(movimento.getCodEmpresa())
							.snEnviou("N")
							.dtCarregamento(DatasUtil.getDataAtual())
							.temHistorico(movimento.getSnHistorico())
							.snOculto("N")
							.build();
					MovimentoProcessualRelatorio mprSalvo = movimentoProcessualRepository.save(mpr);
					if(mprSalvo != null) {
						if(mprSalvo.getTemHistorico().equals("S")) {
							this.incluirHistoricoProcesso(mprSalvo);
						}
					}
				}
			}
			log.info("Finalizou todo o processamento");
		}
	}

	private void incluirHistoricoProcesso(MovimentoProcessualRelatorio mov) {
		Optional<TipoAndamentoProcessual> andamento = tipoAndamentoProcessualRepository.findByNomeAndEmpresaCodigo("Push",mov.getCdempresa());
		if(!andamento.isPresent()) {
			throw new EntidadeNaoEncontradaException("Tipo de Andamento Processual Push não encontrado");
		}

		HistoricoProcessoDto dto = new HistoricoProcessoDto();
		dto.setHistorico(mov.getDsmovimentacao());
		Processo processo = new Processo();
		processo.setCodigo(mov.getCdprocesso());
		dto.setProcesso(processo);
		Usuario usuario = new Usuario();
		usuario.setCodigo(185l);
		dto.setUsuario(usuario);
		dto.setDataHistorico(DatasUtil.getDataAtual());
		dto.setDataOcorrencia(mov.getDtMovimentacao());
		dto.setTipoAndamento(andamento.get());
		historicoProcessoService.incluirHistorico(dto);
	}

	private void lerArquivo(String arq, String idArquivo) throws Exception {
		TextFile arquivo = new TextFile(arq);
		String[] vDados;

		try {

			arquivo.openTextFile();
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			SimpleDateFormat formatoLinha = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formato3 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			SimpleDateFormat formatoLinha2 = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatoLinha3 = new SimpleDateFormat("dd/MM/yyyy hh:mm");

			if (arquivo.getLinhaArquivo().ready()) {

				String linhaLida = null;
				Pattern pattern = Pattern.compile("[a-zA-Z]");
				String dhMovimentacaoAnterior = null;

				while (arquivo.next()) {
					linhaLida = arquivo.readLine();
					vDados = linhaLida.split("[;]");
					LinhaMovimentacaoProcessual linha = LinhaMovimentacaoProcessual.builder()
							.numeroProcessoCNJ(retirarFormatoTexto(vDados[0]))
							.numeroAntigoProcesso(retirarFormatoTexto(vDados[1]))
							.dsClasseProcessual(retirarFormatoTexto(vDados[2]))
							.dsMovimentacao(retirarFormatoAspas(vDados[5]))
							.idArquivoMovimentoProcessual(idArquivo)
							.build();
					linhaMovimentacaoProcessualRepository.save(linha);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void alterarStatusArquivo(String arquivo) {
		Optional<ControleArquivoPush> arq = controleArquivoPushRepository.findByArquivo(arquivo);
		ControleArquivoPush push = ControleArquivoPush.builder()
				.codigo(arq.get().getCodigo())
				.arquivo(arq.get().getArquivo())
				.dataRegistro(arq.get().getDataRegistro())
				.status("S")
				.build();

		controleArquivoPushRepository.save(push);
	}

	private void distribuicaoProcessoEmpresa(List<LinhaMovimentacaoProcessual> movimentos, String dataMovimentacao) {
		for(LinhaMovimentacaoProcessual mov : movimentos) {
			List<Processo> processos = processoRepository.findByNrCnjAndStatus(mov.getNumeroProcessoCNJ(),0);
			if(processos != null && processos.size() > 0) {
				Empresa empresa = null;
				for(Processo proConsulta : processos) {
					empresa = proConsulta.getEmpresa();
					break;
				}

				MovimentoPush movPush = MovimentoPush.builder()
						.cnj(mov.getNumeroProcessoCNJ())
						.processo(mov.getNumeroAntigoProcesso())
						.dsMovimento(mov.getDsMovimentacao())
						.dtMovimentacao(dataMovimentacao)
						.codEmpresa(empresa.getCodigo())
						.build();
				movimentoPushRepository.save(movPush);
			}
		}
	}

	private static String retirarFormatoTexto(String texto) {
		try {
			if (texto == null)
				texto = "";
			else
				texto = texto.replaceAll("[-.,/()!?']", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return texto;
	}

	private static String retirarFormatoAspas(String texto) {
		try {
			if (texto == null)
				texto = "";
			else
				texto = texto.replaceAll("[']", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return texto;
	}
}
