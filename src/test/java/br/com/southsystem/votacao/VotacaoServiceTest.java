package br.com.southsystem.votacao;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.southsystem.votacao.config.DomainException;
import br.com.southsystem.votacao.dto.RealizaVotacaoRequest;
import br.com.southsystem.votacao.dto.RealizaVotacaoResponse;
import br.com.southsystem.votacao.repository.PautaDocument;
import br.com.southsystem.votacao.repository.PautaRepository;
import br.com.southsystem.votacao.repository.SessaoVotacaoDocument;
import br.com.southsystem.votacao.repository.SessaoVotacaoRepository;
import br.com.southsystem.votacao.repository.VotacaoDocument;
import br.com.southsystem.votacao.repository.VotacaoRepository;
import br.com.southsystem.votacao.service.VotacaoService;

@SpringBootTest
class VotacaoServiceTest {

	@InjectMocks
	private VotacaoService service;
	
	@Mock
	private PautaRepository pautaRepository;

	@Mock
	private SessaoVotacaoRepository sessaoVotacaoRepository;

	@Mock
	private VotacaoRepository votacaoRepository;

	@Test
	void deveLancarErroDePautaAtivaNecessaria() {

		int numeroPauta = 1;
		RealizaVotacaoRequest request = new RealizaVotacaoRequest();
		request.setNumeroPauta(numeroPauta);
		request.setCpfAssociado("36989526881");
		request.setConcordaComPauta("SIM");
		
		when(pautaRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.realizaVotacao(request))
		.isInstanceOf(DomainException.class)
		.hasFieldOrPropertyWithValue("mensagem", "É necessário ter uma Pauta ativa para iniciar uma sessão de votação");
	}

	@Test
	void deveLancarErroDeSessaoNaoEncontrada() {

		int numeroPauta = 1;
		RealizaVotacaoRequest request = new RealizaVotacaoRequest();
		request.setNumeroPauta(numeroPauta);
		request.setCpfAssociado("36989526881");
		request.setConcordaComPauta("SIM");
		
		PautaDocument pauta = new PautaDocument();
		pauta.setNumeroPauta(numeroPauta);
		
		when(pautaRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.of(pauta));

		assertThatThrownBy(() -> service.realizaVotacao(request))
		.isInstanceOf(DomainException.class)
		.hasFieldOrPropertyWithValue("mensagem", "Não foi encontrado uma sessão para a pauta "+pauta.getNumeroPauta());
	}
	
	@Test
	void deveLancarErroDeTempoVotacaoFinalizado() {

		int numeroPauta = 1;
		RealizaVotacaoRequest request = new RealizaVotacaoRequest();
		request.setNumeroPauta(numeroPauta);
		request.setCpfAssociado("36989526881");
		request.setConcordaComPauta("SIM");
		
		PautaDocument pauta = new PautaDocument();
		pauta.setNumeroPauta(numeroPauta);
		
		when(pautaRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.of(pauta));

		SessaoVotacaoDocument sessaoVotacao = new SessaoVotacaoDocument();
		sessaoVotacao.setDataLimite(LocalDateTime.now().minusHours(1));
		
		when(sessaoVotacaoRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.of(sessaoVotacao));
		
		assertThatThrownBy(() -> service.realizaVotacao(request))
		.isInstanceOf(DomainException.class)
		.hasFieldOrPropertyWithValue("mensagem", "Tempo para votação finalizou em "+sessaoVotacao.getDataLimite());
	}

	@Test
	void deveLancarErroDeVotoJaRealizado() {

		int numeroPauta = 1;
		String cpfString = "36989526881";
		long cpfLong = Long.parseLong(cpfString);
		
		RealizaVotacaoRequest request = new RealizaVotacaoRequest();
		request.setNumeroPauta(numeroPauta);
		request.setCpfAssociado(cpfString);
		request.setConcordaComPauta("SIM");
		
		PautaDocument pauta = new PautaDocument();
		pauta.setNumeroPauta(numeroPauta);
		
		when(pautaRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.of(pauta));

		SessaoVotacaoDocument sessaoVotacao = new SessaoVotacaoDocument();
		sessaoVotacao.setDataLimite(LocalDateTime.now().plusDays(1));
		
		when(sessaoVotacaoRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.of(sessaoVotacao));
		
		VotacaoDocument votacao = new VotacaoDocument(); 
		votacao.setCpfAssociado(cpfLong);
		
		when(votacaoRepository.findByNumeroPautaAndCpfAssociado(numeroPauta, cpfLong)).thenReturn(Optional.of(votacao));
		
		assertThatThrownBy(() -> service.realizaVotacao(request))
		.isInstanceOf(DomainException.class)
		.hasFieldOrPropertyWithValue("mensagem", "Voto já foi realizado");
	}

	@Test
	void deveRealizarVotacaoComSucesso() throws Exception {

		int numeroPauta = 1;
		String cpfString = "36989526881";
		long cpfLong = Long.parseLong(cpfString);
		
		RealizaVotacaoRequest request = new RealizaVotacaoRequest();
		request.setNumeroPauta(numeroPauta);
		request.setCpfAssociado(cpfString);
		request.setConcordaComPauta("SIM");
		
		PautaDocument pauta = new PautaDocument();
		pauta.setNumeroPauta(numeroPauta);
		
		when(pautaRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.of(pauta));

		SessaoVotacaoDocument sessaoVotacao = new SessaoVotacaoDocument();
		sessaoVotacao.setDataLimite(LocalDateTime.now().plusDays(1));
		
		when(sessaoVotacaoRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.of(sessaoVotacao));
		
		when(votacaoRepository.findByNumeroPautaAndCpfAssociado(numeroPauta, cpfLong)).thenReturn(Optional.empty());
		
		VotacaoDocument votacaoDocument = new VotacaoDocument();
		votacaoDocument.setCpfAssociado(cpfLong);
		votacaoDocument.setConcordaComPauta(true);
		votacaoDocument.setNumeroPauta(numeroPauta);
		
		when(votacaoRepository.save(any())).thenReturn(votacaoDocument);
		
		RealizaVotacaoResponse response = service.realizaVotacao(request);
		
		assertEquals(votacaoDocument.getCpfAssociado(), response.getCpfAssociado());
		assertEquals(votacaoDocument.getNumeroPauta(), response.getNumeroPauta());
	}

}
