package tech.bonadia.authservice.application.ports.out;

import java.time.Duration;

public interface TokenTtlProviderPort {
	Duration accessTokenTtl();

	Duration refreshTokenTtl();
}