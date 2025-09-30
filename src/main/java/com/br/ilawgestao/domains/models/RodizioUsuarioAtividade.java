package com.br.ilawgestao.domains;

import com.br.ilawgestao.domains.models.GrupoTarefa;
import com.br.ilawgestao.domains.models.GrupoTrabalho;
import com.br.ilawgestao.domains.models.Tarefa;
import com.br.ilawgestao.domains.models.Usuario;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rodizio_usuario_atividade")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RodizioUsuarioAtividade {

    @Column(name = "cdcontrole")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private long codigo;

    @ManyToOne
    @JoinColumn(name = "cdgrupo_atividade")
    private GrupoTarefa grupoAtividade;

    @ManyToOne
    @JoinColumn(name = "cdatividade")
    private Tarefa atividade;

    @ManyToOne
    @JoinColumn(name = "cdgrupo_trabalho")
    private GrupoTrabalho grupoTrabalho;

    @ManyToOne
    @JoinColumn(name = "cdusuario_atividade")
    private Usuario usuarioAtividade;

    @Column(name = "posicao")
    private long posicao;

    @Column(name = "data_alteracao")
    private String dataAlteracao;
}
