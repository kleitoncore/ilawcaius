package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Partes;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.models.Processo;

import lombok.Data;

@Data
public class PartesDto {
	private long codigo;
	private Processo processo;
	private Pessoa pessoa;
	private String tipo;
	private String nomeTipo;
	private String parteNomeTipo;
	
	public static PartesDto build(Partes partes) {
		PartesDto dto = new PartesDto();
		dto.setCodigo(partes.getCodigo());
		dto.setPessoa(partes.getPessoa());
		dto.setProcesso(partes.getProcesso());
		dto.setTipo(partes.getTipoParte());
		if(partes.getTipoParte().equals("A")) {
			dto.setNomeTipo("Autor");
		} else if(partes.getTipoParte().equals("R")) {
			dto.setNomeTipo("Réu");
		} else if(partes.getTipoParte().equals("AD")) {
			dto.setNomeTipo("Advogado");
		} else if(partes.getTipoParte().equals("EM")) {
			dto.setNomeTipo("Embargante");
		} else if(partes.getTipoParte().equals("RE")) {
			dto.setNomeTipo("Reclamado");
		} else if(partes.getTipoParte().equals("TE")) {
			dto.setNomeTipo("Testemunha");
		} else if(partes.getTipoParte().equals("EN")) {
			dto.setNomeTipo("Envolvido");
		} else if(partes.getTipoParte().equals("NO")) {
			dto.setNomeTipo("Notificado");
		} else if(partes.getTipoParte().equals("NC")) {
			dto.setNomeTipo("Notificante");
		} else {
			dto.setNomeTipo("Inventariante");
		}
		
		dto.setParteNomeTipo(partes.getPessoa().getNome() + " - " + dto.getNomeTipo());
		
		return dto;
	}
}
