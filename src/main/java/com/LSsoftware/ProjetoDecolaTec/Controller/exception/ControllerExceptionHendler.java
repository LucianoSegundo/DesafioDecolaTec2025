package com.LSsoftware.ProjetoDecolaTec.Controller.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.LSsoftware.ProjetoDecolaTec.excessoes.CampoObrigatorioException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.EntidadeNaoEncontradoException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.RecursoJaExistenteException;
import com.LSsoftware.ProjetoDecolaTec.excessoes.SenhaNaoCorrespondeException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHendler {

	@ExceptionHandler(EntidadeNaoEncontradoException.class)
	public ResponseEntity<ErroPadrao> entidadeNaoEncontrada(EntidadeNaoEncontradoException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ErroPadrao erro =new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI() );
		
		return ResponseEntity.status(status).body(erro);
		
	}
	
	@ExceptionHandler(CampoObrigatorioException.class)
	public ResponseEntity<ErroPadrao> camposNulos(CampoObrigatorioException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ErroPadrao erro =new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI() );
		
		return ResponseEntity.status(status).body(erro);
		
	}
	
	@ExceptionHandler(RecursoJaExistenteException.class)
	public ResponseEntity<ErroPadrao> recursoJaexiste(RecursoJaExistenteException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErroPadrao erro =new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI() );
		
		return ResponseEntity.status(status).body(erro);
		
	}
		
	@ExceptionHandler(SenhaNaoCorrespondeException.class)
	public ResponseEntity<ErroPadrao> senhaIncorreta(SenhaNaoCorrespondeException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErroPadrao erro =new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI() );
		
		return ResponseEntity.status(status).body(erro);
		
	}
	
}
	
