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

import com.LSsoftware.ProjetoDecolaTec.Controller.dto.ComentarioResponse;
import com.LSsoftware.ProjetoDecolaTec.Service.ComentarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value= "/api/v1/comentarios")
public class ComentariosController {
	
	
	private ComentarioService service;
	
	public ComentariosController( ComentarioService service) {
		this.service = service;
	}
	
	@Operation(summary = "Rota de criação de Comentarios", description = "Rota usado para criar Comentarios")
	@ApiResponse(responseCode = "200", description = "comentario criado com sucesso")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@ApiResponse(responseCode = "404", description = "operação não permitida entidade não encontrada")
	@PostMapping(value ="/{postagemid}")
	public ResponseEntity<ComentarioResponse> comentar(@PathVariable Long postagemid, @RequestBody Long usuarioid , String conteudo){
		
		ComentarioResponse resposta = service.comentar(usuarioid, postagemid, conteudo);
		
		return ResponseEntity.ok(resposta);
	}
	
	@Operation(summary = "Rota de criação de Comentarios", description = "Rota usado para criar Comentarios")
	@ApiResponse(responseCode = "200", description = "comentario criado com sucesso")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@ApiResponse(responseCode = "404", description = "operação não permitida entidade não encontrada")
	@ApiResponse(responseCode = "400", description = "senha incorreta")

	@DeleteMapping(value ="/{comentarioid}")
	public ResponseEntity<ComentarioResponse> excluircomentar(@PathVariable Long comentarioid, @RequestBody Long usuarioid , String senha){
		
		service.excluirComentario(comentarioid, usuarioid, senha);
		
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Rota de consulta paginada de usuarios", description = "rota usada para consulta paginada de usuário")
	@ApiResponse(responseCode = "200", description = "consulta realizada com sucesso")
	@ApiResponse(responseCode = "404", description = "operação não permitida entidade não encontrada")
	@GetMapping(value ="/listar/{postagemId}")
	public ResponseEntity<Page<ComentarioResponse>> listarUsuarios(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "dataCriacao") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem,
			@PathVariable Long postagemId){
		
		PageRequest pagi = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);
	
		Page<ComentarioResponse> response = service.listarComentarios(postagemId,pagi);
		
		return ResponseEntity.ok(response);
	}
	

}
