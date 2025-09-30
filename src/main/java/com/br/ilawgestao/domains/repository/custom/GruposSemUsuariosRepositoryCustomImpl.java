package com.br.ilawgestao.domains.repository.custom;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.br.ilawgestao.domains.models.GrupoTrabalho;

public class GruposSemUsuariosRepositoryCustomImpl implements GruposSemUsuariosRepositoryCustom {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<GrupoTrabalho> listarGruposSemUsuarios(long empresa, long usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("select g from GrupoTrabalho g where g.codigo = g.grupoPai and empresa.codigo =:empresa"
				+ " and g.codigo not in(select grupo.codigo from UsuarioGrupoTrabalho where usuario.codigo =:usuario) order by g.nome ");
		
		TypedQuery<GrupoTrabalho> query = manager.createQuery(sql.toString(), GrupoTrabalho.class);
		
		query.setParameter("empresa", empresa);
		query.setParameter("usuario", usuario);
		
		return query.getResultList();
	}

}
