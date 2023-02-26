package br.com.southsystem.votacao.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.southsystem.votacao.config.DomainException;
import br.com.southsystem.votacao.dto.CadastroSessaoVotacaoRequest;
import br.com.southsystem.votacao.dto.CadastroSessaoVotacaoResponse;
import br.com.southsystem.votacao.service.SessaoVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Sessão Votação", description = "Realiza operações relacionadas a sessão de votação")
@RestController
@RequestMapping("/v1/sessao-votacao")
public class SessaoVotacaoController {

	@Autowired
	private SessaoVotacaoService service;
	
	@Operation(summary = "Cadastra uma nova sessão para votação")
	@PostMapping
	public ResponseEntity<CadastroSessaoVotacaoResponse> cadastraSessaoVotacao(@Valid @RequestBody CadastroSessaoVotacaoRequest request) throws DomainException {
		
		CadastroSessaoVotacaoResponse response = service.cadastraSessaoVotacao(request);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

}
