package tech.bonadia.authservice.application.ports.in.model;

public record RefreshCommand(String refreshToken, String userAgent, String ip) {}
