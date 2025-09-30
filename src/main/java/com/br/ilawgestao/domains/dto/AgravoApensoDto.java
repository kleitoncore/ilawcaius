package com.br.ilawgestao.domains.dto;

import java.util.List;

import com.br.ilawgestao.domains.models.AgravoApenso;
import com.br.ilawgestao.domains.models.Processo;
import com.br.ilawgestao.domains.models.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgravoApensoDto {
	private long codigo;
	private Processo processo;
	private Processo processoPrincipal;
	private String camara;
	private String relator;
	private List<PartesDto> partes;
	private String tipo;
	private Usuario usuario;
	
	public static AgravoApensoDto build(AgravoApenso agravo) {
		AgravoApensoDto dto = new AgravoApensoDto();
		dto.setCodigo(agravo.getCodigo());
		dto.setProcesso(agravo.getProcesso());
		dto.setProcessoPrincipal(agravo.getProcessoPrincipal());
		dto.setCamara(agravo.getCamara());
		dto.setRelator(agravo.getRelator());
		if(agravo.getTipo().equals("AG")) {
			dto.setTipo("Agravo");
		} else {
			dto.setTipo("Apenso");
		}
		return dto;
	}
	
	public static AgravoApensoDto buildAlterar(AgravoApenso agravo) {
		AgravoApensoDto dto = new AgravoApensoDto();
		dto.setCodigo(agravo.getCodigo());
		dto.setProcesso(agravo.getProcesso());
		dto.setProcessoPrincipal(agravo.getProcessoPrincipal());
		dto.setCamara(agravo.getCamara());
		dto.setRelator(agravo.getRelator());
		dto.setTipo(agravo.getTipo());
		return dto;
	}
	
	public static AgravoApensoDto buildSalvar(AgravoApenso agravo) {
		AgravoApensoDto dto = new AgravoApensoDto();
		dto.setCodigo(agravo.getCodigo());
		dto.setProcesso(agravo.getProcesso());
		dto.setProcessoPrincipal(agravo.getProcessoPrincipal());
		dto.setCamara(agravo.getCamara());
		dto.setRelator(agravo.getRelator());
		dto.setTipo(agravo.getTipo());
		return dto;
	}
}
