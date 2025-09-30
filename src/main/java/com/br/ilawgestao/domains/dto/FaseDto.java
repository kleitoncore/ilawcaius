package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.Fase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaseDto {
	private long codigo;
	private String nome;
	private Empresa empresa;
	private String cor;
	@Transient
	private String cor2;
	private String altera;
	
	public static FaseDto build(Fase fase) {
		FaseDto dto = new FaseDto();
		dto.setCodigo(fase.getCodigo());
		dto.setNome(fase.getNome());
		dto.setEmpresa(fase.getEmpresa());
		dto.setCor(fase.getCor());
		dto.setAltera(fase.getAltera());
		if(fase.getCor() != null) {
			dto.setCor2(fase.getCor().replace("#", ""));
		} else {
			dto.setCor2(null);
		}
		return dto;
	}
}
