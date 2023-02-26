package br.com.southsystem.votacao.config;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErro> argumentoInvalido(MethodArgumentNotValidException ex) {
		log.error("Argumento invalido");
		HashMap<String, String> erros = new HashMap<>();
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		fieldErrors.forEach(erro -> erros.put(erro.getField(), erro.getDefaultMessage()));
        return new ResponseEntity<>(new ApiErro(erros), HttpStatus.BAD_REQUEST);
    }
	
	@ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErro> mensagemNaoLegivel(HttpMessageNotReadableException ex) {
		log.error("Mensagem nao legivel");
		HashMap<String, String> erros = new HashMap<>();
		erros.put("mensagem", ex.getMessage());
        return new ResponseEntity<>(new ApiErro(erros), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErro> mensagemDominio(DomainException ex) {
    	log.error("Mensagem do dominio");
    	HashMap<String, String> erros = new HashMap<>();
    	erros.put("mensagem", ex.getMessage());
        return new ResponseEntity<>(new ApiErro(erros), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
