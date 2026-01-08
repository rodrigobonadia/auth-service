package tech.bonadia.authservice.adapters.config;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/refresh")
				.permitAll().requestMatchers("/actuator/**", "/error").permitAll().anyRequest().authenticated())
				.formLogin(form -> form.disable()).httpBasic(basic -> basic.disable())
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
				})).exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
					response.setStatus(401);
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					response.setCharacterEncoding("UTF-8");
					try (PrintWriter writer = response.getWriter()) {
						writer.write("""
								  {"timestamp":"%s","error":"UNAUTHORIZED","message":"Authentication required"}
								""".formatted(Instant.now()));
					}
				}));

		return http.build();
	}

	@Bean
	JwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String secret,
			@Value("${security.jwt.issuer}") String issuer) {
		SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).build();

		OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
				JwtValidators.createDefaultWithIssuer(issuer), new JwtTimestampValidator());
		decoder.setJwtValidator(validator);

		return decoder;
	}
}
