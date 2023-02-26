package br.com.southsystem.votacao.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Votacao")
public class VotacaoDocument {

    @Id
    private String id;

	private Integer numeroPauta;
	private Long cpfAssociado;
	private Boolean concordaComPauta;
}

