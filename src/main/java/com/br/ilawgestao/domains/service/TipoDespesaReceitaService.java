package com.br.ilawgestao.domains.service;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.exception.EntidadeEmUsoException;
import com.br.ilawgestao.domains.exception.EntidadeJaCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoCadastradaException;
import com.br.ilawgestao.domains.exception.EntidadeNaoEncontradaException;
import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.TipoDespesaReceita;
import com.br.ilawgestao.domains.repository.EmpresaRepository;
import com.br.ilawgestao.domains.repository.TipoDespesaReceitaRepository;

@Service
public class TipoDespesaReceitaService {
	
	@Autowired
	private TipoDespesaReceitaRepository tipoRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private Color gerarCorAleatoriamente(){  
       Random randColor = new Random();   
       int r = randColor.nextInt(256);  
       int g = randColor.nextInt(256);  
       int b = randColor.nextInt(256);  
       return new Color(r, g, b);  
	}
		
	private String gerarCorHexadecimal(Color color){  
       return '#'+  
        this.tratarHexString(Integer.toHexString(color.getRed()))+  
        this.tratarHexString(Integer.toHexString(color.getGreen()))+  
        this.tratarHexString(Integer.toHexString(color.getBlue()));  
	}  
		
		
	private String tratarHexString(String hexString){  
      String hex = null;  
       if(hexString.length() == 1){  
            hex = '0'+hexString;  
       }else{  
            hex = hexString;  
       }  
       return hex;  
	 }  
	
	public TipoDespesaReceita cadastrarTipoDespesaReceita(TipoDespesaReceita tipo) {
		Optional<Empresa> empresa = empresaRepository.findById(tipo.getEmpresa().getCodigo());
		if(!empresa.isPresent()) {
			throw new EntidadeNaoEncontradaException("Empresa não encontrada");
		}
		
		tipo.setCor(this.gerarCorHexadecimal(this.gerarCorAleatoriamente()));
		
		return tipoRepository.save(tipo);
	}
	
	public List<TipoDespesaReceita> listarTiposDespesasReceitas(long empresa) {
		List<TipoDespesaReceita> tipos = new ArrayList<TipoDespesaReceita>();
		List<TipoDespesaReceita> lista = tipoRepository.findByEmpresaCodigoOrderByNome(empresa);
		for(TipoDespesaReceita tip : lista) {
			TipoDespesaReceita t = new TipoDespesaReceita();
			if(tip.getTipo().equals("R")) {
				t.setTipoCalculado("Receita");
			} else {
				t.setTipoCalculado("Despesa");
			}
			
			t.setCodigo(tip.getCodigo());
			t.setNome(tip.getNome());
			t.setEmpresa(tip.getEmpresa());
			t.setTipo(tip.getTipo());
			tipos.add(t);
		}
		
		return tipos;
	}
	
	public TipoDespesaReceita consultarTipo(long codigo) {
		Optional<TipoDespesaReceita> tipo = tipoRepository.findById(codigo);
		if(!tipo.isPresent()) {
			throw new EntidadeNaoCadastradaException("Tipo de Despesa ou receita não castrada");
		}
		
		return tipo.get();
	}
	
	public TipoDespesaReceita alterarTipoDespesaReceita(long codigo, TipoDespesaReceita tipo) {
		TipoDespesaReceita tipoConsulta = this.consultarTipo(codigo); 
		Optional<TipoDespesaReceita> tipoNome = tipoRepository.findByNomeAndEmpresaCodigo(tipo.getNome(), tipo.getEmpresa().getCodigo());
		if(tipoNome.isPresent() && tipoConsulta.getCodigo() != tipoNome.get().getCodigo()) {
			throw new EntidadeJaCadastradaException("Tipo de Despesa ou receita já cadastrao com este nome");
		}
		
		BeanUtils.copyProperties(tipo, tipoConsulta,"codigo", "empresa");
		return tipoRepository.save(tipoConsulta);
	}
	
	public void excluirTipoDespesaReceita(long codigo) {
		TipoDespesaReceita tipo = this.consultarTipo(codigo);
		try {
			tipoRepository.delete(tipo);
		} catch (DataIntegrityViolationException e ) {
			throw new EntidadeEmUsoException("Tipo de Despesa ou Receita não pode ser excluído, já está em uso");
		}
	}
	
	public List<TipoDespesaReceita> listarTiposDespesasReceitasPorTipo(long empresa, String tipo) {
		return tipoRepository.findByEmpresaCodigoAndTipoOrderByNome(empresa, tipo);
	}
}
