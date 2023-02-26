package br.com.southsystem.votacao.config;

import java.util.Map;

import br.com.southsystem.votacao.util.FormatacaoDataUtil;
import lombok.Getter;

@Getter
public class ApiErro {
	
	private Map<String, String> erros;
	private String timestamp;
	
	public ApiErro(Map<String, String> erros) {
		this.erros = erros;
		String dataAtualFormatada = FormatacaoDataUtil.getDataAtualFormatada();
		this.timestamp = dataAtualFormatada;
	}
}
