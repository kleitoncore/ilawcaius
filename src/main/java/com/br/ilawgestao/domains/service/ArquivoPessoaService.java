package com.br.ilawgestao.domains.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.ArquivoPessoa;
import com.br.ilawgestao.domains.models.ArquivoPessoaTemp;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.ArquivoPessoaRepository;
import com.br.ilawgestao.domains.repository.ArquivoPessoaTempRepository;
import com.br.ilawgestao.domains.repository.PessoaRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class ArquivoPessoaService {
	
	@Autowired
	private ArquivoPessoaRepository arquivoPessoaRepository;
	
	@Autowired
	private ArquivoPessoaTempRepository arquivoPessoaTempRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	
	//Todos Temporários
	public ArquivoPessoaTemp incluirArquivoTemp(MultipartFile file, String usuario) {
		ArquivoPessoaTemp temp = new ArquivoPessoaTemp();
		temp.setArquivo(file.getOriginalFilename());
		temp.setDescricao(file.getOriginalFilename());
		temp.setTipo(file.getContentType());
		Usuario usu = new Usuario();
		usu.setCodigo(Long.parseLong(usuario));
		temp.setUsuario(usu);
		try {
			temp.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arquivoPessoaTempRepository.save(temp);
	}
	
	public List<ArquivoPessoaTemp> listarArquivosTemp(long usuario) {
		return arquivoPessoaTempRepository.findByUsuarioCodigoOrderByArquivoAsc(usuario);
	}
	
	public ArquivoPessoaTemp consultarArquivoTemp(long codigo) {
		Optional<ArquivoPessoaTemp> arquivo = arquivoPessoaTempRepository.findById(codigo);
		if(!arquivo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Arquivo nnão encontrado");
		}
		
		return arquivo.get();
	}
	
	public void excluirArquivosTemp(long usuario) {
		Usuario usu = usuarioService.consultarUsuario(usuario);
		List<ArquivoPessoaTemp> arquivos = arquivoPessoaTempRepository.findByUsuarioCodigoOrderByArquivoAsc(usu.getCodigo());
		if(arquivos != null) {
			for(ArquivoPessoaTemp arq : arquivos) {
				arquivoPessoaTempRepository.deleteById(arq.getCodigo());
			}
		}
	}
	
	public void excluirArquivoTemp(long codigo) {
		arquivoPessoaTempRepository.deleteById(codigo);
	}
	
	public ArquivoPessoaTemp adicionarDescricaoTemp(long codigo, ArquivoPessoaTemp arquivo) {
		ArquivoPessoaTemp arquivoConsultado = this.consultarArquivoTemp(codigo);
		BeanUtils.copyProperties(arquivo, arquivoConsultado,"codigo","arquivo", "file","tipo","usuario");
		return arquivoPessoaTempRepository.save(arquivoConsultado);
	}
	
	//Arquivos definitivos
	public ArquivoPessoa incluirArquivo(MultipartFile file, String pessoa, String usuario) {
		Optional<Pessoa> pessoaConsulta = pessoaRepository.findById(Long.parseLong(pessoa));
		if(!pessoaConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Pessoa não encontrada");
		}
		ArquivoPessoa arquivo = new ArquivoPessoa();
		arquivo.setArquivo(file.getOriginalFilename());
		arquivo.setDescricao(file.getOriginalFilename());
		arquivo.setTipo(file.getContentType());
		Usuario usu = new Usuario();
		usu.setCodigo(Long.parseLong(usuario));
		arquivo.setUsuario(usu);
		try {
			arquivo.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		arquivo.setPessoa(pessoaConsulta.get());
		arquivo.setDtArquivo(DatasUtil.getDataAtual());
		return arquivoPessoaRepository.save(arquivo);
	}
	
	public List<ArquivoPessoa> listarArquivos(long pessoa) {
		Optional<Pessoa> pessoaConsulta = pessoaRepository.findById(pessoa);
		if(!pessoaConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Pessoa não enncontrada");
		}
		
		return arquivoPessoaRepository.findByPessoaCodigoOrderByArquivo(pessoaConsulta.get().getCodigo());
	}
	
	public ArquivoPessoa consultarArquivo(long codigo) {
		Optional<ArquivoPessoa> arquivo = arquivoPessoaRepository.findById(codigo);
		if(!arquivo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Arquivo não encontrado");
		}
		
		return arquivo.get();
	}
	
	public ArquivoPessoa adicionarDescricao(long codigo, ArquivoPessoa arquivo) {
		ArquivoPessoa arquivoConsultado = this.consultarArquivo(codigo);
		BeanUtils.copyProperties(arquivo, arquivoConsultado,"codigo","arquivo", "file","tipo","usuario","pessoa","dtArquivo");
		return arquivoPessoaRepository.save(arquivoConsultado);
	}
	
	public void excluirArquivo(long codigo) {
		arquivoPessoaRepository.deleteById(codigo);
	}
}
