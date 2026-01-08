package tech.bonadia.authservice.adapters.in.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.bonadia.authservice.adapters.in.web.dto.MeResponse;
import tech.bonadia.authservice.application.ports.in.GetCurrentUserUseCase;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

  private final GetCurrentUserUseCase getCurrentUserUseCase;

  public MeController(GetCurrentUserUseCase getCurrentUserUseCase) {
    this.getCurrentUserUseCase = getCurrentUserUseCase;
  }

  @GetMapping
  public MeResponse me(@AuthenticationPrincipal(expression = "subject") String email) {
    var current = getCurrentUserUseCase.getByEmail(email);

    return new MeResponse(
    	current.id().toString(),
        current.email(),
        current.enabled(),
        current.roles(),
        current.scope()
    );
  }
}
