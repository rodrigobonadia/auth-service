package tech.bonadia.authservice.application.ports.out;

import java.time.Duration;

public interface TokenIssuerPort {

	IssuedToken issue(String subject, Duration ttl);

	record IssuedToken(String token, Duration ttl) {
	}
}
