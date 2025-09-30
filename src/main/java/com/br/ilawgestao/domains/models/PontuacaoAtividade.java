package com.br.ilawgestao.domains.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pontuacao_atividades")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PontuacaoAtividade {
	
	@Column(name = "cdcontrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@Column(name = "atribuidos_hoje_resp")
	private double responsavel;
	
	@Column(name = "interesses_hoje")
	private double interesse;
	
	@Column(name = "agenda_hoje_resp")
	private double agenda;
	
	@Column(name = "pontuacao")
	private double pontuacao;

}
