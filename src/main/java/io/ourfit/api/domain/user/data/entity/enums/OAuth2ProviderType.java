package io.ourfit.api.domain.user.data.entity.enums;

import java.util.Arrays;
import java.util.Optional;

public enum OAuth2ProviderType {
  KAKAO;

  public static Optional<OAuth2ProviderType> findByName(String name) {
    return Arrays.stream(values())
        .filter(provider -> provider.name().equalsIgnoreCase(name))
        .findFirst();
  }
}
