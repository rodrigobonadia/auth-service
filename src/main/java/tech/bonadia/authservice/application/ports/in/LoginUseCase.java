package tech.bonadia.authservice.application.ports.in;

public interface LoginUseCase {
	LoginResult login(String email, String rawPassword);

	public record LoginResult(String accessToken, String refreshToken, long expiresInSeconds, long refreshTokenExpiresInSeconds) {
	}

}
