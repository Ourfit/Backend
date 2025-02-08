package io.ourfit.api.domain.user.data.entity.enums;

import jakarta.annotation.Nullable;
import java.util.Arrays;

public enum GenderType {
  F,
  M;

  @Nullable public static GenderType findByName(String value) {
    return Arrays.stream(values()).filter(v -> v.name().equals(value)).findFirst().orElse(null);
  }
}
