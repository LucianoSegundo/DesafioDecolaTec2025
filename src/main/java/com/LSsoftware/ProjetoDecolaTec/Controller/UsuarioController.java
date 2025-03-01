package com.LSsoftware.ProjetoDecolaTec.Controller;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LSsoftware.ProjetoDecolaTec.Controller.dto.LoginRequest;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.LoginResponse;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.UsuarioRequest;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.UsuarioResponse;
import com.LSsoftware.ProjetoDecolaTec.Service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "api/v1/usuario")
public class UsuarioController {
	
	private UsuarioService useService;
	
	public UsuarioController(UsuarioService useService) {
		this.useService = useService;
	};
	
	
	@Operation(summary = "Rota de criação de usuarios", description = "Rota usado para criar usuarios")
	@ApiResponse(responseCode = "200", description = "Usuario criado com sucesso não encontrada")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@ApiResponse(responseCode = "400", description = "Usuario já existe, operação não permitida.")
	@PostMapping(value ="/")
	public ResponseEntity<UsuarioResponse> criarUsuario(@RequestBody UsuarioRequest request){
	
		UsuarioResponse response =useService.cadastrarUsuario(request);
		
		
		return ResponseEntity.ok(response);
	}

	
	@Operation(summary = "Rota de login de usuarios", description = "rota usada para adiquirir as credenciais necessarias para usar o sistema")
	@ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@ApiResponse(responseCode = "404", description = "Usuario não encontrado")
	@ApiResponse(responseCode = "400", description = "Senha incorreta.")
	@PostMapping(value ="/login")
	public ResponseEntity<LoginResponse> logarUsuario(@RequestBody LoginRequest request){
	
		LoginResponse response =useService.logarUsuario(request);
		
		
		return ResponseEntity.ok(response);
	}
	
	
	@Operation(summary = "Rota de deleção de usuarios", description = "rota usada para excluir uma conta, só pode ser feito pelo proprio dono da conta")
	@ApiResponse(responseCode = "200", description = "deleção realizada com sucesso")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@ApiResponse(responseCode = "404", description = "Usuario não encontrado")
	@ApiResponse(responseCode = "400", description = "Senha incorreta.")
	@DeleteMapping(value ="/{idUsuario}")
	public ResponseEntity<Void> deletarUsuario(@PathVariable Long idUsuario,@RequestBody String Senha){
	
		useService.excluirUsuario(idUsuario, Senha);
		
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Rota de consulta de usuarios", description = "rota usada para consultar um usuário")
	@ApiResponse(responseCode = "200", description = "consulta realizada com sucesso")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@ApiResponse(responseCode = "404", description = "Usuario não encontrado")
	@GetMapping(value = "/{idUsuario}")
	public ResponseEntity<UsuarioResponse> consultar(@PathVariable Long idUsuario){
	
		UsuarioResponse response = useService.consultar(idUsuario);
		
		return ResponseEntity.ok(response);
	}
	
	
	@Operation(summary = "Rota de consulta paginada de usuarios", description = "rota usada para consulta paginada de usuário")
	@ApiResponse(responseCode = "200", description = "consulta realizada com sucesso")
	@GetMapping(value ="/listar")
	public ResponseEntity<Page<UsuarioResponse>> listarUsuarios(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "nome") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem){
		
		PageRequest pagi = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);
	
		Page<UsuarioResponse> response = useService.listarUsuarios(pagi);
		
		return ResponseEntity.ok(response);
	}
}
