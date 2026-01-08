package tech.bonadia.authservice.adapters.in.web.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn, String refreshToken,
		long refreshExpiresIn) {
}
