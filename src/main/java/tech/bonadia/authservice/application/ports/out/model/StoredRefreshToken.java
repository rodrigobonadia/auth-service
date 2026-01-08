package tech.bonadia.authservice.application.ports.out.model;

import java.time.Instant;

public record StoredRefreshToken(
    String tokenHash,
    String familyId,
    String userId,
    String email,
    String scope,
    String roles,
    String status,         
    Instant issuedAt,
    Instant expiresAt,
    String replacedByHash
) {}
