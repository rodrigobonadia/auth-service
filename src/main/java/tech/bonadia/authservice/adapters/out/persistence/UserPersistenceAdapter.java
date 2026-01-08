package tech.bonadia.authservice.adapters.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tech.bonadia.authservice.adapters.out.persistence.jpa.UserJpaEntity;
import tech.bonadia.authservice.adapters.out.persistence.jpa.UserJpaRepository;
import tech.bonadia.authservice.application.ports.out.UserRepositoryPort;
import tech.bonadia.authservice.domain.user.Email;
import tech.bonadia.authservice.domain.user.User;
import tech.bonadia.authservice.domain.user.UserId;

@Component
@Transactional
public class UserPersistenceAdapter implements UserRepositoryPort {

  private final UserJpaRepository repository;

  public UserPersistenceAdapter(UserJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public User save(User user) {
    UserJpaEntity saved = repository.save(toEntity(user));
    return toDomain(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findById(UserId id) {
    return repository.findById(id.value()).map(this::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByEmail(Email email) {
    return repository.findByEmail(email.value()).map(this::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByEmail(Email email) {
    return repository.existsByEmail(email.value());
  }

  private UserJpaEntity toEntity(User user) {
    return new UserJpaEntity(
        user.id().value(),
        user.email().value(),
        user.passwordHash(),
        user.enabled(),
        user.createdAt()
    );
  }

  private User toDomain(UserJpaEntity entity) {
	  return User.rehydrate(
	      new UserId(entity.getId()),
	      new Email(entity.getEmail()),
	      entity.getPasswordHash(),
	      entity.isEnabled(),
	      entity.getCreatedAt()
	  );
	}

}
