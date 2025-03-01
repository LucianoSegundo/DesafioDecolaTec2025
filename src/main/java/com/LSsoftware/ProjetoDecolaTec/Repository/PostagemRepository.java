package com.LSsoftware.ProjetoDecolaTec.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.LSsoftware.ProjetoDecolaTec.Entidades.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {

	Page<Postagem> findAllByForumId(Long forumId, PageRequest pageRequest);

}
