package br.com.southsystem.votacao.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.southsystem.votacao.config.DomainException;
import br.com.southsystem.votacao.dto.RealizaVotacaoRequest;
import br.com.southsystem.votacao.dto.RealizaVotacaoResponse;
import br.com.southsystem.votacao.dto.ResultadoVotacaoResponse;
import br.com.southsystem.votacao.service.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Votação", description = "Realiza operações relacionadas a votação")
@RestController
@RequestMapping("/v1/votacao")
public class VotacaoController {

	@Autowired
	private VotacaoService service;
	
	@Operation(summary = "Realizar votação sobre uma determinada Pauta")
	@PostMapping
	public ResponseEntity<RealizaVotacaoResponse> realizaVotacao(@Valid @RequestBody RealizaVotacaoRequest request) throws DomainException {
		
		RealizaVotacaoResponse response = service.realizaVotacao(request);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Obter resultado de votação")
	@GetMapping("/{numeroPauta}")
	public ResponseEntity<ResultadoVotacaoResponse> obterResultadoVotacao(@PathVariable Integer numeroPauta) throws DomainException {
		
		ResultadoVotacaoResponse response = service.obterResultadoVotacao(numeroPauta);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
