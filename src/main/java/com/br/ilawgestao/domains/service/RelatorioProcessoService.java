package com.br.ilawgestao.domains.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.RelatorioProcessoDto;
import com.br.ilawgestao.domains.dto.RelatorioProcessoViewDto;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.CamposRelatorioProcesso;
import com.br.ilawgestao.domains.models.Parametro;
import com.br.ilawgestao.domains.models.RelatorioGerado;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.CamposRelatorioProcessoRepository;
import com.br.ilawgestao.domains.repository.ParametroRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.repository.RelatorioGeradoRepository;
import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioProcesso;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class RelatorioProcessoService {
	
	@Autowired
	private ProcessoRepository relatorioRepository;
	
	@Autowired
	private RelatorioGeradoRepository relatorioBancoRepository;
	
	@Autowired
	private ParametroRepository parametroRepository;
	
	@Autowired
	private CamposRelatorioProcessoRepository camposRelatorioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
		
	public List<RelatorioProcessoViewDto> consultarRelatorioProcesso(FiltroRelatorioProcesso filtro) {
		List<RelatorioProcessoDto> lista = relatorioRepository.relatorioProcesso(filtro);
		List<RelatorioProcessoViewDto> dtos = new ArrayList<RelatorioProcessoViewDto>();
		if(lista != null) {
			for(RelatorioProcessoDto rel : lista) {
				dtos.add(RelatorioProcessoViewDto.build(rel));
			}
		}
		
		return dtos;
	}
	
	@SuppressWarnings("resource")
	public RelatorioGerado exportarRelatorio(RelatorioGerado relatorioGerado) {
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet firstSheet = workBook.createSheet("Aba1");
		FileOutputStream fos = null;
		Parametro para = parametroRepository.findById((long) 1).get();
		
		Random random = new Random();
		int numeroRelatorio = random.nextInt(1000);
		
		relatorioGerado.setRelatorio(relatorioGerado.getUsuario().getCodigo() + "_" + DatasUtil.getDataAtual() + "_" + numeroRelatorio + ".xls");
		relatorioGerado.setNomeRelatorio(relatorioGerado.getUsuario().getCodigo() + "_" + DatasUtil.getDataAtual() + "_" + numeroRelatorio + ".xls");
		relatorioGerado.setTipo("P");
		//Grava arquivo no banco
		relatorioGerado.setDataRelatorio(DatasUtil.getDataAtual());
		RelatorioGerado retorno = relatorioBancoRepository.save(relatorioGerado);
			
		try {
			fos = new FileOutputStream(new File(para.getValor() + para.getComplemento() + relatorioGerado.getNomeRelatorio()));
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		HSSFRow headers = firstSheet.createRow(0);
		int c = 0;
		for(CamposRelatorioProcesso campos : relatorioGerado.getCampos()) {
			headers.createCell(c).setCellValue(campos.getNome());
			c++;
		}
		
		int i = 1;
		List<RelatorioProcessoDto> relatorio = relatorioRepository.relatorioProcesso(relatorioGerado.getFiltro());
		
		for(RelatorioProcessoDto rel : relatorio) {
			HSSFRow row = firstSheet.createRow(i);
			int cd = 0;
			for(CamposRelatorioProcesso campos : relatorioGerado.getCampos()) {
				//Código do processo
				row.createCell(cd).setCellValue(rel.getCodigo());
				//Numero de processo
				if(campos.getCodigo() == 1) {
					row.createCell(cd).setCellValue(rel.getNumeroProcesso());
				}
				//Número de CNJ
				if(campos.getCodigo() == 2) {
					row.createCell(cd).setCellValue(this.mascaraProcessoCnj(rel.getNumeroCnj()));
				}
				//Pasta
				if(campos.getCodigo() == 3) {
					row.createCell(cd).setCellValue(rel.getPasta());
				}
				//Data de Distribuição
				if(campos.getCodigo() == 4) {
					row.createCell(cd).setCellValue(rel.getDataDistribuicao());
				}
				//Data de decisão
				if(campos.getCodigo() == 5) {
					row.createCell(cd).setCellValue(rel.getDataUltimaDecisao());
				}
				//Data de Movimentação
				if(campos.getCodigo() == 6) {
					row.createCell(cd).setCellValue(rel.getDataUltimaMovimentacao());
				}
				//Data da sentença
				if(campos.getCodigo() == 7) {
					row.createCell(cd).setCellValue(rel.getDataSentenca());
				}
				//Data de Cadastro
				if(campos.getCodigo() == 8) {
					row.createCell(cd).setCellValue(rel.getDataCadastro());
				}
				//Comarca
				if(campos.getCodigo() == 9) {
					row.createCell(cd).setCellValue(rel.getComarca());
				}
				//Pedidos
				if(campos.getCodigo() == 10) {
					row.createCell(cd).setCellValue(rel.getPedidos());
				}
				//Envia Push
				if(campos.getCodigo() == 11) {
					row.createCell(cd).setCellValue(rel.getSnPush());
				}
				//Envia Email
				if(campos.getCodigo() == 12) {
					row.createCell(cd).setCellValue(rel.getSnEmail());
				}
				//Histórico
				if(campos.getCodigo() == 13) {
					row.createCell(cd).setCellValue(rel.getSnHistorico());
				}
				//Última Atividade
				if(campos.getCodigo() == 15) {
					row.createCell(cd).setCellValue(rel.getUltimaAtividade());
				}
				//Data de última atividade
				if(campos.getCodigo() == 16) {
					row.createCell(cd).setCellValue(rel.getDataUltimaAtividade());
				}
				//Último Histórico
				if(campos.getCodigo() == 17) {
					row.createCell(cd).setCellValue(rel.getUltimoHistorico());
				}
				//Data de Último Histórico
				if(campos.getCodigo() == 18) {
					row.createCell(cd).setCellValue(rel.getDataUltimoHistorico());
				}
				//Objetos de Ação
				if(campos.getCodigo() == 19) {
					row.createCell(cd).setCellValue(rel.getObjetos());
				}
				//Autores
				if(campos.getCodigo() == 20) {
					row.createCell(cd).setCellValue(rel.getAutores());
				}
				//Réus
				if(campos.getCodigo() == 21) {
					row.createCell(cd).setCellValue(rel.getReus());
				}
				//Advogados
				if(campos.getCodigo() == 22) {
					row.createCell(cd).setCellValue(rel.getAdvogados());
				}
				//Grupo de Trabalho
				if(campos.getCodigo() == 23) {
					row.createCell(cd).setCellValue(rel.getGrupoTrabalho());
				}
				//Status Processual
				if(campos.getCodigo() == 24) {
					row.createCell(cd).setCellValue(rel.getStatusProcessual());
				}
				//Tipo de Ação
				if(campos.getCodigo() == 25) {
					row.createCell(cd).setCellValue(rel.getTipoAcao());
				}
				//Tipo de Decisão
				if(campos.getCodigo() == 26) {
					row.createCell(cd).setCellValue(rel.getTipoDecisao());
				}
				//Área de Atuação
				if(campos.getCodigo() == 27) {
					row.createCell(cd).setCellValue(rel.getAreaAtuacao());
				}
				//Pagamentos
				if(campos.getCodigo() == 28) {
					row.createCell(cd).setCellValue(rel.getPagamentos());
				}
				//Valores dos pagamentos
				if(campos.getCodigo() == 29) {
					row.createCell(cd).setCellValue(rel.getValorPagamentos());
				}
				//Custas
				if(campos.getCodigo() == 30) {
					row.createCell(cd).setCellValue(rel.getCustas());
				}
				//Valores das custas
				if(campos.getCodigo() == 31) {
					row.createCell(cd).setCellValue(rel.getValorCustas());
				}
				//Valor Provável
				if(campos.getCodigo() == 32) {
					row.createCell(cd).setCellValue(rel.getValorProvavel());
				}
				//Valor Possível
				if(campos.getCodigo() == 33) {
					row.createCell(cd).setCellValue(rel.getValorPossivel());
				}
				//Valor Remoto
				if(campos.getCodigo() == 34) {
					row.createCell(cd).setCellValue(rel.getValorRemoto());
				}
				//Valor Causa
				if(campos.getCodigo() == 35) {
					row.createCell(cd).setCellValue(rel.getValorCausa());
				}
				//UF
				if(campos.getCodigo() == 36) {
					row.createCell(cd).setCellValue(rel.getUf());
				}
				//Fase
				if(campos.getCodigo() == 37) {
					row.createCell(cd).setCellValue(rel.getFase());
				}
				//Rito
				if(campos.getCodigo() == 38) {
					row.createCell(cd).setCellValue(rel.getRito());
				}
				//Motivo de Resultado
				if(campos.getCodigo() == 39) {
					row.createCell(cd).setCellValue(rel.getMotivoResultado());
				}
				//Data de Arquivamento
				if(campos.getCodigo() == 40) {
					row.createCell(cd).setCellValue(rel.getDataArquivamento());
				}
				
				//Importante para Empresa
				if(campos.getCodigo() == 41) {
					row.createCell(cd).setCellValue(rel.getImportanteEmpresa());
				}
				
				//Importante para mim
				if(campos.getCodigo() == 42) {
					row.createCell(cd).setCellValue(rel.getImportanteParaMim());
				}
				
				//Estratégico
				if(campos.getCodigo() == 43) {
					row.createCell(cd).setCellValue(rel.getEstrategico());
				}
				
				//Responsável
				if(campos.getCodigo() == 44) {
					row.createCell(cd).setCellValue(rel.getResponsavel());
				}
				
				//Observação
				if(campos.getCodigo() == 45) {
					row.createCell(cd).setCellValue(rel.getObservacao());
				}
				
				//Bancada
				if(campos.getCodigo() == 46) {
					row.createCell(cd).setCellValue(rel.getBanca());
				}
				
				cd++;
			}
			i++;
		}
		
		try {
			workBook.write(fos);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		try {
			byte[] arquivo = Files.readAllBytes( Paths.get("/usr/local/digitalizacao/relatorios/usuarios/" + retorno.getNomeRelatorio()));
			RelatorioGerado relatorioGeradoSalvo = this.consultarRelatorio(retorno.getCodigo());
			relatorioGeradoSalvo.setFile(arquivo);
			relatorioBancoRepository.save(relatorioGeradoSalvo);
			//Exclui arquivo
			this.excluirArquivo("/usr/local/digitalizacao/relatorios/usuarios/" + retorno.getNomeRelatorio());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	private void excluirArquivo(String arquivo) {
		File file = new File(arquivo); 
		file.delete();
	}
	
	public List<CamposRelatorioProcesso> listarCamposRelatorioProcesso(String codigos) {
		return camposRelatorioRepository.listarCamposRelatorio(codigos);
	}
	
	public List<RelatorioGerado> listaRelatorioPorUsuario(long usuario) {
		List<RelatorioGerado> lista = relatorioBancoRepository.findByusuarioCodigoAndTipoOrderByCodigoDesc(usuario,"P");
		List<RelatorioGerado> relatorios = new ArrayList<RelatorioGerado>();
		for(RelatorioGerado rel : lista) {
			RelatorioGerado relatorio = new RelatorioGerado();
			relatorio.setCodigo(rel.getCodigo());
			relatorio.setUsuario(rel.getUsuario());
			relatorio.setRelatorio(rel.getRelatorio());
			relatorio.setDataRelatorio(DatasUtil.formatarDataTela(rel.getDataRelatorio()));
			relatorios.add(relatorio);
		}
		
		return relatorios;
	}
	
	public void excluirRelatorio(long relatorio, long usuario) {
		Optional<RelatorioGerado> relatorioConsulta = relatorioBancoRepository.findById(relatorio);
		if(!relatorioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("O relatório que procura não foi encontrado");
		}
		relatorioBancoRepository.deleteById(relatorio);
	}
	
	public RelatorioGerado consultarRelatorio(long codigo) {
		return relatorioBancoRepository.findById(codigo).get();
	}
	
	@SuppressWarnings("unused")
	private void excluirArquivoFisico(long relatorio, long usuario, String arquivo) throws Exception {
		Usuario usuarioConsulta = usuarioService.consultarUsuario(usuario);
		Parametro parametro = parametroRepository.findById((long) 1).get();
		Path diretorio = Paths.get(parametro.getValor() + usuarioConsulta.getCodigo());
		
		try {
			File file = new File(diretorio + parametro.getComplemento() + arquivo);
			file.delete();
		} catch ( Exception e ) {
			throw new Exception("Erro ao tentar excluir arquivo físico de relatório de processo");
		}
	}
	
	private String mascaraProcessoCnj(String cnj) {
		String cnjMascarado = "";
		if(cnj != null && cnj != "" && !cnj.equals("0")) {
			cnjMascarado = cnj.substring(0, 7);
			cnjMascarado = cnjMascarado + "-" + cnj.substring(7, 9);
			cnjMascarado = cnjMascarado + "." + cnj.substring(9, 13);
			cnjMascarado = cnjMascarado + "." + cnj.substring(13, 14);
			cnjMascarado = cnjMascarado + "." + cnj.substring(14, 16);
			cnjMascarado = cnjMascarado + "." + cnj.substring(16, 20);
		}
		
		return cnjMascarado;
	}
}
