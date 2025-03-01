package com.LSsoftware.ProjetoDecolaTec.Controller.dto;

import java.util.Date;

public record UsuarioRequest(String nome, String senha, String email, Date dataNascimento) {

}
