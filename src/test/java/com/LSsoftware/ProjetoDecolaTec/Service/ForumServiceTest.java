package com.LSsoftware.ProjetoDecolaTec.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.LSsoftware.ProjetoDecolaTec.Controller.dto.ForumRequest;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.ForumResponse;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Forum;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Usuario;
import com.LSsoftware.ProjetoDecolaTec.Repository.ForumRepository;
import com.LSsoftware.ProjetoDecolaTec.excessoes.CampoObrigatorioException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.EntidadeNaoEncontradoException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.RecursoJaExistenteException;

@SpringBootTest	
class ForumServiceTest {

    @InjectMocks
    private ForumService forumService;

    @Mock
    private ForumRepository forumRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Testar Criação com Campos Nulos")
    void testarCriaçãoComCamposNulos() {
        ForumRequest forumRequest = new ForumRequest(null, "Descrição válida");

        CampoObrigatorioException thrown = assertThrows(CampoObrigatorioException.class, () -> {
            forumService.criarForum(forumRequest);
        });

        assertEquals("O nome do fórum é obrigatório.", thrown.getMessage());
    }

    @Test
    @DisplayName("Testar Criação com Nome de Fórum Já Existente")
    void testarCriaçãoComNomeExistente() {
        ForumRequest forumRequest = new ForumRequest("Fórum Existente", "Descrição válida");
        
        when(forumRepository.existsByNome(forumRequest.nome())).thenReturn(true);

        RecursoJaExistenteException thrown = assertThrows(RecursoJaExistenteException.class, () -> {
            forumService.criarForum(forumRequest);
        });

        assertEquals("Já existe um fórum com o nome fornecido.", thrown.getMessage());
    }

    @Test
    @DisplayName("Testar Criação com Campos Válidos")
    void testarCriaçãoComCamposValidos() {
        ForumRequest forumRequest = new ForumRequest("Fórum Novo", "Descrição válida");

        Forum forum = new Forum();
        forum.setId(1L);
        forum.setNome(forumRequest.nome());
        forum.setDescricao(forumRequest.descricao());

        when(forumRepository.existsByNome(forumRequest.nome())).thenReturn(false);
        when(forumRepository.save(any(Forum.class))).thenReturn(forum);

        ForumResponse forumResponse = forumService.criarForum(forumRequest);

        assertNotNull(forumResponse);
        assertEquals(1L, forumResponse.id());
        assertEquals("Fórum Novo", forumResponse.nome());
        assertEquals("Descrição válida", forumResponse.descricao());
    }

    @Test
    @DisplayName("Testar Consulta com ID Nulo")
    void testarConsultaComIdNulo() {
        Long id = null;

        CampoObrigatorioException thrown = assertThrows(CampoObrigatorioException.class, () -> {
            forumService.consultarForum(id);
        });

        assertEquals("O ID do fórum é obrigatório.", thrown.getMessage());
    }

    @Test
    @DisplayName("Testar Consulta de Fórum que Não Existe")
    void testarConsultaFórumInexistente() {
        Long id = 1L;

        when(forumRepository.findById(id)).thenReturn(java.util.Optional.empty());

        EntidadeNaoEncontradoException thrown = assertThrows(EntidadeNaoEncontradoException.class, () -> {
            forumService.consultarForum(id);
        });

        assertEquals("Fórum não encontrado com o ID: 1", thrown.getMessage());
    }

    @Test
    @DisplayName("Testar Consulta de Fórum Existente")
    void testarConsultaFórumExistente() {
        Long id = 1L;
        Forum forum = new Forum();
        forum.setId(id);
        forum.setNome("Fórum Existente");
        forum.setDescricao("Descrição válida");

        when(forumRepository.findById(id)).thenReturn(java.util.Optional.of(forum));

        ForumResponse forumResponse = forumService.consultarForum(id);

        assertNotNull(forumResponse);
        assertEquals(1L, forumResponse.id());
        assertEquals("Fórum Existente", forumResponse.nome());
        assertEquals("Descrição válida", forumResponse.descricao());
    }

    @Test
    @DisplayName("Testar Listagem de Fóruns")
    void testarListagemDeForuns() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Forum forum1 = new Forum();
        forum1.setId(1L);
        forum1.setNome("Fórum 1");
        forum1.setDescricao("Descrição do Fórum 1");

        Forum forum2 = new Forum();
        forum2.setId(2L);
        forum2.setNome("Fórum 2");
        forum2.setDescricao("Descrição do Fórum 2");

        Page<Forum> forumPage = new PageImpl<>(java.util.List.of(forum1, forum2));
        when(forumRepository.findAll(pageRequest)).thenReturn(forumPage);

        Page<ForumResponse> forumResponses = forumService.listarForuns(pageRequest);

        assertNotNull(forumResponses);
        assertEquals(2, forumResponses.getTotalElements());

    }
}