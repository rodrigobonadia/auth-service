package tech.bonadia.authservice.application.ports.in;

import java.util.List;
import java.util.UUID;

public interface GetCurrentUserUseCase {

  CurrentUser getByEmail(String email);

  record CurrentUser(
      UUID id,
      String email,
      boolean enabled,
      List<String> roles,
      String scope
  ) {}
}
