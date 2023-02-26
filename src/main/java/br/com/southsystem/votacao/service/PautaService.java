package br.com.southsystem.votacao.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.southsystem.votacao.config.DomainException;
import br.com.southsystem.votacao.dto.CadastroPautaRequest;
import br.com.southsystem.votacao.dto.CadastroPautaResponse;
import br.com.southsystem.votacao.repository.PautaDocument;
import br.com.southsystem.votacao.repository.PautaRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PautaService {

	@Autowired
	private PautaRepository repository;
	
	public CadastroPautaResponse cadastraPauta(CadastroPautaRequest request) throws DomainException {

		log.info("Verifica se a Pauta {} já existe na base", request.getNumeroPauta());
		
		Optional<PautaDocument> pautaProcurada = repository.findByNumeroPauta(request.getNumeroPauta());
		if(pautaProcurada.isPresent()) {
			throw new DomainException("Número de Pauta já existente");
		}
		
		log.info("Cria nova paula {}", request);
		
		PautaDocument pauta = new PautaDocument();
		pauta.setId(UUID.randomUUID().toString());
		pauta.setNumeroPauta(request.getNumeroPauta());
		pauta.setAssuntoPauta(request.getAssuntoPauta());
		
		PautaDocument saved = repository.save(pauta);
		
		return new CadastroPautaResponse(saved.getId());
	}
}
