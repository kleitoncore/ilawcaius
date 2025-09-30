package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.Fase;
import com.br.ilawgestao.domains.models.FaseTarefa;
import com.br.ilawgestao.domains.models.TituloAtividade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TituloAtividadeDto {
	private long codigo;
	private String titulo;
	private String tituloExibir;
	private Empresa empresa;
	private long pontos;
	private Fase fase;
	private String status;
	
	public static TituloAtividadeDto build(TituloAtividade titulo) {
		TituloAtividadeDto dto = new TituloAtividadeDto();
		dto.setCodigo(titulo.getCodigo());
		dto.setTitulo(titulo.getTitulo());
		dto.setEmpresa(titulo.getEmpresa());
		dto.setPontos(titulo.getPontos());
		dto.setStatus(titulo.getStatus());
		return dto;
	}
	
	public static TituloAtividadeDto buildConsulta(TituloAtividade titulo) {
		TituloAtividadeDto dto = new TituloAtividadeDto();
		dto.setCodigo(titulo.getCodigo());
		dto.setTitulo(titulo.getTitulo());
		dto.setTituloExibir(titulo.getTitulo() + " (" + titulo.getPontos() + " PTOS)");
		dto.setEmpresa(titulo.getEmpresa());
		dto.setPontos(titulo.getPontos());
		dto.setFase(titulo.getFase());
		if(titulo.getStatus().equals("A")) {
			dto.setStatus("Ativo");
		} else {
			dto.setStatus("Inativo");
		}
		return dto;
	}
}
