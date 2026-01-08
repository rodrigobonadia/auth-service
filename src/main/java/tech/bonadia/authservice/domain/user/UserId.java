package tech.bonadia.authservice.domain.user;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {

	public UserId {
		Objects.requireNonNull(value, "UserId value must not be null");
	}

	public static UserId generate() {
		return new UserId(UUID.randomUUID());
	}

	public static UserId from(String raw) {
		return new UserId(UUID.fromString(raw));
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
