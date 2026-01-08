package tech.bonadia.authservice.adapters.in.web.dto;

import java.util.List;

public record MeResponse(String id, String email, boolean enabled, List<String> roles, String scope) {
}
