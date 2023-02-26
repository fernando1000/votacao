package br.com.southsystem.votacao;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.southsystem.votacao.config.DomainException;
import br.com.southsystem.votacao.dto.CadastroPautaRequest;
import br.com.southsystem.votacao.dto.CadastroPautaResponse;
import br.com.southsystem.votacao.repository.PautaDocument;
import br.com.southsystem.votacao.repository.PautaRepository;
import br.com.southsystem.votacao.service.PautaService;

import org.mockito.InjectMocks;
import org.mockito.Mock;

@SpringBootTest
class PautaServiceTest {

	@InjectMocks
	private PautaService service;
	
	@Mock
	private PautaRepository repository;

	@Test
	void deveLancarErroDePautaJaExistente() {

		int numeroPauta = 1;
		String assunto = "aceita CBD de 16% ao ano";
		CadastroPautaRequest request = new CadastroPautaRequest();
		request.setNumeroPauta(numeroPauta);
		request.setAssuntoPauta(assunto);
		
		PautaDocument document = new PautaDocument();
		document.setId("abc");
		document.setNumeroPauta(numeroPauta);
		document.setAssuntoPauta(assunto);
		
		when(repository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.of(document));

		assertThatThrownBy(() -> this.service.cadastraPauta(request))
		.isInstanceOf(DomainException.class)
		.hasFieldOrPropertyWithValue("mensagem", "Número de Pauta já existente");
	}

	@Test
	void deveCadastrarPautaComSucesso() throws Exception {

		int numeroPauta = 1;
		String assunto = "aceita CBD de 16% ao ano";
		CadastroPautaRequest request = new CadastroPautaRequest();
		request.setNumeroPauta(numeroPauta);
		request.setAssuntoPauta(assunto);
		
		when(repository.findByNumeroPauta(numeroPauta)).thenReturn(Optional.empty());

		PautaDocument document = new PautaDocument();
		document.setId("abcd");
		document.setNumeroPauta(numeroPauta);
		document.setAssuntoPauta(assunto);
	
		when(repository.save(any())).thenReturn(document);

		CadastroPautaResponse response = service.cadastraPauta(request);
		assertEquals(document.getId(), response.getId());
	}

}
