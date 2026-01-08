package tech.bonadia.authservice.adapters.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tech.bonadia.authservice.adapters.in.web.dto.LoginRequest;
import tech.bonadia.authservice.adapters.in.web.dto.RefreshRequest;
import tech.bonadia.authservice.adapters.in.web.dto.RegisterRequest;
import tech.bonadia.authservice.adapters.in.web.dto.TokenResponse;
import tech.bonadia.authservice.application.ports.in.LoginUseCase;
import tech.bonadia.authservice.application.ports.in.RefreshTokenUseCase;
import tech.bonadia.authservice.application.ports.in.RegisterUserUseCase;
import tech.bonadia.authservice.application.ports.in.model.RefreshCommand;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

	private final RegisterUserUseCase registerUserUseCase;
	private final LoginUseCase loginUseCase;
	private final RefreshTokenUseCase refreshTokenUseCase;

	public AuthController(RegisterUserUseCase registerUserUseCase, LoginUseCase loginUseCase, RefreshTokenUseCase refreshTokenUseCase) {
	    this.refreshTokenUseCase = refreshTokenUseCase;
		this.registerUserUseCase = registerUserUseCase;
		this.loginUseCase = loginUseCase;
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
		registerUserUseCase.register(req.email(), req.password());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
	  var result = loginUseCase.login(req.email(), req.password());

	  return ResponseEntity.ok(new TokenResponse(
	      result.accessToken(),
	      "Bearer",
	      result.expiresInSeconds(), result.refreshToken(), result.refreshTokenExpiresInSeconds()
	  ));
	}

	@PostMapping("/refresh")
	  public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest req,
	                                               @RequestHeader(value = "User-Agent", required = false) String ua,
	                                               @RequestHeader(value = "X-Forwarded-For", required = false) String ip) {

	    var pair = refreshTokenUseCase.refresh(new RefreshCommand(req.refreshToken(), ua, ip));

	    return ResponseEntity.ok(new TokenResponse(
	        pair.accessToken(),
	        "Bearer",
	        pair.accessExpiresInSeconds(),
	        pair.refreshToken(),
	        pair.refreshExpiresInSeconds()
	    ));
	  }
}
