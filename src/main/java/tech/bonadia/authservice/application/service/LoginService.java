package tech.bonadia.authservice.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.bonadia.authservice.application.ports.in.LoginUseCase;
import tech.bonadia.authservice.application.ports.out.TokenIssuerPort;
import tech.bonadia.authservice.application.ports.out.TokenTtlProviderPort;

@Service
@Transactional(readOnly = true)
public class LoginService implements LoginUseCase {

	private final AuthenticateUserService authenticateUserService;
	private final TokenIssuerPort tokenIssuer;
	private final RefreshTokenService refreshTokenService;
	private final TokenTtlProviderPort ttlProvider;

	public LoginService(AuthenticateUserService authenticateUserService, TokenIssuerPort tokenIssuer,
			RefreshTokenService refreshTokenService, TokenTtlProviderPort ttlProvider) {
		this.authenticateUserService = authenticateUserService;
		this.tokenIssuer = tokenIssuer;
		this.refreshTokenService = refreshTokenService;
		this.ttlProvider = ttlProvider;
	}

	@Override
	public LoginResult login(String email, String rawPassword) {
		authenticateUserService.authenticate(email, rawPassword);

		var accessTtl = ttlProvider.accessTokenTtl();
		var refreshTtl = ttlProvider.refreshTokenTtl();

		var access = tokenIssuer.issue(email, accessTtl);
		var refresh = refreshTokenService.issue(email, refreshTtl);

		return new LoginResult(access.token(), refresh.token(), accessTtl.getSeconds(), refreshTtl.getSeconds());
	}
}
