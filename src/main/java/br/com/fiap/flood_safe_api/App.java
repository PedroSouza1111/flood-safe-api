package br.com.fiap.flood_safe_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "API Flood Safe", version = "v1", description = "API de monitoramento de alagamentos", contact = @Contact(name = "Pedro Souza", email = "rm555533@fiap.com.br")))
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
