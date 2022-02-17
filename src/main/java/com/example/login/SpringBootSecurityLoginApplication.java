package com.example.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.example.login.models.Provincia;

@SpringBootApplication
public class SpringBootSecurityLoginApplication {
	
	private static final String URL_API_TEST =
	            "https://apis.datos.gob.ar/georef/api/provincias?nombre=Tucuman";
	private static final Logger log = LoggerFactory.getLogger(SpringBootSecurityLoginApplication.class);
	

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityLoginApplication.class, args);
	
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Object prov = restTemplate.getForObject(
					URL_API_TEST, Object.class);
			log.info(prov.toString());
		};
	}
}