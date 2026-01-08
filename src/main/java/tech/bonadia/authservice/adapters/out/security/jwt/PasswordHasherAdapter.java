package tech.bonadia.authservice.adapters.out.security.jwt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import tech.bonadia.authservice.application.ports.out.PasswordHasherPort;

@Component
public class PasswordHasherAdapter implements PasswordHasherPort {

  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Override
  public String hash(String rawPassword) {
    return encoder.encode(rawPassword);
  }

  @Override
  public boolean matches(String rawPassword, String hashedPassword) {
    return encoder.matches(rawPassword, hashedPassword);
  }
}
