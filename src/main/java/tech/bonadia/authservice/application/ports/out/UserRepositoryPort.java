package tech.bonadia.authservice.application.ports.out;

import java.util.Optional;

import tech.bonadia.authservice.domain.user.Email;
import tech.bonadia.authservice.domain.user.User;
import tech.bonadia.authservice.domain.user.UserId;

public interface UserRepositoryPort {

  User save(User user);

  Optional<User> findById(UserId id);

  Optional<User> findByEmail(Email email);

  boolean existsByEmail(Email email);
}
