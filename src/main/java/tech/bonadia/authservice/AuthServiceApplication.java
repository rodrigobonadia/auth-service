package tech.bonadia.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import tech.bonadia.authservice.adapters.config.TokenTtlProperties;

@SpringBootApplication
@EnableConfigurationProperties(TokenTtlProperties.class)
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
