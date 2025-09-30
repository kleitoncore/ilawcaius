package com.br.ilawgestao.domains.repository.filtros;

import com.br.ilawgestao.domains.models.Empresa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.StatusProcessual;
import com.br.ilawgestao.domains.models.TipoAcao;
import com.br.ilawgestao.domains.models.TipoDecisao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroProcesso {
	
	private String pessoa;
	private GrupoTrabalho grupoTrabalho;
	private TipoAcao tipoAcao;
	private StatusProcessual statusProcessual;
	private TipoDecisao tipoDecisao;
	private Empresa empresa;
	private String pasta;
	private String numeroProcesso;
	private String numeroCnj;
	private String importanciaParaMim;
	private String importanciaParaEmpresa;
	private String estrategico;
	private String usuario;
	
	public FiltroProcesso() {
		this.pessoa = "";
		this.grupoTrabalho = null;
		this.tipoAcao = null;
		this.statusProcessual = null;
		this.tipoDecisao = null;
		this.empresa = null;
		this.pasta = "";
		this.numeroProcesso = "";
		this.numeroCnj = "";
		this.importanciaParaMim = "";
		this.importanciaParaEmpresa = "";
		this.estrategico = "";
		this.usuario = "";
	}
	
	public FiltroProcesso(String pessoa, GrupoTrabalho grupoTrabalho, TipoAcao tipoAcao,
			StatusProcessual statusProcessual, TipoDecisao tipoDecisao, Empresa empresa, String pasta,
			String numeroProcesso, String numeroCnj, String importanteParaMim, String importanciaParaEmpresa, String estrategico, String usuario) {
		this.pessoa = pessoa;
		this.grupoTrabalho = grupoTrabalho;
		this.tipoAcao = tipoAcao;
		this.statusProcessual = statusProcessual;
		this.tipoDecisao = tipoDecisao;
		this.empresa = empresa;
		this.pasta = pasta;
		this.numeroProcesso = numeroProcesso;
		this.numeroCnj = numeroCnj;
		this.importanciaParaMim = importanteParaMim;
		this.importanciaParaEmpresa = importanciaParaEmpresa;
		this.estrategico = estrategico;
		this.usuario = usuario;
	}
}
