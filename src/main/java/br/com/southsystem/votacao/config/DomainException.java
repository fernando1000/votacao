package br.com.southsystem.votacao.config;

import lombok.Getter;

@Getter
public class DomainException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String mensagem;
    private Throwable causa;

    public DomainException(String mensagem) {
        super(mensagem);
        this.mensagem = mensagem;
    }

    public DomainException(String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.mensagem = mensagem;
        this.causa = causa;
    }
}
