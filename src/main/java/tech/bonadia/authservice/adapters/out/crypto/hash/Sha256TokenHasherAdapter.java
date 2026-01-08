package tech.bonadia.authservice.adapters.out.crypto.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import tech.bonadia.authservice.application.ports.out.TokenHasherPort;

@Component
public class Sha256TokenHasherAdapter implements TokenHasherPort {

	@Override
	public String sha256(String raw) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));

			StringBuilder sb = new StringBuilder(hash.length * 2);
			for (byte b : hash)
				sb.append(String.format("%02x", b));
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 not available", e);
		}
	}
}
