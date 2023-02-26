package br.com.southsystem.votacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VotacaoRepository extends MongoRepository<VotacaoDocument, String> {

	Optional<VotacaoDocument> findByNumeroPautaAndCpfAssociado(int numeroPauta, long cpfAssociado);
	
	List<VotacaoDocument> findAllByNumeroPauta(int numeroPauta);
}
