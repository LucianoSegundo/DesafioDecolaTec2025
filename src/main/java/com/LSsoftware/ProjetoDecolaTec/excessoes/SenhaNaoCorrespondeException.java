package com.LSsoftware.ProjetoDecolaTec.excessoes;

public class SenhaNaoCorrespondeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SenhaNaoCorrespondeException(String mensagem) {
		super(mensagem);
	}
}
