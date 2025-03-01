package com.LSsoftware.ProjetoDecolaTec.Service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LSsoftware.ProjetoDecolaTec.Entidades.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByNome(String nome);

}
