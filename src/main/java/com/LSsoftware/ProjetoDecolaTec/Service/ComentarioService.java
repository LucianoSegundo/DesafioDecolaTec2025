package com.LSsoftware.ProjetoDecolaTec.Service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LSsoftware.ProjetoDecolaTec.Controller.dto.ComentarioResponse;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Comentario;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Postagem;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Usuario;
import com.LSsoftware.ProjetoDecolaTec.Repository.ComentarioRepository;
import com.LSsoftware.ProjetoDecolaTec.Repository.PostagemRepository;
import com.LSsoftware.ProjetoDecolaTec.Repository.UsuarioRepository;
import com.LSsoftware.ProjetoDecolaTec.excessoes.CampoObrigatorioException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.EntidadeNaoEncontradoException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.SenhaNaoCorrespondeException;

@Service
public class ComentarioService{

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostagemRepository postagemRepository;

    @Transactional
    public ComentarioResponse comentar(Long usuarioId, Long postagemId, String conteudo) {
    	 if (postagemId == null ) {
             throw new CampoObrigatorioException("ID da Postagem não pode ser nulo");
         }
         if ( usuarioId == null) {
             throw new CampoObrigatorioException(" ID do usuário não pode ser nulo");
         }
         if (conteudo == null || conteudo.isBlank()) {
             throw new CampoObrigatorioException(" conteudo não pode ser branc ou nulo");
         }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("Usuário não encontrado."));

        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("Postagem não encontrada."));

        Comentario comentario = new Comentario();
        comentario.setConteudo(conteudo);
        comentario.setDataCriacao(Instant.now());
        comentario.setUsuario(usuario);
        comentario.setPostagem(postagem);

        comentario = comentarioRepository.save(comentario);

        return new ComentarioResponse(
        		comentario.getId(),
        		comentario.getUsuario().getId(),
                comentario.getPostagem().getId(),
                comentario.getConteudo(),
                comentario.getDataCriacao());
    }

    @Transactional
    public void excluirComentario(Long comentarioId, Long usuarioId, String senha) {
        if (comentarioId == null ) {
            throw new CampoObrigatorioException("ID do comentário não pode ser nulo");
        }
        if ( usuarioId == null) {
            throw new CampoObrigatorioException(" ID do usuário não pode ser nulo");
        }
        if (senha == null || senha.isBlank()) {
            throw new CampoObrigatorioException(" senha não pode ser branca ou nula");
        }
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("Usuário não encontrado."));

        Comentario comentario = comentarioRepository.findByIdAndUsuarioId(comentarioId, usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("Comentário não encontrado com o ID: " + comentarioId +" para o usuario fornecido"));

        if (usuario.getSenha().equals(senha)) {
            comentarioRepository.delete(comentario);

        } else throw new SenhaNaoCorrespondeException("senha incorreta");

        
    }

    @Transactional(readOnly = true)
    public Page<ComentarioResponse> listarComentarios(Long postagemId, PageRequest pageRequest) {
        if (postagemId == null) {
            throw new CampoObrigatorioException("O ID da postagem é obrigatório.");
        }
                
        Page<Comentario> comentarios = comentarioRepository.findAllByPostagemId(postagemId, pageRequest)
        .orElseThrow(() -> new EntidadeNaoEncontradoException("Postagem não encontrada com o ID: " + postagemId));

        return comentarios.map(comentario -> new ComentarioResponse(
                comentario.getId(),
                comentario.getUsuario().getId(),
                comentario.getPostagem().getId(),
                comentario.getConteudo(),
                comentario.getDataCriacao()
        ));
    }
}