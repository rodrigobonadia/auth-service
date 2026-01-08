package tech.bonadia.authservice.application.ports.out;

import java.time.Duration;
import java.util.Optional;

import tech.bonadia.authservice.application.ports.out.model.StoredRefreshToken;

public interface RefreshTokenStorePort {

  Optional<StoredRefreshToken> findByRawToken(String rawRefreshToken);

  void store(StoredRefreshToken token, Duration ttlSeconds);

  void revoke(String rawRefreshToken, String replacedByHash);

  void revokeFamily(String familyId);

  void indexFamilyToken(String familyId, String tokenHash, long ttlSeconds);


}
