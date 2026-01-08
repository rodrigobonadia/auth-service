package tech.bonadia.authservice.adapters.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import tech.bonadia.authservice.application.ports.out.TokenTtlProviderPort;

@ConfigurationProperties(prefix = "security.tokens")
public class TokenTtlProperties implements TokenTtlProviderPort {

	private final Duration accessTtl;
	private final Duration refreshTtl;

	public TokenTtlProperties(@DefaultValue("15m") Duration accessTtl, @DefaultValue("7d") Duration refreshTtl) {
		this.accessTtl = accessTtl;
		this.refreshTtl = refreshTtl;
	}

	@Override
	public Duration accessTokenTtl() {
		return accessTtl;
	}

	@Override
	public Duration refreshTokenTtl() {
		return refreshTtl;
	}
}
