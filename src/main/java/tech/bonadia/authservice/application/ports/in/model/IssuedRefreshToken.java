package tech.bonadia.authservice.application.ports.in.model;

import java.time.Duration;

public record IssuedRefreshToken(
    String token,
    Duration ttl
) {}
