package com.br.ilawgestao.domains.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.ArquivoFinanceiro;
import com.br.ilawgestao.domains.models.ArquivoFinanceiroTemp;
import com.br.ilawgestao.domains.models.Lancamento;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.ArquivoFinanceiroRepository;
import com.br.ilawgestao.domains.repository.ArquivoFinanceiroTempRepository;
import com.br.ilawgestao.domains.repository.LancamentoRepostory;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class ArquivoFinanceiroService {
	
	@Autowired
	private ArquivoFinanceiroRepository arquivoFianceiroRepository;
	
	@Autowired
	private ArquivoFinanceiroTempRepository arquivoTempRepository;
	
	@Autowired
	private LancamentoRepostory lancamentoRepository;
	
	public ArquivoFinanceiro incluirArquivo(MultipartFile file, String lancamento, String usuario) {
		
		ArquivoFinanceiro arquivo = new ArquivoFinanceiro();
		Lancamento lanc = new Lancamento();
		lanc.setCodigo(Long.parseLong(lancamento));
		Usuario usu = new Usuario();
		usu.setCodigo(Long.parseLong(usuario));
		arquivo.setLancamento(lanc);
		arquivo.setUsuario(usu);
		arquivo.setArquivo(file.getOriginalFilename());
		arquivo.setTipo(file.getContentType());
		try {
			arquivo.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		arquivo.setDtRegistro(DatasUtil.getDataAtual());
		return arquivoFianceiroRepository.save(arquivo);
	}
	
	public ArquivoFinanceiroTemp incluirArquivoTemp(MultipartFile file, String usuario) {
		ArquivoFinanceiroTemp temp = new ArquivoFinanceiroTemp();
		temp.setArquivo(file.getOriginalFilename());
		Usuario usu = new Usuario();
		usu.setCodigo(Long.parseLong(usuario));
		temp.setUsuario(usu);
		temp.setDescricao(file.getOriginalFilename());
		temp.setTipo(file.getContentType());
		try {
			temp.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arquivoTempRepository.save(temp);
	}
	
	public List<ArquivoFinanceiroTemp> listarTemp(long usuario) {
		return arquivoTempRepository.findByUsuarioCodigo(usuario);
	}
	
	public void excluirArquivoTemp(long codigo) {
		arquivoTempRepository.deleteById(codigo);
	}
	
	public void excluirArquivosTemp(long usuario) {
		List<ArquivoFinanceiroTemp> lista = this.listarTemp(usuario);
		if(lista != null) {
			for(ArquivoFinanceiroTemp arq: lista) {
				arquivoTempRepository.deleteById(arq.getCodigo());
			}
		}
	}
	
	public ArquivoFinanceiro consultarArquivo(long codigo) {
		return arquivoFianceiroRepository.findById(codigo).get();
	}
	
	public ArquivoFinanceiroTemp consultarArquivoTemp(long codigo) {
		return arquivoTempRepository.findById(codigo).get();
	}
	
	public List<ArquivoFinanceiro> listarArquivosFinanceiro(long lancamento) {
		Optional<Lancamento> lancamentoConsulta = lancamentoRepository.findById(lancamento);
		if(!lancamentoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Lançamento não encontrado");
		}
		
		return arquivoFianceiroRepository.findByLancamentoCodigo(lancamento);
	}
	
	public void excluirArquivo(long codigo) {
		arquivoFianceiroRepository.deleteById(codigo);
	}
	
	public ArquivoFinanceiroTemp adicionarDescricaoTemp(long codigo, ArquivoFinanceiroTemp arquivo) {
		ArquivoFinanceiroTemp arquivoConsultado = this.consultarArquivoTemp(codigo);
		BeanUtils.copyProperties(arquivo, arquivoConsultado,"codigo","arquivo", "file","tipo","usuario");
		return arquivoTempRepository.save(arquivoConsultado);
	}
	
	public ArquivoFinanceiro adicionarDescricao(long codigo, ArquivoFinanceiro arquivo) {
		ArquivoFinanceiro arquivoConsultado = this.consultarArquivo(codigo);
		BeanUtils.copyProperties(arquivo, arquivoConsultado,"codigo","arquivo", "file","tipo","usuario","lancamento","dtRegistro");
		return arquivoFianceiroRepository.save(arquivoConsultado);
	}

}
