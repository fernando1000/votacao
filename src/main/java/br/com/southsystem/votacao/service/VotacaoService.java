package br.com.southsystem.votacao.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.southsystem.votacao.config.DomainException;
import br.com.southsystem.votacao.dto.RealizaVotacaoRequest;
import br.com.southsystem.votacao.dto.RealizaVotacaoResponse;
import br.com.southsystem.votacao.dto.ResultadoVotacaoResponse;
import br.com.southsystem.votacao.repository.PautaDocument;
import br.com.southsystem.votacao.repository.PautaRepository;
import br.com.southsystem.votacao.repository.SessaoVotacaoDocument;
import br.com.southsystem.votacao.repository.SessaoVotacaoRepository;
import br.com.southsystem.votacao.repository.VotacaoDocument;
import br.com.southsystem.votacao.repository.VotacaoRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class VotacaoService {

	@Autowired
	private PautaRepository pautaRepository;
	
	@Autowired
	private SessaoVotacaoRepository sessaoVotacaoRepository;

	@Autowired
	private VotacaoRepository votacaoRepository;

	public RealizaVotacaoResponse realizaVotacao(RealizaVotacaoRequest request) throws DomainException {

		log.info("Verifica se a Pauta {} foi criada", request.getNumeroPauta());
		Optional<PautaDocument> pautaProcurada = pautaRepository.findByNumeroPauta(request.getNumeroPauta());
		if(!pautaProcurada.isPresent()) {
			throw new DomainException("É necessário ter uma Pauta ativa para iniciar uma sessão de votação");
		}
		Optional<SessaoVotacaoDocument> sessaoVotacaoProcurada = sessaoVotacaoRepository.findByNumeroPauta(pautaProcurada.get().getNumeroPauta());
		if(!sessaoVotacaoProcurada.isPresent()) {
			throw new DomainException("Não foi encontrado uma sessão para a pauta "+pautaProcurada.get().getNumeroPauta());
		}
		LocalDateTime dataLimite = sessaoVotacaoProcurada.get().getDataLimite();
		if(dataLimite.isBefore(LocalDateTime.now())) {
			throw new DomainException("Tempo para votação finalizou em "+dataLimite);
		}
		
		long cpf = Long.parseLong(request.getCpfAssociado());
		
		log.info("Verifica se o CPF {} é válido", cpf);
		this.validaCpf(cpf);
		
		this.verificaVotoRepetido(request.getNumeroPauta(), cpf);
		
		VotacaoDocument votacao = new VotacaoDocument();
		votacao.setId(UUID.randomUUID().toString());
		votacao.setNumeroPauta(request.getNumeroPauta());
		votacao.setCpfAssociado(cpf);
		votacao.setConcordaComPauta((request.getConcordaComPauta().equals("SIM")));
		
		VotacaoDocument saved = votacaoRepository.save(votacao);
		
		return new RealizaVotacaoResponse(saved.getId(), saved.getNumeroPauta(), saved.getCpfAssociado(), request.getConcordaComPauta());
	}
	
	public ResultadoVotacaoResponse obterResultadoVotacao(Integer numeroPauta) throws DomainException {
		
		Optional<PautaDocument> pautaProcurada = pautaRepository.findByNumeroPauta(numeroPauta);
		if(!pautaProcurada.isPresent()) {
			throw new DomainException("Pauta não encontrada");
		}
		List<VotacaoDocument> votacoes = votacaoRepository.findAllByNumeroPauta(numeroPauta);
		long sim = votacoes.stream().filter(VotacaoDocument::getConcordaComPauta).count();
		
		ResultadoVotacaoResponse resultado = new ResultadoVotacaoResponse();
		resultado.setNumeroPauta(numeroPauta);
		resultado.setAssuntoPauta(pautaProcurada.get().getAssuntoPauta());
		resultado.setVotosPositivos(sim);
		resultado.setVotosNegativos(votacoes.size() - sim);
		return resultado;
	}
	
	private void validaCpf(Long cpf) throws DomainException {
		if(cpf.toString().length() != 11) {
			throw new DomainException("CPF precisa ter 11 números");
		}
		//throw new DomainException("CPF inválido 'UNABLE_TO_VOTE'");
	}
	
	private void verificaVotoRepetido(int numeroPauta, long cpfAssociado) throws DomainException {
		
		Optional<VotacaoDocument> votacaoProcurada = votacaoRepository.findByNumeroPautaAndCpfAssociado(numeroPauta, cpfAssociado);
		if(votacaoProcurada.isPresent()) {
			throw new DomainException("Voto já foi realizado");
		}
	}
	
}
