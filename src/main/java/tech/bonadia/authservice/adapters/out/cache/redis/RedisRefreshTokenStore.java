package tech.bonadia.authservice.adapters.out.cache.redis;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import tech.bonadia.authservice.application.ports.out.RefreshTokenStorePort;
import tech.bonadia.authservice.application.ports.out.model.StoredRefreshToken;

@Repository
public class RedisRefreshTokenStore implements RefreshTokenStorePort {

	private final StringRedisTemplate redis;

	public RedisRefreshTokenStore(StringRedisTemplate redis) {
		this.redis = redis;
	}

	@Override
	public Optional<StoredRefreshToken> findByRawToken(String rawRefreshToken) {
		String hash = sha256(rawRefreshToken);
		String key = rtKey(hash);

		Map<Object, Object> data = redis.opsForHash().entries(key);
		if (data == null || data.isEmpty())
			return Optional.empty();

		return Optional.of(new StoredRefreshToken(hash, str(data.get("familyId")), str(data.get("userId")),
				str(data.get("email")), str(data.get("scope")), str(data.get("rolesCsv")), str(data.get("status")),
				Instant.parse(str(data.get("issuedAt"))), Instant.parse(str(data.get("expiresAt"))),
				str(data.get("replacedByHash"))));
	}

	@Override
	public void store(StoredRefreshToken token, Duration ttl) {
		String key = rtKey(token.tokenHash());

		redis.opsForHash().putAll(key,
				Map.of("familyId", token.familyId(), "userId", token.userId(), "email", token.email(), "scope",
						token.scope() == null ? "" : token.scope(), "rolesCsv",
						token.roles() == null ? "" : token.roles(), "status", token.status(), "issuedAt",
						token.issuedAt().toString(), "expiresAt", token.expiresAt().toString(), "replacedByHash",
						token.replacedByHash() == null ? "" : token.replacedByHash()));

		redis.expire(key, ttl);
	}

	@Override
	public void revoke(String rawRefreshToken, String replacedByHash) {
		String oldHash = sha256(rawRefreshToken);
		String key = rtKey(oldHash);
		redis.opsForHash().put(key, "status", "REVOKED");
		redis.opsForHash().put(key, "replacedByHash", replacedByHash == null ? "" : replacedByHash);
	}

	@Override
	public void revokeFamily(String familyId) {
		String famKey = familyKey(familyId);
		Set<String> hashes = redis.opsForSet().members(famKey);
		if (hashes == null)
			return;

		for (String hash : hashes) {
			redis.opsForHash().put(rtKey(hash), "status", "REVOKED");
		}
	}

	@Override
	public void indexFamilyToken(String familyId, String tokenHash, long ttlSeconds) {
		String famKey = familyKey(familyId);
		redis.opsForSet().add(famKey, tokenHash);
		redis.expire(famKey, java.time.Duration.ofSeconds(ttlSeconds));
	}

	private static String rtKey(String hash) {
		return "rt:" + hash;
	}

	private static String familyKey(String familyId) {
		return "rtfam:" + familyId;
	}

	private static String str(Object o) {
		return o == null ? "" : o.toString();
	}

	private static String sha256(String raw) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : digest)
				sb.append(String.format("%02x", b));
			return sb.toString();
		} catch (Exception e) {
			throw new IllegalStateException("Cannot hash refresh token", e);
		}
	}
}
