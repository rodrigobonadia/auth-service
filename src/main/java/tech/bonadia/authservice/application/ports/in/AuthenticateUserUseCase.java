package tech.bonadia.authservice.application.ports.in;

import tech.bonadia.authservice.domain.user.User;

public interface AuthenticateUserUseCase {

  User authenticate(String email, String rawPassword);
}
