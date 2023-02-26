package br.com.southsystem.votacao.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PautaRepository extends MongoRepository<PautaDocument, String> {

    Optional<PautaDocument> findByNumeroPauta(Integer numeroPauta);
}
