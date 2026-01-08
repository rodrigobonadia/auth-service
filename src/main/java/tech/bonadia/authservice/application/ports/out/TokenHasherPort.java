package tech.bonadia.authservice.application.ports.out;

public interface TokenHasherPort {
  String sha256(String raw);
}
