package br.com.southsystem.votacao.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RealizaVotacaoRequest {

	@NotNull
	private Integer numeroPauta;

	@NotEmpty
	@Size(min=11, max=11)
	@Pattern(regexp = "^[0-9]*$", message="Permitido apenas numeros")
	private String cpfAssociado;

	@NotNull
	@Pattern(regexp = "SIM|NAO", message="Permitido apenas 'SIM' ou 'NAO'")
	private String concordaComPauta;
}
