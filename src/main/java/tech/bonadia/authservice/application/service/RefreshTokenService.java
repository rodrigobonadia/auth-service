package tech.bonadia.authservice.application.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tech.bonadia.authservice.application.ports.in.RefreshTokenUseCase;
import tech.bonadia.authservice.application.ports.in.model.IssuedRefreshToken;
import tech.bonadia.authservice.application.ports.in.model.RefreshCommand;
import tech.bonadia.authservice.application.ports.in.model.TokenPair;
import tech.bonadia.authservice.application.ports.out.RefreshTokenStorePort;
import tech.bonadia.authservice.application.ports.out.TokenHasherPort;
import tech.bonadia.authservice.application.ports.out.TokenIssuerPort;
import tech.bonadia.authservice.application.ports.out.model.StoredRefreshToken;

@Service
public class RefreshTokenService implements RefreshTokenUseCase {

  private final RefreshTokenStorePort refreshTokenStore;
  private final TokenIssuerPort tokenIssuerPort; 
  private final TokenHasherPort tokenHasher;
  
  private static final Duration ACCESS_TTL = Duration.ofMinutes(1);
  private static final Duration REFRESH_TTL = Duration.ofMinutes(5);


  public RefreshTokenService(RefreshTokenStorePort refreshTokenStore, TokenIssuerPort tokenIssuerPort, TokenHasherPort tokenHasher) {
    this.refreshTokenStore = refreshTokenStore;
    this.tokenIssuerPort = tokenIssuerPort;
    this.tokenHasher = tokenHasher;
  }

  @Override
  public TokenPair refresh(RefreshCommand command) {
    var existing = refreshTokenStore.findByRawToken(command.refreshToken())
        .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

    if (!"ACTIVE".equals(existing.status())) {
      refreshTokenStore.revokeFamily(existing.familyId());
      throw new IllegalArgumentException("Refresh token reuse detected");
    }

    String newRefresh = UUID.randomUUID().toString();
    String newHash = hash(newRefresh); 

    refreshTokenStore.revoke(command.refreshToken(), newHash);

    Instant now = Instant.now();
    StoredRefreshToken rotated = new StoredRefreshToken(
        newHash,
        existing.familyId(),
        existing.userId(),
        existing.email(),
        existing.scope(),
        existing.roles(),
        "ACTIVE",
        now,
        now.plusSeconds(REFRESH_TTL.getSeconds()),
        ""
    );

    refreshTokenStore.store(rotated, REFRESH_TTL);
    refreshTokenStore.indexFamilyToken(existing.familyId(), newHash, REFRESH_TTL.getSeconds());

    String subject = existing.userId().toString(); 
    String access = tokenIssuerPort.issue(subject, ACCESS_TTL).token();
    return new TokenPair(access, ACCESS_TTL.getSeconds(), newRefresh, REFRESH_TTL.getSeconds());
  }
  
  @Override
  public IssuedRefreshToken issue(String subject, Duration ttl) {
	  String raw = UUID.randomUUID().toString();       
	  String hashed = hash(raw);           
	  Instant now = Instant.now();
	  Instant expiresAt = now.plus(ttl);

	  var stored = new StoredRefreshToken(
	      hashed,
	      UUID.randomUUID().toString(),
	      subject,
	      subject,
	      null,
	      null,
	      "ACTIVE",
	      now,
	      expiresAt,
	      null
	  );

	  refreshTokenStore.store(stored, ttl);

    return new IssuedRefreshToken(raw, ttl);
  }

  private String hash(String raw) {
	  return tokenHasher.sha256(raw);
	}
}
