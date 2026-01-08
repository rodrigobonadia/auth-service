package tech.bonadia.authservice.application.ports.in;

import java.time.Duration;

import tech.bonadia.authservice.application.ports.in.model.IssuedRefreshToken;
import tech.bonadia.authservice.application.ports.in.model.RefreshCommand;
import tech.bonadia.authservice.application.ports.in.model.TokenPair;

public interface RefreshTokenUseCase {
  TokenPair refresh(RefreshCommand command);
  
  IssuedRefreshToken issue(String subject, Duration ttl);
}
