package tech.bonadia.authservice.application.ports.in.model;

public record TokenPair(
    String accessToken,
    long accessExpiresInSeconds,
    String refreshToken,
    long refreshExpiresInSeconds
) {}
