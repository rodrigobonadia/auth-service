package tech.bonadia.authservice.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.bonadia.authservice.application.ports.in.RegisterUserUseCase;
import tech.bonadia.authservice.application.ports.out.PasswordHasherPort;
import tech.bonadia.authservice.application.ports.out.UserRepositoryPort;
import tech.bonadia.authservice.domain.user.Email;
import tech.bonadia.authservice.domain.user.User;

@Service
@Transactional
public class RegisterUserService implements RegisterUserUseCase {

  private final UserRepositoryPort userRepository;
  private final PasswordHasherPort passwordHasher;

  public RegisterUserService(UserRepositoryPort userRepository,
                             PasswordHasherPort passwordHasher) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
  }

  @Override
  public void register(String email, String rawPassword) {
    Email userEmail = new Email(email);

    if (userRepository.existsByEmail(userEmail)) {
      throw new IllegalStateException("Email already registered");
    }

    String passwordHash = passwordHasher.hash(rawPassword);

    User user = User.createNew(userEmail, passwordHash);

    userRepository.save(user);
  }
}
