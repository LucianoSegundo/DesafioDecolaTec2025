package com.LSsoftware.ProjetoDecolaTec.Controller.dto;

import java.time.Instant;

public record ComentarioResponse(
        Long idComentario,
        Long idUsuario,
        Long idPostagem,
        String conteudo,
        Instant dataCriacao
) {}
