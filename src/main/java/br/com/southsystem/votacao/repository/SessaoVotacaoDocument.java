package br.com.southsystem.votacao.repository;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "SessaoVotacao")
public class SessaoVotacaoDocument {

    @Id
    private String id;
    
	private Integer numeroPauta;
	private LocalDateTime dataLimite;
}
