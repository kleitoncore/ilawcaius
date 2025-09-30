package com.br.ilawgestao.domains.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.ilawgestao.domains.models.AtividadeUsuario;

public interface AtividadeUsuarioRepository extends JpaRepository<AtividadeUsuario, Long> {
	List<AtividadeUsuario> findByAtividadeCodigoAndTipoOrderByUsuarioNome(long atividade, String tipo);
	Optional<AtividadeUsuario> findByAtividadeCodigoAndUsuarioCodigoAndTipo(long atividade, long usuario, String tipo);
	List<AtividadeUsuario> findByAtividadeCodigo(long atviadde);
}
