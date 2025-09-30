package com.br.ilawgestao.domains.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
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

import com.br.ilawgestao.domains.dto.RelatorioAtividadesDto;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.AtividadeUsuario;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Parametro;
import com.br.ilawgestao.domains.models.Partes;
import com.br.ilawgestao.domains.models.RelatorioGerado;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.AtividadeRepository;
import com.br.ilawgestao.domains.repository.AtividadeUsuarioRepository;
import com.br.ilawgestao.domains.repository.GrupoTrabalhoRepository;
import com.br.ilawgestao.domains.repository.ParametroRepository;
import com.br.ilawgestao.domains.repository.PartesRepository;
import com.br.ilawgestao.domains.repository.RelatorioGeradoRepository;
import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioAtividades;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class RelatorioAtividadeService {
	
	@Autowired
	private AtividadeRepository atividadeRepository;
	
	@Autowired
	private GrupoTrabalhoRepository grupoTrabalhoRepository;
	
	@Autowired
	private PartesRepository partesRepository;
	
	@Autowired
	private AtividadeUsuarioRepository atividadeUsuarioRepository;
	
	@Autowired
	private ParametroRepository parametroRepository;
	
	@Autowired
	private RelatorioGeradoRepository relatorioBancoRepository;

	
	public List<RelatorioAtividadesDto> relatorioAtividade(FiltroRelatorioAtividades filtro) {
		List<RelatorioAtividadesDto> dtos = new ArrayList<RelatorioAtividadesDto>();
		List<Atividade> atividades = atividadeRepository.relatorioAtividades(filtro);
		if(atividades != null && atividades.size() > 0) {
			for(Atividade atividade : atividades) {
				RelatorioAtividadesDto dto = new RelatorioAtividadesDto();
				dto.setCodigo(atividade.getCodigo());
				dto.setTitulo(atividade.getTitulo());
				dto.setDescricao(atividade.getDescricao());
				dto.setSubGrupo(atividade.getSubGrupo().getNome());
				dto.setGrupo(this.retornaGrupoTrabalho(atividade.getSubGrupo().getGrupoPai()));
				String status = "";

				dto.setStatus(atividade.getStatus().getStatus());

				dto.setDataCadastro(DatasUtil.formatarDataTela(atividade.getDtRegistro()));
				if(atividade.getTipo().equals("A")) {
					dto.setDataLimite(DatasUtil.formatarDataHoraTela(atividade.getDtLimite()));
				} else {
					dto.setDataLimite(DatasUtil.formatarDataTela(atividade.getDtLimite()));
				}
				if(atividade.getDtFatal() != null) {
					dto.setDataFatal(DatasUtil.formatarDataTela(atividade.getDtFatal()));
				}
				if(atividade.getDtConcluido() != null) {
					dto.setDataConcluido(DatasUtil.formatarDataTela(atividade.getDtConcluido()));
				}
				//dto.setStatus(this.retornaStatus(atividade.getStatus().getCodigo()));
				if(atividade.getProcesso() != null) {
					if(atividade.getProcesso().getNrCnj() != "" && atividade.getProcesso().getNrCnj() != null) {
						dto.setNumerocnj(this.mascaraProcessoCnj(atividade.getProcesso().getNrCnj()));
					} else {
						dto.setNumerocnj(null);
					}
					dto.setNumeroProcesso(atividade.getProcesso().getNrProcesso());
					dto.setNumeroPasta(atividade.getProcesso().getPasta());
					dto.setAutor(this.retornaAutor(atividade.getProcesso().getCodigo()));
					dto.setReu(this.retornaReu(atividade.getProcesso().getCodigo()));
				}
				dto.setResponsavel(this.retornaUsuariosAtividade(atividade.getCodigo(), "R"));
				dto.setInteressado(this.retornaUsuariosAtividade(atividade.getCodigo(), "I"));
				if(atividade.getTipo().equals("T")) {
					dto.setTipoAtividade("Tarefa");
				} else {
					dto.setTipoAtividade("Compromisso/Audiência");
				}
				
				dtos.add(dto);
			}
		}
		
		return dtos;
	}
	
	@SuppressWarnings("resource")
	public RelatorioGerado exportarRelatorioAtividade(FiltroRelatorioAtividades filtro) {
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet firstSheet = workBook.createSheet("Aba1");
		FileOutputStream fos = null;
		Parametro para = parametroRepository.findById((long) 1).get();
		
		Random random = new Random();
		int numeroRelatorio = random.nextInt(1000);
		List<RelatorioAtividadesDto> relatorio = relatorioAtividade(filtro);
		String strNomeRelatorio = filtro.getUsuario() + "_" + DatasUtil.getDataAtual() + "_" + numeroRelatorio + ".xls";
		
		RelatorioGerado relatorioGerado = new RelatorioGerado();
		relatorioGerado.setRelatorio(strNomeRelatorio);
		relatorioGerado.setNomeRelatorio(strNomeRelatorio);
		Usuario usuario = new Usuario();
		usuario.setCodigo(filtro.getUsuario());
		relatorioGerado.setUsuario(usuario);
		relatorioGerado.setTipo("A");
		//Grava arquivo no banco
		relatorioGerado.setDataRelatorio(DatasUtil.getDataAtual());
		RelatorioGerado relatorioSalvo = relatorioBancoRepository.save(relatorioGerado);
		
		try {
			fos = new FileOutputStream(new File(para.getValor() + para.getComplemento() + strNomeRelatorio));
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		HSSFRow headers = firstSheet.createRow(0);
		headers.createCell(0).setCellValue("Título");
		headers.createCell(1).setCellValue("Tipo Atividade");
		headers.createCell(2).setCellValue("Descrição");
		headers.createCell(3).setCellValue("Data Limite");
		headers.createCell(4).setCellValue("Prazo Fatal");
		headers.createCell(5).setCellValue("Responsável");
		headers.createCell(7).setCellValue("Sub-Grpupo de Trabalho");
		headers.createCell(8).setCellValue("Grupo de Trabalho");
		headers.createCell(9).setCellValue("Status");
		headers.createCell(10).setCellValue("Número de CNJ");
		headers.createCell(11).setCellValue("Número de Processo");
		headers.createCell(12).setCellValue("Pasta");
		headers.createCell(13).setCellValue("Autor");
		headers.createCell(14).setCellValue("Réu");
		
		int rownum = 1;
		int cd = 0;
		for(RelatorioAtividadesDto rel : relatorio) {
			HSSFRow row = firstSheet.createRow(rownum++);
			cd = 0;
			row.createCell(cd++).setCellValue(rel.getTitulo());
			row.createCell(cd++).setCellValue(rel.getTipoAtividade());
			row.createCell(cd++).setCellValue(rel.getDescricao());
			row.createCell(cd++).setCellValue(rel.getDataLimite());
			row.createCell(cd++).setCellValue(rel.getDataFatal());
			row.createCell(cd++).setCellValue(rel.getResponsavel());
			row.createCell(cd++).setCellValue(rel.getSubGrupo());
			row.createCell(cd++).setCellValue(rel.getGrupo());
			row.createCell(cd++).setCellValue(rel.getStatus());
			row.createCell(cd++).setCellValue(rel.getNumerocnj());
			row.createCell(cd++).setCellValue(rel.getNumeroProcesso());
			row.createCell(cd++).setCellValue(rel.getNumeroPasta());
			row.createCell(cd++).setCellValue(rel.getAutor());
			row.createCell(cd++).setCellValue(rel.getReu());
		}
		
		try {
			workBook.write(fos);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		try {
			byte[] arquivo = Files.readAllBytes( Paths.get("/usr/local/digitalizacao/relatorios/usuarios/" + strNomeRelatorio));
			RelatorioGerado relatorioGeradoSalvo = this.consultarRelatorio(relatorioSalvo.getCodigo());
			relatorioGeradoSalvo.setFile(arquivo);
			relatorioBancoRepository.save(relatorioGeradoSalvo);
			//Exclui arquivo
			this.excluirArquivo("/usr/local/digitalizacao/relatorios/usuarios/" + relatorioSalvo.getNomeRelatorio());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return relatorioSalvo;
	}
	
	public List<RelatorioGerado> listaRelatorioPorUsuario(long usuario) {
		List<RelatorioGerado> lista = relatorioBancoRepository.findByusuarioCodigoAndTipoOrderByCodigoDesc(usuario,"A");
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
	
	public RelatorioGerado consultarRelatorio(long codigo) {
		return relatorioBancoRepository.findById(codigo).get();
	}
	
	public void excluirRelatorio(long codigo) {
		relatorioBancoRepository.deleteById(codigo);
	}
	
	private void excluirArquivo(String arquivo) {
		File file = new File(arquivo); 
		file.delete();
	}
	
	private String retornaGrupoTrabalho(long codigo) {
		Optional<GrupoTrabalho> grupo = grupoTrabalhoRepository.findById(codigo);
		if(grupo.isPresent()) {
			return grupo.get().getNome();
		}
		
		return null;
	}
	
	private String retornaStatus(long status) {
		String nome = "";
		switch ((int) status) {
		case 1:
			nome = "Ativo";
			break;
		case 2:
			nome = "Aguardando";
			break;
		case 3:
			nome = "Cancelada";
			break;
		case 4:
			nome = "Concluído pelo Estagiário";
			break;
		case 5:
			nome = "Concluído";
			break;
		default:
			nome = "Iniciado";
			break;
		}
		
		return nome;
	}
	
	private String mascaraProcessoCnj(String cnj) {
		String cnjMascarado = cnj.substring(0, 7);
		cnjMascarado = cnjMascarado + "-" + cnj.substring(7, 9);
		cnjMascarado = cnjMascarado + "." + cnj.substring(9, 13);
		cnjMascarado = cnjMascarado + "." + cnj.substring(13, 14);
		cnjMascarado = cnjMascarado + "." + cnj.substring(14, 16);
		cnjMascarado = cnjMascarado + "." + cnj.substring(16, 20);
		return cnjMascarado;
	}
	
	private String retornaAutor(long processo) {
		List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo);
		String autor = "";
		if(partes != null && partes.size() > 0) {
			int contador = 0;
			int tamanhoLista = partes.size();
			for(Partes autores: partes) {
				if(autores.getTipoParte().equals("A")) {
					if(contador == 0) {
						autor = autores.getPessoa().getNome();
					} else {
						if(contador == tamanhoLista) {
							autor = autor + autores.getPessoa().getNome();
						} else {
							autor = autor + autores.getPessoa().getNome() + ",";
						}
					}
				}
				
				contador++;
			}
		}
		
		return autor;
	}
	
	private String retornaReu(long processo) {
		List<Partes> partes = partesRepository.findByProcessoCodigoOrderByPessoaNomeAsc(processo);
		String reu = "";
		if(partes != null && partes.size() > 0) {
			int contador = 0;
			int tamanhoLista = partes.size();
			for(Partes reus: partes) {
				if(reus.getTipoParte().equals("R")) {
					if(contador == 0) {
						reu = reus.getPessoa().getNome();
					} else {
						if(contador == tamanhoLista) {
							reu = reu + reus.getPessoa().getNome();
						} else {
							reu = reu + reus.getPessoa().getNome() + ",";
						}
					}
				}
				
				contador++;
			}
		}
		
		return reu;
	}
	
	private String retornaUsuariosAtividade(long atividade, String tipo) {
		String nome = "";
		if(tipo.equals("R")) {
			List<AtividadeUsuario> responsaveis = atividadeUsuarioRepository.findByAtividadeCodigoAndTipoOrderByUsuarioNome(atividade, "R");
			int contador = 1;
			if(responsaveis != null && responsaveis.size() > 0) {
				for(AtividadeUsuario au : responsaveis) {
					if(contador == responsaveis.size()) {
						nome = nome + au.getUsuario().getNome();
					} else {
						nome = nome + au.getUsuario().getNome() + ",";
					}
					contador++;
				}
			}
		} else {
			List<AtividadeUsuario> interessados = atividadeUsuarioRepository.findByAtividadeCodigoAndTipoOrderByUsuarioNome(atividade, "I");
			int contador = 1;
			if(interessados != null && interessados.size() > 0) {
				for(AtividadeUsuario au : interessados) {
					if(contador == interessados.size()) {
						nome = nome + au.getUsuario().getNome();
					} else {
						nome = nome + au.getUsuario().getNome() + ",";
					}
					contador++;
				}
			}
		}
		
		return nome;
	}
}
