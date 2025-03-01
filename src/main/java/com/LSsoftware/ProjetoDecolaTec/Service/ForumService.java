package com.LSsoftware.ProjetoDecolaTec.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LSsoftware.ProjetoDecolaTec.Controller.dto.ForumRequest;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.ForumResponse;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Forum;
import com.LSsoftware.ProjetoDecolaTec.Repository.ForumRepository;
import com.LSsoftware.ProjetoDecolaTec.excessoes.CampoObrigatorioException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.EntidadeNaoEncontradoException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.RecursoJaExistenteException;

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;
    
    public ForumService(){}

    @Transactional
    public ForumResponse criarForum(ForumRequest forumRequest) {

    	if (forumRequest.nome() == null || forumRequest.nome().isBlank()) {
            throw new CampoObrigatorioException("O nome do fórum é obrigatório.");
        }
        if (forumRequest.descricao() == null || forumRequest.descricao().isBlank()) {
            throw new CampoObrigatorioException("A descrição do fórum é obrigatória.");
        }

        if (forumRepository.existsByNome(forumRequest.nome())) {
            throw new RecursoJaExistenteException("Já existe um fórum com o nome fornecido.");
        }

        Forum forum = new Forum();
        forum.setNome(forumRequest.nome());
        forum.setDescricao(forumRequest.descricao());

        forum = forumRepository.save(forum);

        return new ForumResponse(forum.getId(), forum.getNome(), forum.getDescricao());
    }

    @Transactional(readOnly = true)
    public ForumResponse consultarForum(Long id) {

    	if (id == null) {
            throw new CampoObrigatorioException("O ID do fórum é obrigatório.");
        }

        Forum forum = forumRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("Fórum não encontrado com o ID: " + id));

        return new ForumResponse(forum.getId(), forum.getNome(), forum.getDescricao());
    }

    @Transactional(readOnly = true)
    public Page<ForumResponse> listarForuns(PageRequest pageRequest) {
        Page<Forum> forums = forumRepository.findAll(pageRequest);

        return forums.map(forum -> new ForumResponse(forum.getId(), forum.getNome(), forum.getDescricao()));
    }
}