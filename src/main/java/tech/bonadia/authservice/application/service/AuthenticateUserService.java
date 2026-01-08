package tech.bonadia.authservice.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.bonadia.authservice.application.ports.in.AuthenticateUserUseCase;
import tech.bonadia.authservice.application.ports.out.PasswordHasherPort;
import tech.bonadia.authservice.application.ports.out.UserRepositoryPort;
import tech.bonadia.authservice.domain.user.Email;
import tech.bonadia.authservice.domain.user.User;

@Service
@Transactional(readOnly = true)
public class AuthenticateUserService implements AuthenticateUserUseCase {

  private final UserRepositoryPort userRepository;
  private final PasswordHasherPort passwordHasher;

  public AuthenticateUserService(UserRepositoryPort userRepository,
                                 PasswordHasherPort passwordHasher) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
  }

  @Override
  public User  authenticate(String email, String rawPassword) {
    Email userEmail = new Email(email);

    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

    if (!user.enabled()) {
      throw new IllegalArgumentException("User disabled");
    }

    if (!passwordHasher.matches(rawPassword, user.passwordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }
    
    return user;
  }
}
