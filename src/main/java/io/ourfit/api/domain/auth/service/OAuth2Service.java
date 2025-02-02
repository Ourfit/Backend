package io.ourfit.api.domain.auth.service;

import io.ourfit.api.domain.auth.data.dto.OAuth2UserInfo;
import io.ourfit.api.domain.user.entity.enums.OAuth2ProviderType;

public interface OAuth2Service {

  OAuth2UserInfo authenticate(OAuth2ProviderType providerType, String code);
}
