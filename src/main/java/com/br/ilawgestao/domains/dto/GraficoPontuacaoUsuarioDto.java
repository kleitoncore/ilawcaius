package com.br.ilawgestao.domains.dto;

import java.util.List;

import com.br.ilawgestao.domains.service.PontuacaoProjecaoDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraficoPontuacaoUsuarioDto {
	private List<PontuacaoUsuarioDto> mesAnterior;
	private List<PontuacaoUsuarioDto> mesAtual;
	private List<PontuacaoProjecaoDto> projecao;
}
