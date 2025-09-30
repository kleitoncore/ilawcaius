package com.br.ilawgestao.domains.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.ArquivoAtividade;
import com.br.ilawgestao.domains.models.ArquivoAtividadeTemp;
import com.br.ilawgestao.domains.models.ArquivoProcesso;
import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.ArquivoAtividadeRepository;
import com.br.ilawgestao.domains.repository.ArquivoAtividadeTempRepository;
import com.br.ilawgestao.domains.repository.ArquivoProcessoRepository;
import com.br.ilawgestao.domains.repository.AtividadeRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class ArquivoAtividadeService {
	
	@Autowired
	private ArquivoAtividadeTempRepository arquivoTemp;
	
	@Autowired
	private ArquivoAtividadeRepository arquivoAtividadeRepository;
	
	@Autowired
	private ArquivoProcessoRepository arquivoProcessoRepository;
	
	@Autowired
	private AtividadeRepository atividadeRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//Arquivos Temporários
	
	public ArquivoAtividadeTemp incluirArquivoTemp(MultipartFile file, String usuario) {
		Optional<Usuario> usuarioConsulta = usuarioRepository.findById(Long.parseLong(usuario));
		if(!usuarioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		ArquivoAtividadeTemp temp = new ArquivoAtividadeTemp();
		temp.setArquivo(file.getOriginalFilename());
		temp.setDescricao(file.getOriginalFilename());
		temp.setTipo(file.getContentType());
		temp.setUsuario(usuarioConsulta.get());
		try {
			temp.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return arquivoTemp.save(temp);
	}
	
	public List<ArquivoAtividadeTemp> listarArquivosTemp(long usuario) {
		Optional<Usuario> usuarioConsulta = usuarioRepository.findById(usuario);
		if(!usuarioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		return arquivoTemp.findByUsuarioCodigoOrderByArquivo(usuarioConsulta.get().getCodigo());
	}
	
	public ArquivoAtividadeTemp consultarArquivoTemp(long codigo) {
		Optional<ArquivoAtividadeTemp> arquivo = arquivoTemp.findById(codigo);
		if(!arquivo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Arquivo não encontrado");
		}
		
		return arquivo.get();
	}
	
	public void excluirArquivosTemp(long usuario) {
		List<ArquivoAtividadeTemp> arquivos = this.listarArquivosTemp(usuario);
		if(arquivos != null) {
			for(ArquivoAtividadeTemp arq : arquivos) {
				arquivoTemp.deleteById(arq.getCodigo());
			}
		}
	}
	
	public void excluirArquivoTemp(long codigo) {
		arquivoTemp.deleteById(codigo);
	}
	
	public ArquivoAtividadeTemp adicionarDescricaoTemp(long codigo, ArquivoAtividadeTemp arquivo) {
		ArquivoAtividadeTemp arquivoConsulta = this.consultarArquivoTemp(codigo);
		BeanUtils.copyProperties(arquivo, arquivoConsulta,"codigo","arquivo", "file","tipo","usuario");
		return arquivoTemp.save(arquivoConsulta);
	}
	
	//Arquivos definitivos
	public ArquivoAtividade incluirArquivo(MultipartFile file, String atividade, String usuario) {
		Optional<Atividade> atividadeConsulta = atividadeRepository.findById(Long.parseLong(atividade));
		if(!atividadeConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Atividade não encontrada");
		}
		
		Optional<Usuario> usuarioConsulta = usuarioRepository.findById(Long.parseLong(usuario));
		if(!usuarioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		ArquivoAtividade arquivo = new ArquivoAtividade();
		arquivo.setArquivo(file.getOriginalFilename());
		arquivo.setDsArquivo(file.getOriginalFilename());
		arquivo.setTipo(file.getContentType());
		try {
			arquivo.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		arquivo.setDtArquivo(DatasUtil.getDataAtual());
		arquivo.setAtividade(atividadeConsulta.get());
		arquivo.setUsuario(usuarioConsulta.get());
		ArquivoAtividade arquivoSalvo = arquivoAtividadeRepository.save(arquivo);
		
		//Verifica se existe processo
		if(atividadeConsulta.get().getProcesso() != null) {
			ArquivoProcesso arquivoProcesso = new ArquivoProcesso();
			arquivoProcesso.setNome(file.getOriginalFilename());
			arquivoProcesso.setDsArquivo(file.getOriginalFilename());
			arquivoProcesso.setTipo(file.getContentType());
			try {
				arquivoProcesso.setFile(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			arquivoProcesso.setDataArquivo(DatasUtil.getDataAtual());
			arquivoProcesso.setProcesso(atividadeConsulta.get().getProcesso());
			arquivoProcesso.setUsuario(usuarioConsulta.get());
			arquivoProcessoRepository.save(arquivoProcesso);
		}
		
		return arquivoSalvo;
		
	}
	
	public List<ArquivoAtividade> listarArquivos(long atividade) {
		Optional<Atividade> atividadeConsulta = atividadeRepository.findById(atividade);
		if(!atividadeConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Atividade não encontrada");
		}
		
		return arquivoAtividadeRepository.findByAtividadeCodigoOrderByArquivo(atividade);
	}
	
	public ArquivoAtividade consultarArquivo(long codigo) {
		Optional<ArquivoAtividade> arquivo = arquivoAtividadeRepository.findById(codigo);
		if(!arquivo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Arquivo não encontrado");
		}
		
		return arquivo.get();
	}
	
	public ArquivoAtividade adicionarDescricao(long codigo, ArquivoAtividade arquivo) {
		ArquivoAtividade arquivoConsulta = this.consultarArquivo(codigo);
		BeanUtils.copyProperties(arquivo, arquivoConsulta,"codigo","atividade","arquivo","dtArquivo","tipo","file","usuario");
		return arquivoAtividadeRepository.save(arquivoConsulta);
	}
	
	public void excluirArquivo(long codigo) {
		arquivoAtividadeRepository.deleteById(codigo);
	}
	
}
