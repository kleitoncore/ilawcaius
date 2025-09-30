package com.br.ilawgestao.domains.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.br.ilawgestao.domains.repository.filtros.FiltroRelatorioProcesso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "relatorio_gerado")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioGerado {
	
	@Column(name = "cdrelatorio")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@Column(name = "norelatorio")
	private String relatorio;
	
	@Column(name = "dtrelatorio")
	private String dataRelatorio;
	
	@JoinColumn(name = "cdusuario")
	@ManyToOne
	private Usuario usuario;
	
	@Lob
	@Column(name = "file")
	private byte[] file;
	
	@Column(name = "dsarquivo")
	private String descricao;
	
	@Column(name = "tprelatorio")
	private String tipo;
	
	@Transient
	private List<CamposRelatorioProcesso> campos;
	
	@Transient
	private FiltroRelatorioProcesso filtro;
	
	@Transient
	private String nomeRelatorio;
}
