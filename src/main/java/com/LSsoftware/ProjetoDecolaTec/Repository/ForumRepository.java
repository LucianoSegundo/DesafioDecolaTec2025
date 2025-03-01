package com.LSsoftware.ProjetoDecolaTec.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LSsoftware.ProjetoDecolaTec.Entidades.Forum;

public interface ForumRepository extends JpaRepository<Forum, Long> {

	boolean existsByNome(String nome);

}
