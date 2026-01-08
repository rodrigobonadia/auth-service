package tech.bonadia.authservice.domain.user;

import java.time.Instant;
import java.util.Objects;

public final class User {
  private final UserId id;
  private final Email email;
  private final String passwordHash;
  private final boolean enabled;
  private final Instant createdAt;

  private User(UserId id, Email email, String passwordHash, boolean enabled, Instant createdAt) {
    this.id = Objects.requireNonNull(id);
    this.email = Objects.requireNonNull(email);
    this.passwordHash = Objects.requireNonNull(passwordHash);
    this.enabled = enabled;
    this.createdAt = Objects.requireNonNull(createdAt);
  }

  public static User createNew(Email email, String passwordHash) {
    return new User(UserId.newId(), email, passwordHash, true, Instant.now());
  }

  public UserId id() { return id; }
  public Email email() { return email; }
  public String passwordHash() { return passwordHash; }
  public boolean enabled() { return enabled; }
  public Instant createdAt() { return createdAt; }
  public static User rehydrate(UserId id, Email email, String passwordHash, boolean enabled, Instant createdAt) {
	  return new User(id, email, passwordHash, enabled, createdAt);
	}

}
