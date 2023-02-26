package br.com.southsystem.votacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RealizaVotacaoResponse {
	
    private String id;
	private Integer numeroPauta;
	private Long cpfAssociado;
	private String concordaComPauta;
}
