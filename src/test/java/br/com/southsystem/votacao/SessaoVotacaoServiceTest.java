package br.com.southsystem.votacao;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.southsystem.votacao.config.DomainException;
import br.com.southsystem.votacao.dto.CadastroSessaoVotacaoRequest;
import br.com.southsystem.votacao.dto.CadastroSessaoVotacaoResponse;
import br.com.southsystem.votacao.repository.PautaDocument;
import br.com.southsystem.votacao.repository.PautaRepository;
import br.com.southsystem.votacao.repository.SessaoVotacaoDocument;
import br.com.southsystem.votacao.repository.SessaoVotacaoRepository;
import br.com.southsystem.votacao.service.SessaoVotacaoService;

@SpringBootTest
class SessaoVotacaoServiceTest {

	@InjectMocks
	private SessaoVotacaoService service;
	
	@Mock
	private PautaRepository pautaRepository;

	@Mock
	private SessaoVotacaoRepository sessaoVotacaoRepository;
	
	@Test
	void deveLancarErroDePautaAtivaNecessaria() {

		int numeroPauta = 1;
		CadastroSessaoVotacaoRequest request = new CadastroSessaoVotacaoRequest();
		request.setNumeroPauta(numeroPauta);
		request.setDataLimite("2023-01-11 10:57:00");
		
		when(pautaRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> this.service.cadastraSessaoVotacao(request))
		.isInstanceOf(DomainException.class)
		.hasFieldOrPropertyWithValue("mensagem", "É necessário ter uma Pauta ativa para iniciar uma sessão de votação");
	}

	@Test
	void deveLancarErroDataLimiteNaoPodeSerMenorQueDataAtual() {

		int numeroPauta = 1;
		CadastroSessaoVotacaoRequest request = new CadastroSessaoVotacaoRequest();
		request.setNumeroPauta(numeroPauta);
		request.setDataLimite("2023-01-11 10:57:00");
		
		PautaDocument document = new PautaDocument();
		document.setNumeroPauta(numeroPauta);
		
		when(pautaRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.of(document));

		assertThatThrownBy(() -> this.service.cadastraSessaoVotacao(request))
		.isInstanceOf(DomainException.class)
		.hasFieldOrPropertyWithValue("mensagem", "dataLimite não pode ser menor que data atual");
	}

	@Test
	void deveCadastrarSessaoVotacaoComSucesso() throws Exception {

		int numeroPauta = 1;
		String dataLimite = "2023-03-12 10:57:00";
		CadastroSessaoVotacaoRequest request = new CadastroSessaoVotacaoRequest();
		request.setNumeroPauta(numeroPauta);
		request.setDataLimite(dataLimite);
		
		PautaDocument document = new PautaDocument();
		document.setNumeroPauta(numeroPauta);
		
		when(pautaRepository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.of(document));

		SessaoVotacaoDocument votacao = new SessaoVotacaoDocument();
		votacao.setId("abcde");
		votacao.setNumeroPauta(numeroPauta);
		
		when(sessaoVotacaoRepository.save(any())).thenReturn(votacao);
		
		CadastroSessaoVotacaoResponse response = service.cadastraSessaoVotacao(request);
		assertEquals(votacao.getId(), response.getId());
	}


}
