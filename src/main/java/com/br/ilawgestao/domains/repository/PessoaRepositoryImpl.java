package com.br.ilawgestao.domains.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.br.ilawgestao.domains.models.Pessoa;
import com.br.ilawgestao.domains.repository.filtros.FiltroPessoa;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {
	
	@Autowired
	private EntityManager manager;

	@Override
	public List<Pessoa> listarPessoasForaDasPartesProcesso(long empresa, long processo, String perfil) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p from Pessoa p where codigo not in(select pessoa.codigo from Partes where processo.codigo =:codigoProcesso) and empresa.codigo =:codigoEmpresa and perfil.codigo =:perfil order by p.nome");
		TypedQuery<Pessoa> query = manager.createQuery(sql.toString(), Pessoa.class);
		query.setParameter("codigoEmpresa", empresa);
		query.setParameter("codigoProcesso", processo);
		query.setParameter("perfil", perfil);
		return query.getResultList();
	}

	@Override
	public List<Pessoa> filtrarPessoas(FiltroPessoa filtro) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p from Pessoa p where p.empresa.codigo =:codigoEmpresa ");
		
		if(filtro.getNome() != null) {
			sql.append(" and p.nome like :nome ");
		}
		
		if(filtro.getCpfcnpj() != null) {
			sql.append(" and p.cpfCnpj =:cpfCnpj ");
		}

		if(filtro.getEstado() != null) {
			sql.append(" and p.cidade in(select c.codigo from Cidade c where c.estado.codigo =:estado)");
		}

		if(filtro.getCidade() != null) {
			sql.append(" and p.cidade.codigo =:cidade ");
		}
		
		if(filtro.getTelefone() != null) {
			sql.append(" and p.telefone1 =:telefone or p.telefone2 =:telefone ");
		}
		
		if(filtro.getPorLetra() != null) {
			sql.append(" and p.nome like :letra");
		}
		
		if(filtro.getGrupo() != null) {
			sql.append(" and p.grupoCliente.codigo =:grupo ");
		}
		
		TypedQuery<Pessoa> query = manager.createQuery(sql.toString(), Pessoa.class);
		
		query.setParameter("codigoEmpresa", filtro.getEmpresa().getCodigo());
		
		if(filtro.getNome() != null) {
			query.setParameter("nome", "%" + filtro.getNome() + "%");
		}
		
		if(filtro.getCpfcnpj() != null) {
			query.setParameter("cpfCnpj", filtro.getCpfcnpj());
		}

		if(filtro.getEstado() != null) {
			query.setParameter("estado", filtro.getEstado().getCodigo());
		}
		
		if(filtro.getCidade() != null) {
			query.setParameter("cidade", filtro.getCidade().getCodigo());
		}
		
		if(filtro.getTelefone() != null) {
			query.setParameter("telefone", filtro.getTelefone());
		}
		
		if(filtro.getPorLetra() != null) {
			query.setParameter("letra", filtro.getLetra() + "%");
		}
		
		if(filtro.getGrupo() != null) {
			query.setParameter("grupo", filtro.getGrupo().getCodigo());
		}
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> consultarPessoas(String codigos) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from pessoa where cdpessoa in(" + codigos + ")");
		Query query = manager.createNativeQuery(sql.toString(), Pessoa.class);
		return query.getResultList();
	}
}
