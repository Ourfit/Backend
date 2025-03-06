package io.ourfit.api.domain.user.data.entity.enums;

import java.util.Arrays;
import java.util.Optional;

public enum GenderType {
  F,
  M;

  public static Optional<GenderType> findByName(String value) {
    return Arrays.stream(values()).filter(v -> v.name().equals(value)).findFirst();
  }
}
