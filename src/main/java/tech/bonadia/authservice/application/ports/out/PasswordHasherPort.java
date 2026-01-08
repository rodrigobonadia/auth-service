package tech.bonadia.authservice.application.ports.out;

public interface PasswordHasherPort {

  String hash(String rawPassword);

  boolean matches(String rawPassword, String hashedPassword);
}