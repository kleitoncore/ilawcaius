package com.br.ilawgestao.domains.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.ArquivoProcesso;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.repository.ArquivoProcessoRepository;
import com.br.ilawgestao.domains.repository.ProcessoRepository;
import com.br.ilawgestao.domains.repository.UsuarioRepository;
import com.br.ilawgestao.domains.utils.DatasUtil;

@Service
public class ArquivoProcessoService {
	
	@Autowired
	private ArquivoProcessoRepository arquivoProcessoRepository;
		
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
		
	public ArquivoProcesso incluirArquivo(MultipartFile file, String processo, String usuario) {
		Optional<Processo> processoConsulta = processoRepository.findById(Long.parseLong(processo));
		if(!processoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		Optional<Usuario> usuarioConsulta = usuarioRepository.findById(Long.parseLong(usuario));
		if(!usuarioConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Usuário não encontrado");
		}
		
		ArquivoProcesso arquivo = new ArquivoProcesso();
		try {
			arquivo.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Usuario usu = new Usuario();
		usu.setCodigo(usuarioConsulta.get().getCodigo());
		arquivo.setUsuario(usu);
		arquivo.setNome(file.getOriginalFilename());
		arquivo.setDsArquivo(file.getOriginalFilename());
		arquivo.setTipo(file.getContentType());
		Processo pro = new Processo();
		pro.setCodigo(processoConsulta.get().getCodigo());
		arquivo.setProcesso(pro);
		arquivo.setDataArquivo(DatasUtil.getDataAtual());
		return arquivoProcessoRepository.save(arquivo);
	}
	
	public List<ArquivoProcesso> listarArquivos(long processo) {
		Optional<Processo> processoConsulta = processoRepository.findById(processo);
		if(!processoConsulta.isPresent()) {
			throw new EntidadeNaoEncontradaException("Processo não encontrado");
		}
		
		return arquivoProcessoRepository.findByProcessoCodigoOrderByNomeAsc(processo);
	}
	
	public ArquivoProcesso consultarArquivo(long codigo) {
		Optional<ArquivoProcesso> arquivo = arquivoProcessoRepository.findById(codigo);
		if(!arquivo.isPresent()) {
			throw new EntidadeNaoEncontradaException("Arquivo não encontrado");
		}
		
		return arquivo.get();
	}
	
	public ArquivoProcesso adicionarDescricao(long codigo, ArquivoProcesso arquivo) {
		ArquivoProcesso arquivoConsulta = this.consultarArquivo(codigo);
		BeanUtils.copyProperties(arquivo, arquivoConsulta,"codigo","nome","file","tipo","usuario","processo","dataArquivo");
		return arquivoProcessoRepository.save(arquivoConsulta);
	}
	
	public void excluirArquivo(long codigo) {
		arquivoProcessoRepository.deleteById(codigo);
	}
}
