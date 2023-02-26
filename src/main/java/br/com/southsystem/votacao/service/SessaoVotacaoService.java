package br.com.southsystem.votacao.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.southsystem.votacao.config.DomainException;
import br.com.southsystem.votacao.dto.CadastroSessaoVotacaoRequest;
import br.com.southsystem.votacao.dto.CadastroSessaoVotacaoResponse;
import br.com.southsystem.votacao.repository.PautaDocument;
import br.com.southsystem.votacao.repository.PautaRepository;
import br.com.southsystem.votacao.repository.SessaoVotacaoDocument;
import br.com.southsystem.votacao.repository.SessaoVotacaoRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SessaoVotacaoService {

	@Autowired
	private SessaoVotacaoRepository sessaoVotacaoRepository;
	
	@Autowired
	private PautaRepository pautaRepository;

	public CadastroSessaoVotacaoResponse cadastraSessaoVotacao(CadastroSessaoVotacaoRequest request) throws DomainException {

		log.info("Verifica se a Pauta {} foi criada", request.getNumeroPauta());
		
		Optional<PautaDocument> pautaProcurada = pautaRepository.findByNumeroPauta(request.getNumeroPauta());
		if(!pautaProcurada.isPresent()) {
			throw new DomainException("É necessário ter uma Pauta ativa para iniciar uma sessão de votação");
		}
		
		log.info("Cria sessão votação {}", request);
		
		SessaoVotacaoDocument votacao = new SessaoVotacaoDocument();
		votacao.setId(UUID.randomUUID().toString());
		votacao.setNumeroPauta(request.getNumeroPauta());
		votacao.setDataLimite(this.aplicaDataLimite(request.getDataLimite()));
		
		SessaoVotacaoDocument saved = sessaoVotacaoRepository.save(votacao);
		
		return new CadastroSessaoVotacaoResponse(saved.getId(), votacao.getNumeroPauta(), votacao.getDataLimite());
	}
		 
	private LocalDateTime aplicaDataLimite(String dataLimite) throws DomainException {
		if(dataLimite == null) {
			return this.aplicaTempoPadrao();
		}
		LocalDateTime dataInformada = this.converteStringEmDateTime(dataLimite);
		if(dataInformada.isBefore(LocalDateTime.now())) {
			throw new DomainException("dataLimite não pode ser menor que data atual");
		}
		return dataInformada;
	}
	
	private LocalDateTime aplicaTempoPadrao() {
		return LocalDateTime.now().plus(Duration.of(1, ChronoUnit.MINUTES));
	}
	
	private LocalDateTime converteStringEmDateTime(String dataString) throws DomainException {
		try {
			return LocalDateTime.parse(dataString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}catch(Exception ex) {
			throw new DomainException("informar dataLimite conforme exemplo: '2022-12-30 14:40:00'");
		}
	}
}
