package tech.bonadia.authservice.application.ports.in;

public interface RegisterUserUseCase {

  void register(String email, String rawPassword);
}
