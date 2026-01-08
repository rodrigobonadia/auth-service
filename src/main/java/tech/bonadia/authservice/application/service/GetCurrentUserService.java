package tech.bonadia.authservice.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.bonadia.authservice.application.ports.in.GetCurrentUserUseCase;
import tech.bonadia.authservice.application.ports.out.UserRepositoryPort;
import tech.bonadia.authservice.domain.user.Email;

@Service
@Transactional(readOnly = true)
public class GetCurrentUserService implements GetCurrentUserUseCase {

  private final UserRepositoryPort userRepository;

  public GetCurrentUserService(UserRepositoryPort userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public CurrentUser getByEmail(String email) {
    var user = userRepository.findByEmail(new Email(email))
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new CurrentUser(
        user.id().value(),
        user.email().value(),
        user.enabled(),
        List.of("USER"),
        "scope"
    );
  }
}
