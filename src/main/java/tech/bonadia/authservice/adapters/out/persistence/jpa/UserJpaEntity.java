package tech.bonadia.authservice.adapters.out.persistence.jpa;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserJpaEntity {

  @Id
  @Column(nullable = false)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private boolean enabled;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected UserJpaEntity() {
  }

  public UserJpaEntity(UUID id, String email, String passwordHash, boolean enabled, Instant createdAt) {
    this.id = id;
    this.email = email;
    this.passwordHash = passwordHash;
    this.enabled = enabled;
    this.createdAt = createdAt;
  }

  public UUID getId() { return id; }
  public String getEmail() { return email; }
  public String getPasswordHash() { return passwordHash; }
  public boolean isEnabled() { return enabled; }
  public Instant getCreatedAt() { return createdAt; }
}
