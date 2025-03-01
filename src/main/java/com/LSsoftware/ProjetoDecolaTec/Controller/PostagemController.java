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

import com.LSsoftware.ProjetoDecolaTec.Controller.dto.PostagemResponse;
import com.LSsoftware.ProjetoDecolaTec.Service.PostagemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value="/api/v1/postagem")
public class PostagemController {
	
	private PostagemService postagemServi;
	
	public PostagemController(PostagemService postagemServi) {
		this.postagemServi = postagemServi;
	}
	
	@Operation(summary = "Rota de criação de Postagem", description = "Rota usado para criar Postagem")
	@ApiResponse(responseCode = "200", description = "Postagem criado com sucesso")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@PostMapping(value= "/{idForum}")
	public ResponseEntity<PostagemResponse> criarPostagem( @RequestBody Long idUsuario, String conteudo, @PathVariable Long idForum) {
		
		PostagemResponse response = postagemServi.criarPostagem(idUsuario, idForum, conteudo);
		
		return ResponseEntity.ok(response);
	}
	
	
	@Operation(summary = "Rota de exclusão de Postagem", description = "Rota usado para excluir Postagem")
	@ApiResponse(responseCode = "200", description = "Postagem excluida com sucesso")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@ApiResponse(responseCode = "404", description = "operação não permitida entidade não encontrada")
	@ApiResponse(responseCode = "400", description = "senha incorreta")

	@DeleteMapping(value= "/{postagemId}")
	public ResponseEntity<Void> excluirPostagem(@PathVariable Long postagemId, @RequestBody Long idUsuario, String senha) {
		
		postagemServi.excluirPostagem(idUsuario,postagemId, senha);
		
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Rota de consulta de Postagem", description = "Rota usado para consulta Postagem")
	@ApiResponse(responseCode = "200", description = "consulta realizada com sucesso")
	@ApiResponse(responseCode = "422", description = "operação não permitida devido a campos nulos")
	@ApiResponse(responseCode = "404", description = "operação não permitida entidade não encontrada")
	@GetMapping(value= "/{postagemId}")
	public ResponseEntity<PostagemResponse> cunsultarPostagem(@PathVariable Long postagemId) {
		
		PostagemResponse resposta =	postagemServi.consultarPostagem(postagemId);
		
		return ResponseEntity.ok(resposta);
	}
	
	@Operation(summary = "Rota de consulta paginada de paginada", description = "Rota usado para consultar postagemde forma paginada")
	@ApiResponse(responseCode = "200", description = "consulta realizada com sucesso")
	@GetMapping(value="/listar/{forumId}")
	public ResponseEntity<Page<PostagemResponse>> listarPostagem(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "dataCriacao") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem,
			@PathVariable Long forumId){
		
		PageRequest pagi = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);
	
		
		Page<PostagemResponse> resposta = postagemServi.listarPostagens(forumId, pagi);
		
		return ResponseEntity.ok(resposta);
		
	}
}
