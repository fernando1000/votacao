package br.com.southsystem.votacao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Value("${application.name}")
    private String applicationName;

    @Value("${application.description}")
    private String description;

    @Value("${application.version}")
    private String version;

    @Bean
    public OpenAPI customOpenAPI(){
    	return new OpenAPI().components(new Components()).info(new Info().title(applicationName).description(description).version(version));
    }

    @Bean
    public CorsFilter corsFilter() {
    	
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    	CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/v3/api-docs", config);
        return new CorsFilter(source);
    }
}