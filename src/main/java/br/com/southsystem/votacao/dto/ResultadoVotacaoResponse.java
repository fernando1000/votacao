package br.com.southsystem.votacao.dto;

import lombok.Data;

@Data
public class ResultadoVotacaoResponse {
	
    private Integer numeroPauta;
    private String assuntoPauta;
    private Long votosPositivos;
	private Long votosNegativos;
}
