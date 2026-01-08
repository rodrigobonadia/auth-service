package tech.bonadia.authservice.domain.user;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Email {
  private static final Pattern EMAIL =
      Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

  private final String value;

  public Email(String value) {
    if (value == null || value.isBlank() || !EMAIL.matcher(value).matches()) {
      throw new IllegalArgumentException("Invalid email");
    }
    this.value = value.toLowerCase();
  }

  public String value() {
    return value;
  }

  @Override public boolean equals(Object o) {
    return (o instanceof Email other) && Objects.equals(this.value, other.value);
  }

  @Override public int hashCode() {
    return Objects.hash(value);
  }

  @Override public String toString() {
    return value;
  }
}
