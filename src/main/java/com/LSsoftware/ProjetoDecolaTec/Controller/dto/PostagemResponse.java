package com.LSsoftware.ProjetoDecolaTec.Controller.dto;

import java.time.Instant;
import java.util.List;

public record PostagemResponse(
        Long id,
        String conteudo,
        Long idAutor,
        Long idForum,
        List<ComentarioResponse> comentarios,
        Instant dataCriacao
) {}