package br.com.southsystem.votacao.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CadastroSessaoVotacaoResponse {

	private String id;
	private Integer numeroPauta;
	private LocalDateTime dataLimite;
}
