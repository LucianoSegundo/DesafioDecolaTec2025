package com.LSsoftware.ProjetoDecolaTec.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LSsoftware.ProjetoDecolaTec.Controller.dto.LoginRequest;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.LoginResponse;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.UsuarioRequest;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.UsuarioResponse;
import com.LSsoftware.ProjetoDecolaTec.Entidades.Usuario;
import com.LSsoftware.ProjetoDecolaTec.Repository.UsuarioRepository;
import com.LSsoftware.ProjetoDecolaTec.excessoes.CampoObrigatorioException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.EntidadeNaoEncontradoException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.RecursoJaExistenteException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.SenhaNaoCorrespondeException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
   // @Autowired
   // private  BCryptPasswordEncoder passwordEncoder
    
    public UsuarioService() {};

    @Transactional
    public UsuarioResponse cadastrarUsuario(UsuarioRequest usuarioRequest) {

    	if (usuarioRequest.nome() == null || usuarioRequest.nome().isBlank()) {
    		System.out.println("campo " + usuarioRequest.nome() );
            throw new CampoObrigatorioException("nome blanco ou nulo, cadastro negado");
        }
        if (usuarioRequest.senha() == null || usuarioRequest.senha().isBlank()) {
            throw new CampoObrigatorioException("senha blanca ou nula, cadastro negado");
        }
        if (usuarioRequest.email() == null || usuarioRequest.email().isBlank()) {
            throw new CampoObrigatorioException("email blanco ou nulo, cadastro negado");
        }
        if (usuarioRequest.dataNascimento() == null) {
            throw new CampoObrigatorioException("data de nascimento blanco ou nulo, cadastro negado");
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.findByNome(usuarioRequest.nome());
        if (usuarioExistente.isPresent()) {
            throw new RecursoJaExistenteException("usuario já existe, cadastro negado");
        }

        //lembrar de criptogramar a senha quando adicionar o spring security
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRequest.nome());
        usuario.setEmail(usuarioRequest.email());
        usuario.setSenha(usuarioRequest.senha());
        usuario.setDataNascimento(usuarioRequest.dataNascimento());

        usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario.getId(),usuario.getNome());
    }

    @Transactional(readOnly = true)
    public LoginResponse logarUsuario(LoginRequest loginRequest) {

    	if (loginRequest.nome() == null || loginRequest.nome().isBlank()) {
            throw new CampoObrigatorioException("nome blanco ou nulo, login negado");
        }
        if (loginRequest.senha() == null || loginRequest.senha().isBlank()) {
            throw new CampoObrigatorioException("senha blanca ou nula, login negado");
        }

        Usuario usuario = usuarioRepository.findByNome(loginRequest.nome())
                .orElseThrow(() -> new EntidadeNaoEncontradoException( "Usuario" +loginRequest.nome() + "não foi encontrado"));

        //lembrar de comparar as senhas criptografadas quando adicionar o spring security
        
        if ( loginRequest.senha().equals( usuario.getSenha())) {
            return new LoginResponse(usuario.getNome(), 2l);

        }
        else throw new SenhaNaoCorrespondeException("Senha incorreta");


    }

    @Transactional
    public void excluirUsuario(Long idUsuario, String senha) {
        if (idUsuario == null) {
            throw new CampoObrigatorioException("id balnco ou nulo, operação negada");
        }
        if (senha == null || senha.isBlank()) {
            throw new CampoObrigatorioException("senha blanca ou nula, operação negada");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntidadeNaoEncontradoException("ID " + idUsuario));

        //lembrar de comparar as senhas criptografadas quando adicionar o spring security
        if (usuario.getSenha().equals(senha) == true) {
            usuarioRepository.delete(usuario);
            
        }
        else {
        	System.out.println("senha salva: |" + usuario.getSenha() + "| senha recebida: |"+ senha +"|");
        	throw new SenhaNaoCorrespondeException("Senha incoreta");
        } 


    }

    @Transactional(readOnly = true)
    public UsuarioResponse consultar( Long idUsuario ) {
    	
    	if(idUsuario == null) throw new CampoObrigatorioException("id do usuário está nulo");
    	
        Usuario user = usuarioRepository.findById(idUsuario) 
        		.orElseThrow(() -> new EntidadeNaoEncontradoException("Usuario não encontrado"));

        return new UsuarioResponse(user.getId(), user.getNome());
    }
    
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarUsuarios( PageRequest pageRequest) {
    	
        return usuarioRepository.findAll(pageRequest).map((x)-> new UsuarioResponse(x.getId(), x.getNome()));
    }
}
