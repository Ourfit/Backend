package io.ourfit.api.domain.user.data.entity.enums;

import java.util.Arrays;

public enum OAuth2ProviderType {
  KAKAO;

  public static OAuth2ProviderType from(String providerValue) {
    return Arrays.stream(values())
        .filter(provider -> provider.name().equalsIgnoreCase(providerValue))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Unsupported OAuth2 provider: " + providerValue));
  }
}
