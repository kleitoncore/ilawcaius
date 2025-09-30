package com.br.ilawgestao.domains.models;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "status_atividade")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusAtividade {
	
	@Column(name = "cdstatus")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Id
	private long codigo;
	
	@Column(name = "nostatus")
	private String status;

	@ManyToOne
	@JoinColumn(name = "cdempresa")
	private Empresa empresa;

	@Column(name = "cor")
	private String cor;

	@Column(name = "snaltera")
	private String altera;
}
