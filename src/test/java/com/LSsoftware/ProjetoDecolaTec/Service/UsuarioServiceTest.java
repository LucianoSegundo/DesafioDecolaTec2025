package com.LSsoftware.ProjetoDecolaTec.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

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

import com.LSsoftware.ProjetoDecolaTec.Controller.dto.LoginRequest;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.LoginResponse;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.UsuarioRequest;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.UsuarioResponse;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Usuario;
import com.LSsoftware.ProjetoDecolaTec.excessoes.CampoObrigatorioException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.EntidadeNaoEncontradoException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.RecursoJaExistenteException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.SenhaNaoCorrespondeException;



@SpringBootTest	
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioRequest usuarioRequest;
    private LoginRequest loginRequest;
    private Usuario usuario;
    private SimpleDateFormat  data;

    @BeforeEach
    void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);
        
        data = new SimpleDateFormat("dd/mm/yyyy");
        usuarioRequest = new UsuarioRequest("João", "senha123", "joao@mail.com", data.parse(("01/01/1990")));
        loginRequest = new LoginRequest("João", "senha123");
        
        usuario = new Usuario();
        usuario.setNome("João");
        usuario.setSenha("senha123");
        usuario.setEmail("joao@mail.com");
        usuario.setDataNascimento(data.parse(("01/01/1990")));
    }

    @Test
    @DisplayName("Testar Cadastro Campos Nulos")
    void testarCadastroCamposNulos() {
        UsuarioRequest requestInvalido = new UsuarioRequest(null, "senha123", "joao@mail.com", null);

        assertThrows(CampoObrigatorioException.class, () -> {
            usuarioService.cadastrarUsuario(requestInvalido);
        });
    }

    @Test
    @DisplayName("Testar Cadastro Usuário Existente")
    void testarCadastroUsuarioExistente() {
        when(usuarioRepository.findByNome(usuarioRequest.nome())).thenReturn(Optional.of(usuario));

        assertThrows(RecursoJaExistenteException.class, () -> {
            usuarioService.cadastrarUsuario(usuarioRequest);
        });
    }

    @Test
    @DisplayName("Testar Cadastro Sucesso")
    void testarCadastroSucesso() {
        when(usuarioRepository.findByNome(usuarioRequest.nome())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponse response = usuarioService.cadastrarUsuario(usuarioRequest);

        assertNotNull(response);
        assertEquals("João", response.nome());
    }

    @Test
    @DisplayName("Testar Login Campos Nulos")
    void testarLoginCamposNulos() {
        LoginRequest requestInvalido = new LoginRequest(null, "senha123");

        assertThrows(CampoObrigatorioException.class, () -> {
            usuarioService.logarUsuario(requestInvalido);
        });
    }

    @Test
    @DisplayName("Testar Login Usuário Não Encontrado")
    void testarLoginUsuarioNaoEncontrado() {
        when(usuarioRepository.findByNome(loginRequest.nome())).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradoException.class, () -> {
            usuarioService.logarUsuario(loginRequest);
        });
    }

    @Test
    @DisplayName("Testar Login Senha Incorreta")
    void testarLoginSenhaIncorreta() {
        when(usuarioRepository.findByNome(loginRequest.nome())).thenReturn(Optional.of(usuario));

        LoginRequest loginInvalido = new LoginRequest("João", "senhaErrada");

        assertThrows(SenhaNaoCorrespondeException.class, () -> {
            usuarioService.logarUsuario(loginInvalido);
        });
    }

    @Test
    @DisplayName("Testar Login Sucesso")
    void testarLoginSucesso() {
        when(usuarioRepository.findByNome(loginRequest.nome())).thenReturn(Optional.of(usuario));

        LoginResponse response = usuarioService.logarUsuario(loginRequest);

        assertNotNull(response);
    }

    @Test
    @DisplayName("Testar Exclusão Usuário com Senha Errada")
    void testarExclusaoUsuarioSenhaErrada() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThrows(SenhaNaoCorrespondeException.class, () -> {
            usuarioService.excluirUsuario(1L, "senhaErrada");
        });
    }

    @Test
    @DisplayName("Testar Exclusão Usuário Sucesso")
    void testarExclusaoUsuarioSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.excluirUsuario(1L, "senha123");

        verify(usuarioRepository).delete(usuario);
    }

    @Test
    @DisplayName("Testar Consultar Usuário Não Encontrado")
    void testarConsultarUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradoException.class, () -> {
            usuarioService.consultar(1L);
        });
    }

    @Test
    @DisplayName("Testar Consultar Usuário Sucesso")
    void testarConsultarUsuarioSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponse response = usuarioService.consultar(1L);

        assertNotNull(response);
        assertEquals("João", response.nome());
    }

    @Test
    @DisplayName("Testar Listar Usuários")
    void testarListarUsuarios() {
        Page<Usuario> page = new PageImpl<>(java.util.List.of(usuario));
        when(usuarioRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<UsuarioResponse> response = usuarioService.listarUsuarios(PageRequest.of(0, 10));

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }
}