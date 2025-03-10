package com.LSsoftware.ProjetoDecolaTec.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LSsoftware.ProjetoDecolaTec.Controller.dto.ForumRequest;
import com.LSsoftware.ProjetoDecolaTec.Controller.dto.ForumResponse;
import com.LSsoftware.ProjetoDecolaTec.Service.ForumService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value="/api/v1/forum")
public class ForumController {
	
	private ForumService forumService;
	
	public ForumController(ForumService forumService) {
		this.forumService = forumService;
	}

	@Operation(summary = "Rota de criação de forum", description = "Rota usado para criar forum")
	@ApiResponse(responseCode = "200", description = "forum criado com sucesso")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@ApiResponse(responseCode = "400", description = "forum já existe, operação não permitida.")
	@PostMapping(value="/")
	public ResponseEntity<ForumResponse> criarForum(@RequestBody ForumRequest request, JwtAuthenticationToken token){
		
		ForumResponse resposta = forumService.criarForum(request);
		
		return ResponseEntity.ok(resposta);
		
	}
	
	@Operation(summary = "Rota de consulta de forum", description = "Rota usado para consultar forum")
	@ApiResponse(responseCode = "200", description = "forum consultado com sucesso")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@ApiResponse(responseCode = "400", description = "forum já existe, operação não permitida.")
	@GetMapping(value="/{forumID}")
	public ResponseEntity<ForumResponse> consultatForum(@PathVariable Long forumID, JwtAuthenticationToken token){
		
		ForumResponse resposta = forumService.consultarForum(forumID);
		
		return ResponseEntity.ok(resposta);
		
	}

	@Operation(summary = "Rota de consulta paginada de forum", description = "Rota usado para consultar foruns forma paginada")
	@ApiResponse(responseCode = "200", description = "consulta realizada com sucesso")
	@GetMapping(value="/listar")
	public ResponseEntity<Page<ForumResponse>> listarForum(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "nome") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, JwtAuthenticationToken token){
		
		PageRequest pagi = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);
	
		
		Page<ForumResponse> resposta = forumService.listarForuns(pagi);
		
		return ResponseEntity.ok(resposta);
		
	}
}
