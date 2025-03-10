package com.LSsoftware.ProjetoDecolaTec.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LSsoftware.ProjetoDecolaTec.Controller.dto.ComentarioResponse;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.PostagemResponse;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Comentario;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Forum;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Postagem;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Usuario;
import com.LSsoftware.ProjetoDecolaTec.Repository.ForumRepository;
import com.LSsoftware.ProjetoDecolaTec.Repository.PostagemRepository;
import com.LSsoftware.ProjetoDecolaTec.Repository.UsuarioRepository;
import com.LSsoftware.ProjetoDecolaTec.excessoes.CampoObrigatorioException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.EntidadeNaoEncontradoException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.SenhaNaoCorrespondeException;

@Service
public class PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;
    
    @Autowired
    private ForumRepository forumRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private BCryptPasswordEncoder codSenha; 
    
    public PostagemService() {}

    @Transactional
    public PostagemResponse criarPostagem(Long usuarioId, Long forumId, String conteudo) {
    
    	
    	if (usuarioId == null || forumId == null || conteudo == null || conteudo.isBlank()) {
            throw new CampoObrigatorioException("Os campos 'usuarioId', 'forumId' e 'conteudo' são obrigatórios.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("Usuário não encontrado."));

        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("Fórum não encontrado."));

        Postagem postagem = new Postagem();
        postagem.setConteudo(conteudo);
        postagem.setDataCriacao(Instant.now());
        usuario.addPostagem(postagem);
        postagem.setUsuario(usuario);
        forum.addPostagem(postagem);
        postagem.setForum(forum);

        postagem = postagemRepository.save(postagem);

        List<ComentarioResponse> comentarios = listarComentariosResponse(postagem.getComentarios());

        return new PostagemResponse(postagem.getId(), postagem.getConteudo(), postagem.getUsuario().getId(),
                postagem.getForum().getId(), comentarios, postagem.getDataCriacao());
    }

    @Transactional
    public void excluirPostagem(Long usuarioId, Long postagemId, String senha) {

        if (postagemId == null || usuarioId == null || senha == null || senha.isBlank()) {
            throw new CampoObrigatorioException("ID da postagem, ID do usuário e senha são obrigatórios.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("Usuário não encontrado."));

        if (codSenha.matches(senha, usuario.getSenha())) {
            throw new SenhaNaoCorrespondeException("A senha fornecida não corresponde ao usuário.");
        }

        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("Postagem não encontrado com o ID: " + postagemId));

        postagemRepository.delete(postagem);
    }

    @Transactional(readOnly = true)
    public PostagemResponse consultarPostagem(Long id) {

        if (id == null) {
            throw new CampoObrigatorioException("O ID da postagem é obrigatório.");
        }

        Postagem postagem = postagemRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("Postagem não encontrada com o ID: " + id));

        List<ComentarioResponse> comentarios = listarComentariosResponse(postagem.getComentarios());

        return new PostagemResponse(postagem.getId(), postagem.getConteudo(), postagem.getUsuario().getId(),
                postagem.getForum().getId(), comentarios, postagem.getDataCriacao());
    }

    @Transactional(readOnly = true)
    public Page<PostagemResponse> listarPostagens(Long forumId, PageRequest pageRequest) {
        Page<Postagem> postagens = postagemRepository.findAllByForumId(forumId,pageRequest);

        return postagens.map(postagem -> new PostagemResponse(
                postagem.getId(),
                postagem.getConteudo(),
                postagem.getUsuario().getId(),
                postagem.getForum().getId(),
                listarComentariosResponse(postagem.getComentarios()),
                postagem.getDataCriacao()
        ));
    }
    
    private List<ComentarioResponse> listarComentariosResponse(List<Comentario> coment){
    	List<ComentarioResponse> comentarios = coment.stream()
                .map(comentario -> new ComentarioResponse(
                        comentario.getId(), 
                        comentario.getUsuario().getId(), 
                        comentario.getPostagem().getId(),
                        comentario.getConteudo(), 
                        comentario.getDataCriacao()))
                .collect(Collectors.toList());
    	return comentarios;
    }
}
