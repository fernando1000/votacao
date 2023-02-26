package br.com.southsystem.votacao.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessaoVotacaoRepository extends MongoRepository<SessaoVotacaoDocument, String> {

	Optional<SessaoVotacaoDocument> findByNumeroPauta(Integer numeroPauta);
}
