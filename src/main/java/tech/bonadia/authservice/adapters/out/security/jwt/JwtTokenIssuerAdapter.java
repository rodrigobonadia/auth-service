package tech.bonadia.authservice.adapters.out.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import tech.bonadia.authservice.application.ports.out.TokenIssuerPort;

@Component
public class JwtTokenIssuerAdapter implements TokenIssuerPort {

	private final String issuer;
	private final SecretKey secretKey;

	public JwtTokenIssuerAdapter(@Value("${security.jwt.issuer}") String issuer,
			@Value("${security.jwt.secret}") String secret) {
		this.issuer = issuer;
		byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
		if (bytes.length < 32) {
			throw new IllegalArgumentException("security.jwt.secret must be at least 32 bytes for HS256");
		}
		this.secretKey = new SecretKeySpec(bytes, "HmacSHA256");
	}

	@Override
	public IssuedToken issue(String subject, Duration ttl) {
		Instant now = Instant.now();
		Instant exp = now.plus(ttl);

		JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder().issuer(issuer).subject(subject)
				.issueTime(java.util.Date.from(now)).expirationTime(java.util.Date.from(exp));

		JWTClaimsSet claimSet = builder.build();

		try {
			JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
			SignedJWT jwt = new SignedJWT(header, claimSet);
			jwt.sign(new MACSigner(secretKey.getEncoded()));
			return new IssuedToken(jwt.serialize(), ttl);
		} catch (Exception e) {
			throw new IllegalStateException("Could not sign token", e);
		}
	}
}
