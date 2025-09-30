package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedido_processo")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoProcesso {
	
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private Long codigo;
	
	@ManyToOne
	@JoinColumn(name = "cdpedido")
	private Pedido pedido;
	
	@ManyToOne
	@JoinColumn(name = "cdprocesso")
	private Processo processo;
	
	@Column(name = "vlremoto")
	private double vlRemoto;
	
	@Column(name = "vlpossivel")
	private double vlPossivel;
	
	@Column(name = "vlprovavel")
	private double vlProvavel;
	
	@Column(name = "vlcausa")
	private double vlCausa;
	
	@Column(name = "dtregistro")
	private String dtRegistro;
}
