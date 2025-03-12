package io.ourfit.api.global.config.properties;

import java.util.Arrays;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EnvironmentProfileType {
  PRODUCTION("prod"),
  DEVELOPMENT("develop"),
  LOCAL("local");

  private final String value;

  public static EnvironmentProfileType findByValue(String value) {
    return Arrays.stream(values())
        .filter(profile -> profile.value.equals(value))
        .findFirst()
        .orElse(LOCAL);
  }

  public boolean isRemote() {
    return this != LOCAL;
  }
}
