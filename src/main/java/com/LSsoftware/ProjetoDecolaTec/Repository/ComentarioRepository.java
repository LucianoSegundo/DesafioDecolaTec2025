package com.LSsoftware.ProjetoDecolaTec.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.LSsoftware.ProjetoDecolaTec.Entidades.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

	Optional<Page<Comentario>> findAllByPostagemId(Long postagemId, PageRequest pageRequest);

	Optional<Comentario> findByIdAndUsuarioId(Long comentarioId, Long usuarioId);

}
